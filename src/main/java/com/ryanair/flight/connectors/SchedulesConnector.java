package com.ryanair.flight.connectors;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ryanair.flight.config.properties.ExternalServices;
import com.ryanair.flight.model.input.Schedule;

@Component
public class SchedulesConnector {

    @Autowired
    ExternalServices externalServices;

    private static final Logger logger = LogManager.getLogger(SchedulesConnector.class);

    /**
     * Retrieves the schedule for a route in one month of the year.
     * 
     * 
     * @param departure
     *            Departure Airport
     * @param arrival
     *            Arrival Airport
     * @param year
     *            Year
     * @param month
     *            Month
     * @return the schedule for the departure airport, arrival airport, year and
     *         month given
     */
    public Schedule getSchedule(String departure, String arrival, int year, int month) {

	Schedule schedule = null;

	Map<String, Object> uriParams = fillUriParams(departure, arrival, year, month);
	RestTemplate restTemplate = new RestTemplate();

	try {
	    ResponseEntity<Schedule> responseEntity = restTemplate.getForEntity(externalServices.getSchedulesUrl(), Schedule.class, uriParams);

	    if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
		schedule = responseEntity.getBody();
	    }
	} catch (HttpClientErrorException e) {
	    logger.warn("getSchedule ::: Error retrieving schedule from departure airport " + departure + " to arrival airport " + arrival + " for "
		    + String.valueOf(month) + "/" + String.valueOf(year) + ". " + e.getMessage());
	}

	return schedule;
    }

    /**
     * Fills a map with the uriparams values
     * 
     * 
     * @param departure
     *            UriParam Departure airport value
     * @param arrival
     *            UriParam Arrival airport value
     * @param year
     *            UriParam year value
     * @param month
     *            UriParam month value
     * @return a map with the UriParam values
     */
    private Map<String, Object> fillUriParams(String departure, String arrival, int year, int month) {
	Map<String, Object> uriParams = new HashMap<String, Object>();

	uriParams.put("departure", departure);
	uriParams.put("arrival", arrival);
	uriParams.put("year", year);
	uriParams.put("month", month);

	return uriParams;
    }

}
