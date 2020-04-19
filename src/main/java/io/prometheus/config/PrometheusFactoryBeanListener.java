package io.prometheus.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.feature.PrometheusMetricsFeature;
import io.prometheus.prometheus.PrometheusMetrics;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.service.factory.AbstractServiceFactoryBean;
import org.apache.cxf.service.factory.FactoryBeanListener;

import static org.apache.cxf.service.factory.FactoryBeanListener.Event.CLIENT_CREATED;
import static org.apache.cxf.service.factory.FactoryBeanListener.Event.SERVER_CREATED;

public class PrometheusFactoryBeanListener implements FactoryBeanListener {
    private final PrometheusMetricsFeature feature;

    public PrometheusFactoryBeanListener(final CollectorRegistry registry) {
        this.feature = new PrometheusMetricsFeature();
        PrometheusMetrics.registerMetrics(registry);
    }

    public PrometheusFactoryBeanListener() {
        this(CollectorRegistry.defaultRegistry);
    }

    @Override
    public void handleEvent(final Event ev, final AbstractServiceFactoryBean factory, final Object... args) {
        if (ev == SERVER_CREATED) {
            feature.initialize((Server) args[0], factory.getBus());
        } else if (ev == CLIENT_CREATED) {
            feature.initialize((Client) args[0], factory.getBus());
        }
    }
}
