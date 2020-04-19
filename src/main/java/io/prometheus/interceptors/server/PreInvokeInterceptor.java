package io.prometheus.interceptors.server;

import io.prometheus.prometheus.PrometheusMetricsService;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import static io.prometheus.prometheus.PrometheusMetrics.CXF_REQUEST_EXECUTION_TIME_COUNTER;

public class PreInvokeInterceptor extends AbstractPhaseInterceptor<Message> {

    public PreInvokeInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        PrometheusMetricsService
            .create(message)
            .startTimer(CXF_REQUEST_EXECUTION_TIME_COUNTER);
    }
}
