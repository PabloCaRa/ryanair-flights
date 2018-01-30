package com.ryanair.flight.model.holders;

import java.util.List;

public class FlightInfo {

    private String departureAirport;

    private String arrivalAirport;

    private List<FlightTimeInfo> flightTimes;

    public FlightInfo(String departureAirport, String arrivalAirport) {
	this.departureAirport = departureAirport;
	this.arrivalAirport = arrivalAirport;
    }

    public String getDepartureAirport() {
	return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
	this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
	return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
	this.arrivalAirport = arrivalAirport;
    }

    public List<FlightTimeInfo> getFlightTimes() {
	return flightTimes;
    }

    public void setFlightTimes(List<FlightTimeInfo> flightTimes) {
	this.flightTimes = flightTimes;
    }

}
