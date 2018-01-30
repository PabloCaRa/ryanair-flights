package com.ryanair.flight.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.flight.connectors.RoutesConnector;
import com.ryanair.flight.model.holders.FinalRoute;
import com.ryanair.flight.model.input.Route;
import com.ryanair.flight.services.RoutesService;

@Service
public class RoutesServiceImpl implements RoutesService {

    @Autowired
    RoutesConnector routesConnector;

    @Override
    public List<FinalRoute> getRoutesBetweenAirports(String departureAirport, String arrivalAirport) {

	List<Route> allRoutes = routesConnector.getRoutes();

	List<Route> routesFromDepartureAirport = allRoutes.stream().filter(route -> StringUtils.equals(departureAirport, route.getAirportFrom()))
		.collect(Collectors.toList());

	List<Route> routesToArrivalAirport = allRoutes.stream().filter(route -> StringUtils.equals(arrivalAirport, route.getAirportTo()))
		.collect(Collectors.toList());

	List<FinalRoute> finalRoutes = getFinalRoutesBetweenAirports(routesFromDepartureAirport, routesToArrivalAirport, departureAirport,
		arrivalAirport);

	return finalRoutes;
    }

    /**
     * Makes an union between the routes that depart from the departure airport
     * and the ones that arrive to the arrival aiport
     * 
     * @param routesFromDepartureAirport
     *            the routes that depart from the departure airport given
     * @param routesToArrivalAirport
     *            the routes that depart from the arrival airport given
     * @param departureAirport
     *            the departure airport
     * @param arrivalAirport
     *            the arrival airport
     * @return list with all routes that depart from the departure airport or
     *         arrives to the arrival airport
     */
    private List<FinalRoute> getFinalRoutesBetweenAirports(List<Route> routesFromDepartureAirport, List<Route> routesToArrivalAirport,
	    String departureAirport, String arrivalAirport) {

	List<FinalRoute> finalRoutes = new ArrayList<FinalRoute>();

	routesFromDepartureAirport.stream().forEach(route -> {
	    FinalRoute finalRoute = new FinalRoute(departureAirport);

	    if (StringUtils.equals(arrivalAirport, route.getAirportTo())) {
		finalRoute.setArrivalAirport(arrivalAirport);
	    } else {
		Route connectingRoute = routesToArrivalAirport.stream()
			.filter(arrivalRoute -> StringUtils.equals(route.getAirportTo(), arrivalRoute.getAirportFrom())).findAny().orElse(null);
		if (connectingRoute != null) {
		    finalRoute.setConnectingAirport(connectingRoute.getAirportFrom());
		    finalRoute.setArrivalAirport(connectingRoute.getAirportTo());
		}
	    }

	    if (finalRoute != null && StringUtils.isNotBlank(finalRoute.getArrivalAirport())) {
		finalRoutes.add(finalRoute);
	    }
	});

	return finalRoutes;
    }

}
