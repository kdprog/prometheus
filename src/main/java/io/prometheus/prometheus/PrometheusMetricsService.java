package io.prometheus.prometheus;

import io.prometheus.client.Gauge;
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
        exchange.putIfAbsent(
            metric.name(),
            metric.getMetric()
                .labels(labels)
                .startTimer()
        );
        return this;
    }

    public PrometheusMetricsService stopTimer(final PrometheusMetrics metric) {
        Optional.ofNullable(exchange.get(metric.name()))
            .filter(timer -> Gauge.Timer.class.isAssignableFrom(timer.getClass()))
            .map(timer -> (Gauge.Timer) timer)
            .ifPresent(Gauge.Timer::setDuration);
        return this;
    }

    public PrometheusMetricsService inc(final PrometheusMetrics metric) {
        metric.getMetric()
            .labels(labels)
            .inc();
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
