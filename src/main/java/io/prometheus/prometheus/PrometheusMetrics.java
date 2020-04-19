package io.prometheus.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
        Gauge.build()
            .name("cxf_requests_seconds")
            .labelNames("endpoint", "operation")
            .help("execution time of cxf request")
            .create()
    );

    private final Gauge metric;

    public static void registerMetrics(final CollectorRegistry registry) {
        Arrays.stream(values())
            .forEach(value -> value.getMetric().register(registry));
    }
}
