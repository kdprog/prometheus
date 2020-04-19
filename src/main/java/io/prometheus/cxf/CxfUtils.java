package io.prometheus.cxf;

import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.EndpointInfo;

import javax.xml.namespace.QName;
import java.util.Optional;

import static java.util.Objects.nonNull;

public final class CxfUtils {

    private CxfUtils() {
    }

    public static String describeEndpoint(final Message message) {
        return explainQName(
            findEndpointInfo(getExchange(message))
                .map(EndpointInfo::getName)
        );
    }

    public static String extractOperation(final Message message) {
        return explainQName(
            findBindings(getExchange(message))
                .map(BindingOperationInfo::getName)
        );
    }

    public static Exchange getExchange(final Message message) {
        return message.getExchange();
    }

    public static boolean hasErrors(final Message message) {
        return nonNull(message.getContent(Exception.class));
    }

    private static String explainQName(final Optional<QName> name) {
        return name
            .map(QName::getLocalPart)
            .orElse("unknown");
    }

    private static Optional<BindingOperationInfo> findBindings(final Exchange exchange) {
        return Optional.ofNullable(exchange.getBindingOperationInfo());
    }

    private static Optional<EndpointInfo> findEndpointInfo(final Exchange exchange) {
        return Optional.ofNullable(exchange.getEndpoint())
            .map(Endpoint::getEndpointInfo);
    }
}
