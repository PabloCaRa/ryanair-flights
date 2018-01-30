package com.ryanair.flight.connectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.flight.config.properties.ExternalServices;
import com.ryanair.flight.model.input.Route;

@Component
public class RoutesConnector {

    @Autowired
    ExternalServices externalServices;

    private static final Logger logger = LogManager.getLogger(RoutesConnector.class);

    /**
     * Connects to the Ryanair API to retrieve the routes info and filter them
     * to get only the ones which has no connecting airport
     * 
     * @return list with all the routes without connecting airport
     */
    public List<Route> getRoutes() {
	List<Route> availableRoutes = null;

	RestTemplate restTemplate = new RestTemplate();

	try {
	    ResponseEntity<Route[]> responseEntity = restTemplate.getForEntity(externalServices.getRoutesUrl(), Route[].class);

	    if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
		List<Route> routes = new ArrayList<>(Arrays.asList(responseEntity.getBody()));
		availableRoutes = routes.stream().filter(route -> StringUtils.isBlank(route.getConnectingAirport())).collect(Collectors.toList());
	    }
	} catch (HttpClientErrorException e) {
	    logger.warn("getRoutes ::: Error retrieving routes. " + e.getMessage());
	}

	return availableRoutes;
    }

}
