package io.prometheus.feature;

import io.prometheus.interceptors.client.FaultInInterceptor;
import io.prometheus.interceptors.client.PostInvokeInterceptor;
import io.prometheus.interceptors.client.SetupInterceptor;
import io.prometheus.interceptors.server.FaultOutInterceptor;
import io.prometheus.interceptors.server.PreInvokeInterceptor;
import io.prometheus.interceptors.server.SendInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;

public class PrometheusMetricsFeature extends AbstractFeature {

    @Override
    public void initialize(final Client client, final Bus bus) {
        client.getOutInterceptors().add(new SetupInterceptor());
        client.getInFaultInterceptors().add(new FaultInInterceptor());
        client.getInInterceptors().add(new PostInvokeInterceptor());
    }

    @Override
    public void initialize(final Server server, final Bus bus) {
        final Endpoint endpoint = server.getEndpoint();
        endpoint.getOutInterceptors().add(new SendInterceptor());
        endpoint.getOutFaultInterceptors().add(new FaultOutInterceptor());
        endpoint.getInInterceptors().add(new PreInvokeInterceptor());
    }
}
