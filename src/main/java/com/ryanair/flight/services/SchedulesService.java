package com.ryanair.flight.services;

import java.time.LocalDateTime;

import com.ryanair.flight.model.holders.FlightInfo;

public interface SchedulesService {

    public FlightInfo getFlightsByDepartureTimeBetweenAirports(String departureAirport, String arrivalAirport, LocalDateTime limitDepartureTime,
	    LocalDateTime limitArrivalTime);
}
