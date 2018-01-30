package com.ryanair.flight.services;

import java.time.LocalDateTime;
import java.util.List;

import com.ryanair.flight.model.output.InterconnectedFlight;

public interface InterconnectingFlightsService {

    public List<InterconnectedFlight> getInterconnectedFlights(String departure, String arrival, LocalDateTime departureDateTime,
	    LocalDateTime arrivalDateTime);

}
