package com.userdata.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * The ExternalRestConfig class will provide the RestTemplate to make a REST call by the application.
 * If the app is running behind a firewall then rest template can be configured to hit the proxy to communicate to the
 * external world. Plus RestTemplate is configured to use Connection time out(ms)  and Read time out(ms).
 */

@Configuration
public class ExternalRestConfig {

    @Value("${proxy.host}")
    private String host;

    @Value("${proxy.port}")
    private Integer port;

    @Value("${connection.timeout}")
    private Integer connectionTimeout;

    @Value("${read.timeout}")
    private Integer readTimeout;


    @Bean
    public RestTemplate restTemplate() {
        if (StringUtils.hasLength(host) && port !=null ) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            requestFactory.setProxy(proxy);

            requestFactory.setConnectTimeout(connectionTimeout);
            requestFactory.setReadTimeout(readTimeout);

            return new RestTemplate(requestFactory);
        }
        return new RestTemplate();
    }

}
