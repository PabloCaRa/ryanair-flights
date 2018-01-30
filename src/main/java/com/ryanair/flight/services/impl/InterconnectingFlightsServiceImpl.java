package com.ryanair.flight.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.flight.model.holders.FinalRoute;
import com.ryanair.flight.model.holders.FlightInfo;
import com.ryanair.flight.model.holders.FlightTimeInfo;
import com.ryanair.flight.model.output.InterconnectedFlight;
import com.ryanair.flight.model.output.Leg;
import com.ryanair.flight.services.InterconnectingFlightsService;
import com.ryanair.flight.services.RoutesService;
import com.ryanair.flight.services.SchedulesService;

@Service
public class InterconnectingFlightsServiceImpl implements InterconnectingFlightsService {

    @Autowired
    RoutesService routesService;

    @Autowired
    SchedulesService schedulesService;

    @Override
    public List<InterconnectedFlight> getInterconnectedFlights(String departureAirport, String arrivalAirport, LocalDateTime departureDateTime,
	    LocalDateTime arrivalDateTime) {

	List<InterconnectedFlight> interconnectedFlights = new ArrayList<InterconnectedFlight>();

	// Retrieving info about routes from departure airport to arrival
	// airport
	List<FinalRoute> finalRoutes = routesService.getRoutesBetweenAirports(departureAirport, arrivalAirport);

	finalRoutes.stream().forEach(finalRoute -> {

	    FlightInfo firstFlightInfo = schedulesService.getFlightsByDepartureTimeBetweenAirports(finalRoute.getDepartureAirport(),
		    StringUtils.defaultIfBlank(finalRoute.getConnectingAirport(), finalRoute.getArrivalAirport()), departureDateTime,
		    arrivalDateTime);

	    if (firstFlightInfo != null) {
		if (StringUtils.isNotBlank(finalRoute.getConnectingAirport())) {
		    // Flight with stopover
		    List<FlightTimeInfo> firstflightTimesInfo = firstFlightInfo.getFlightTimes();

		    if (CollectionUtils.isNotEmpty(firstflightTimesInfo)) {

			FlightInfo secondFlightInfo = schedulesService.getFlightsByDepartureTimeBetweenAirports(finalRoute.getConnectingAirport(),
				finalRoute.getArrivalAirport(), departureDateTime, arrivalDateTime);
			List<FlightTimeInfo> secondFlightTimesInfo = secondFlightInfo.getFlightTimes();

			if (CollectionUtils.isNotEmpty(secondFlightTimesInfo)) {
			    firstflightTimesInfo.stream().forEach(firstFlightTimeInfo -> {
				// Filtering the flights to retrieve only the
				// ones that departs two hours later than the
				// arrival time of the first flight
				// I have assumed that the stopover should be
				// always in the same day
				secondFlightTimesInfo.stream()
					.filter(secondFlightTimeInfo -> firstFlightTimeInfo.getDepartureTime().getDayOfMonth() == secondFlightTimeInfo
						.getDepartureTime().getDayOfMonth()
						&& firstFlightTimeInfo.getArrivalTime().getDayOfMonth() == secondFlightTimeInfo.getArrivalTime()
							.getDayOfMonth()
						&& firstFlightTimeInfo.getArrivalTime().plusHours(2)
							.compareTo(secondFlightTimeInfo.getDepartureTime()) <= 0)
					.forEach(filteredSecondFlightTimeInfo -> {

					    interconnectedFlights.add(fillInterconnectedFlightWithStopOver(firstFlightInfo, firstFlightTimeInfo,
						    secondFlightInfo, filteredSecondFlightTimeInfo));
					});
			    });
			}

		    }

		} else {
		    // Non-stop flights
		    firstFlightInfo.getFlightTimes().stream().forEach(flightTime -> {
			interconnectedFlights.add(fillInterconnectedFlightNonStop(firstFlightInfo, flightTime));
		    });

		}
	    }

	});

	return interconnectedFlights;

    }

    /**
     * Fills the response bean for flights with no stops.
     * 
     * @param flightInfo
     *            the flight info about departure and arrival airports
     * @param flightTime
     *            the flight info about the departure and arrival times
     * @return the flight info with the departure airport, arrival airport and
     *         the flight times
     */
    private InterconnectedFlight fillInterconnectedFlightNonStop(FlightInfo flightInfo, FlightTimeInfo flightTime) {

	InterconnectedFlight interconnectedFlight = new InterconnectedFlight(0);

	Leg leg = new Leg(flightInfo.getDepartureAirport(), flightInfo.getArrivalAirport(), flightTime.getDepartureTime().toString(),
		flightTime.getArrivalTime().toString());
	interconnectedFlight.setLegs(Arrays.asList(leg));

	return interconnectedFlight;
    }

    /**
     * Fills the response bean for flights with stopover
     * 
     * @param firstFlightInfo
     *            the first flight info about departure and arrival airports
     * @param firstFlightTime
     *            the first flight info about the departure and arrival times
     * @param secondFlightInfo
     *            the second flight info about departure and arrival airports
     * @param secondFlightTime
     *            the second flight info about the departure and arrival times
     * @return the flight info with the departure airport, connecting airport,
     *         arrival airport and the flight times
     */
    private InterconnectedFlight fillInterconnectedFlightWithStopOver(FlightInfo firstFlightInfo, FlightTimeInfo firstFlightTime,
	    FlightInfo secondFlightInfo, FlightTimeInfo secondFlightTime) {

	InterconnectedFlight interconnectedFlight = new InterconnectedFlight(1);
	List<Leg> legs = new ArrayList<Leg>();

	Leg firstLeg = new Leg(firstFlightInfo.getDepartureAirport(), firstFlightInfo.getArrivalAirport(),
		firstFlightTime.getDepartureTime().toString(), firstFlightTime.getArrivalTime().toString());
	legs.add(firstLeg);

	Leg secondLeg = new Leg(secondFlightInfo.getDepartureAirport(), secondFlightInfo.getArrivalAirport(),
		secondFlightTime.getDepartureTime().toString(), secondFlightTime.getArrivalTime().toString());
	legs.add(secondLeg);

	interconnectedFlight.setLegs(legs);

	return interconnectedFlight;
    }

}
