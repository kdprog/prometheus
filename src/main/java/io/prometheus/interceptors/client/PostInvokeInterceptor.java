package io.prometheus.interceptors.client;

import io.prometheus.prometheus.PrometheusMetricsService;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import static io.prometheus.prometheus.PrometheusMetrics.FAILED_CXF_REQUEST_COUNTER;
import static io.prometheus.prometheus.PrometheusMetrics.CXF_REQUEST_EXECUTION_TIME_COUNTER;
import static io.prometheus.prometheus.PrometheusMetrics.SUCCESS_CXF_REQUEST_COUNTER;
import static io.prometheus.prometheus.PrometheusMetrics.TOTAL_CXF_REQUEST_COUNTER;

public class PostInvokeInterceptor extends AbstractPhaseInterceptor<Message> {

    public PostInvokeInterceptor() {
        super(Phase.POST_INVOKE);
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        PrometheusMetricsService
            .create(message)
            .incSuccess(SUCCESS_CXF_REQUEST_COUNTER)
            .inc(TOTAL_CXF_REQUEST_COUNTER)
            .incErrors(FAILED_CXF_REQUEST_COUNTER)
            .stopTimer(CXF_REQUEST_EXECUTION_TIME_COUNTER);
    }
}
