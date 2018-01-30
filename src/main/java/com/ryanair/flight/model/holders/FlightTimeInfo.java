package com.ryanair.flight.model.holders;

import java.time.LocalDateTime;

public class FlightTimeInfo {

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    public FlightTimeInfo(LocalDateTime departureTime, LocalDateTime arrivalTime) {
	this.departureTime = departureTime;
	this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
	return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
	this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
	return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
	this.arrivalTime = arrivalTime;
    }

}
