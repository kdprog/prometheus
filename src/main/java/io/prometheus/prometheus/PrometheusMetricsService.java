package io.prometheus.prometheus;

import io.prometheus.client.Histogram;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

import java.util.Optional;

import static io.prometheus.cxf.CxfUtils.describeEndpoint;
import static io.prometheus.cxf.CxfUtils.extractOperation;
import static io.prometheus.cxf.CxfUtils.getExchange;
import static io.prometheus.cxf.CxfUtils.hasErrors;

public class PrometheusMetricsService {
    private final Exchange exchange;
    private final String[] labels;
    private final boolean failed;

    private PrometheusMetricsService(final Message message) {
        this.exchange = getExchange(message);
        this.failed = hasErrors(message);
        this.labels = new String[] {describeEndpoint(message), extractOperation(message)};
    }

    public static PrometheusMetricsService create(final Message message) {
        return new PrometheusMetricsService(message);
    }

    public PrometheusMetricsService startTimer(final PrometheusMetrics metric) {
        metric.getHistogram()
            .ifPresent(
                hist ->
                    exchange.putIfAbsent(
                        metric.name(),
                        hist
                            .labels(labels)
                            .startTimer()
                    )
            );
        return this;
    }

    public PrometheusMetricsService stopTimer(final PrometheusMetrics metric) {
        Optional.ofNullable(exchange.get(metric.name()))
            .filter(timer -> Histogram.Timer.class.isAssignableFrom(timer.getClass()))
            .map(timer -> (Histogram.Timer) timer)
            .ifPresent(Histogram.Timer::observeDuration);
        return this;
    }

    public PrometheusMetricsService inc(final PrometheusMetrics metric) {
        metric.getGauge()
            .ifPresent(
                gauge ->
                    gauge
                        .labels(labels)
                        .inc()
            );
        return this;
    }

    public PrometheusMetricsService incSuccess(final PrometheusMetrics metric) {
        final PrometheusMetricsService current;
        if (failed) {
            current = this;
        } else {
            current = inc(metric);
        }
        return current;
    }

    public PrometheusMetricsService incErrors(final PrometheusMetrics metric) {
        final PrometheusMetricsService current;
        if (failed) {
            current = inc(metric);
        } else {
            current = this;
        }
        return current;
    }
}
