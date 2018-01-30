package com.ryanair.flight.model.holders;

public class FinalRoute {

    private String departureAirport;

    private String connectingAirport;

    private String arrivalAirport;

    public FinalRoute(String departureAirport) {
	this.departureAirport = departureAirport;
    }

    public String getDepartureAirport() {
	return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
	this.departureAirport = departureAirport;
    }

    public String getConnectingAirport() {
	return connectingAirport;
    }

    public void setConnectingAirport(String connectingAirport) {
	this.connectingAirport = connectingAirport;
    }

    public String getArrivalAirport() {
	return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
	this.arrivalAirport = arrivalAirport;
    }

}
