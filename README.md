# CXF metrics for prometheus

Collects the following metrics for each endpoint and operation 

    cxf_requests_total      - total number of incoming cxf requests,
    cxf_requests_success    - total number of successfully processed cxf requests,
    cxf_requests_failed     - total number of failed cxf requests,
    cxf_requests_seconds    - execution time of cxf request.

For example, for configuration bellow

    @Bean
    public Endpoint server1(final Bus bus, final MyFirstWebService service) {
        final EndpointImpl endpoint = new EndpointImpl(bus, service);
        endpoint.setEndpointName(new QName("server1"));
        endpoint.publish("/server1");
        return endpoint;
    }

    @Bean
    public Endpoint server2(final Bus bus, final MySecondWebService service) {
        final EndpointImpl endpoint = new EndpointImpl(bus, service);
        endpoint.setEndpointName(new QName("server2"));
        endpoint.publish("/server2");
        return endpoint;
    }
    
    @Bean
    public MyCxfClient client(final @Value("${my.cxf.client.url}") String url) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress(url);
        factory.setEndpointName(new QName("client"));
        return factory.create(MyCxfClient.class);
    }

the cxf_requests_total metric will be available for 'server1' endpoint as 'cxf_requests_total{endpoint="server1",operation="server1Method"}' for each web service method of MyFirstWebService.  
And the same for other metrics of MyFirstWebService, MySecondWebService and MyCxfClient.

To enable cxf metrics for prometheus add dependency to your pom.xml.

    <dependency>
        <groupId>io.github.ddk-prog</groupId>
        <artifactId>cxf-prometheus-metrics</artifactId>
        <version>1.0.0</version>
    </dependency>
 
Add the following bean to your application configuration.

    @Bean
    public FactoryBeanListener cxfPrometheusFeatureBean(final CollectorRegistry registry) {
        return new PrometheusFactoryBeanListener(registry);
    }