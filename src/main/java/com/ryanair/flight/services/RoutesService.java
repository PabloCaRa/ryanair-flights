package com.ryanair.flight.services;

import java.util.List;

import com.ryanair.flight.model.holders.FinalRoute;

public interface RoutesService {

    public List<FinalRoute> getRoutesBetweenAirports(String departureAirport, String arrivalAirport);

}
