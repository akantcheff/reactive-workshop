package com.bonitasoft.reactiveworkshop.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "bonitasoft.external-service")
@Configuration("externalService")
@Getter
@Setter
public class ExternalService {
    /**
     * Root URL of the external service API
     */
    private String url;
}
