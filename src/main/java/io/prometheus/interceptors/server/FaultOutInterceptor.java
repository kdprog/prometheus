package io.prometheus.interceptors.server;

import io.prometheus.prometheus.PrometheusMetricsService;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import static io.prometheus.prometheus.PrometheusMetrics.FAILED_CXF_REQUEST_COUNTER;
import static io.prometheus.prometheus.PrometheusMetrics.CXF_REQUEST_EXECUTION_TIME_COUNTER;
import static io.prometheus.prometheus.PrometheusMetrics.TOTAL_CXF_REQUEST_COUNTER;

public class FaultOutInterceptor extends AbstractPhaseInterceptor<Message> {

    public FaultOutInterceptor() {
        super(Phase.SEND);
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        PrometheusMetricsService
            .create(message)
            .incErrors(FAILED_CXF_REQUEST_COUNTER)
            .inc(TOTAL_CXF_REQUEST_COUNTER)
            .stopTimer(CXF_REQUEST_EXECUTION_TIME_COUNTER);
    }
}
