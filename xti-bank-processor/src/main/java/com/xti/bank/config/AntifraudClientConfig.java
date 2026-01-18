package com.xti.bank.config;

import com.xti.bank.client.antifraud.AntifraudClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AntifraudClientConfig {

    @Bean
    AntifraudClient antifraudClient(
            RestClient.Builder builder,
            @Value("${antifraud.base-url}") String baseUrl
    ) {
        RestClient restClient = builder
                .baseUrl(baseUrl)
                .build();

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory
                        .builderFor(RestClientAdapter.create(restClient))
                        .build();

        return factory.createClient(AntifraudClient.class);
    }
}
