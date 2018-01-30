package com.ryanair.flight.model.output;

import java.util.List;

public class InterconnectedFlight {

    private int stops;

    public InterconnectedFlight(int stops) {
	this.stops = stops;
    }

    private List<Leg> legs;

    public int getStops() {
	return stops;
    }

    public void setStops(int stops) {
	this.stops = stops;
    }

    public List<Leg> getLegs() {
	return legs;
    }

    public void setLegs(List<Leg> legs) {
	this.legs = legs;
    }
}
