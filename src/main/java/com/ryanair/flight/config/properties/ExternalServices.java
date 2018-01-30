package com.ryanair.flight.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("externalServices")
public class ExternalServices {

    private String routesUrl;

    private String schedulesUrl;

    public String getRoutesUrl() {
	return routesUrl;
    }

    public void setRoutesUrl(String routesUrl) {
	this.routesUrl = routesUrl;
    }

    public String getSchedulesUrl() {
	return schedulesUrl;
    }

    public void setSchedulesUrl(String schedulesUrl) {
	this.schedulesUrl = schedulesUrl;
    }

}
