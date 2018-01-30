package com.ryanair.flight.model.params;

import org.apache.commons.lang3.StringUtils;

public class InterconnectedFlightsParams {

    private String departureAirport;

    private String arrivalAirport;

    private String departureTime;

    private String arrivalTime;

    public String getDepartureAirport() {
	return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
	this.departureAirport = departureAirport;
    }

    public InterconnectedFlightsParams withDepartureAirport(String departureAirport) {
	this.departureAirport = departureAirport;
	return this;
    }

    public String getArrivalAirport() {
	return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
	this.arrivalAirport = arrivalAirport;
    }

    public InterconnectedFlightsParams withArrivalAirport(String arrivalAirport) {
	this.arrivalAirport = arrivalAirport;
	return this;
    }

    public String getDepartureTime() {
	return departureTime;
    }

    public void setDepartureTime(String departureTime) {
	this.departureTime = departureTime;
    }

    public InterconnectedFlightsParams withDepartureTime(String departureTime) {
	this.departureTime = departureTime;
	return this;
    }

    public String getArrivalTime() {
	return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
	this.arrivalTime = arrivalTime;
    }

    public InterconnectedFlightsParams withArrivalTime(String arrivalTime) {
	this.arrivalTime = arrivalTime;
	return this;
    }

    public boolean isValid() {
	boolean isValid = StringUtils.isNotBlank(departureAirport);
	isValid = isValid && StringUtils.isNotBlank(arrivalAirport);
	isValid = isValid && StringUtils.isNotBlank(departureTime);
	isValid = isValid && StringUtils.isNotBlank(arrivalTime);
	return isValid;
    }

}
