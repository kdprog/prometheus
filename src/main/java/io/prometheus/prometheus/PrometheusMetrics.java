package io.prometheus.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.SimpleCollector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum PrometheusMetrics {
    TOTAL_CXF_REQUEST_COUNTER(
        Gauge.build()
            .name("cxf_requests_total")
            .labelNames("endpoint", "operation")
            .help("total number of incoming cxf requests")
            .create()
    ),
    SUCCESS_CXF_REQUEST_COUNTER(
        Gauge.build()
            .name("cxf_requests_success")
            .labelNames("endpoint", "operation")
            .help("total number of successfully processed cxf requests")
            .create()
    ),
    FAILED_CXF_REQUEST_COUNTER(
        Gauge.build()
            .name("cxf_requests_failed")
            .labelNames("endpoint", "operation")
            .help("total number of failed cxf requests")
            .create()
    ),
    CXF_REQUEST_EXECUTION_TIME_COUNTER(
        Histogram.build()
            .name("cxf_requests_seconds")
            .labelNames("endpoint", "operation")
            .help("execution time of cxf request")
            .create()
    );

    private final SimpleCollector metric;

    public Optional<Gauge> getGauge() {
        return Gauge.class.isAssignableFrom(metric.getClass()) ? Optional.of((Gauge) metric) : Optional.empty();
    }

    public Optional<Histogram> getHistogram() {
        return Histogram.class.isAssignableFrom(metric.getClass()) ? Optional.of((Histogram) metric) : Optional.empty();
    }

    public static void registerMetrics(final CollectorRegistry registry) {
        Arrays.stream(values())
            .forEach(value -> value.metric.register(registry));
    }
}
