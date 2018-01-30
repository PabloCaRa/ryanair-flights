package com.ryanair.flight.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ryanair.flight.model.output.InterconnectedFlight;
import com.ryanair.flight.model.params.InterconnectedFlightsParams;
import com.ryanair.flight.services.InterconnectingFlightsService;
import com.ryanair.flight.utils.LocalDateTimeUtils;

@RestController
@RequestMapping("/interconnections")
public class InterconnectingFlightsController {

    @Autowired
    InterconnectingFlightsService interconnectingFlightsService;

    private static final Logger logger = LogManager.getLogger(InterconnectingFlightsController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<InterconnectedFlight>> getInterconnectedFlights(
	    @RequestParam(value = "departure", required = true) String departureAirport,
	    @RequestParam(value = "arrival", required = true) String arrivalAirport,
	    @RequestParam(value = "departureDateTime", required = true) String departureDateTime,
	    @RequestParam(value = "arrivalDateTime", required = true) String arrivalDateTime) {

	ResponseEntity<List<InterconnectedFlight>> response = null;

	InterconnectedFlightsParams params = new InterconnectedFlightsParams().withDepartureAirport(departureAirport)
		.withArrivalAirport(arrivalAirport);
	params.withDepartureTime(departureDateTime).withArrivalTime(arrivalDateTime);

	if (params.isValid()) {
	    try {
		LocalDateTime departureLocalDateTime = LocalDateTimeUtils.parseStringToLocalDateTime(departureDateTime);
		LocalDateTime arrivalLocalDateTime = LocalDateTimeUtils.parseStringToLocalDateTime(arrivalDateTime);

		List<InterconnectedFlight> interconnectingFlights = interconnectingFlightsService.getInterconnectedFlights(departureAirport,
			arrivalAirport, departureLocalDateTime, arrivalLocalDateTime);

		if (CollectionUtils.isNotEmpty(interconnectingFlights)) {
		    response = ResponseEntity.ok(interconnectingFlights);
		} else {
		    response = ResponseEntity.notFound().build();
		}
	    } catch (DateTimeParseException e) {
		logger.warn("getInterconnectedFlights ::: Invalid date. Departure time: " + departureDateTime + ". Arrival time: " + arrivalDateTime);
		response = ResponseEntity.badRequest().build();
	    }

	} else {
	    logger.warn("getInterconnectedFlights ::: Invalid params");
	    response = ResponseEntity.badRequest().build();
	}

	return response;
    }

}
