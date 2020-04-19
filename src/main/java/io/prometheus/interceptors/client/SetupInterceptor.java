package io.prometheus.interceptors.client;

import io.prometheus.prometheus.PrometheusMetricsService;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import static io.prometheus.prometheus.PrometheusMetrics.CXF_REQUEST_EXECUTION_TIME_COUNTER;

public class SetupInterceptor extends AbstractPhaseInterceptor<Message> {

    public SetupInterceptor() {
        super(Phase.SETUP);
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        PrometheusMetricsService
            .create(message)
            .startTimer(CXF_REQUEST_EXECUTION_TIME_COUNTER);
    }
}
