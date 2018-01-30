package com.ryanair.flight.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.flight.connectors.SchedulesConnector;
import com.ryanair.flight.model.holders.FlightInfo;
import com.ryanair.flight.model.holders.FlightTimeInfo;
import com.ryanair.flight.model.input.Flight;
import com.ryanair.flight.model.input.Schedule;
import com.ryanair.flight.model.input.ScheduleDay;
import com.ryanair.flight.services.SchedulesService;
import com.ryanair.flight.utils.LocalDateTimeUtils;

@Service
public class SchedulesServiceImpl implements SchedulesService {

    @Autowired
    SchedulesConnector schedulesConnector;

    @Override
    public FlightInfo getFlightsByDepartureTimeBetweenAirports(String departureAirport, String arrivalAirport, LocalDateTime limitDepartureTime,
	    LocalDateTime limitArrivalTime) {

	List<FlightTimeInfo> flightTimesInfo = null;

	FlightInfo flightInfo = new FlightInfo(departureAirport, arrivalAirport);

	if (limitDepartureTime.getMonth().equals(limitArrivalTime.getMonth())) {
	    flightTimesInfo = getFlightsSameMonth(departureAirport, arrivalAirport, limitDepartureTime, limitArrivalTime);
	} else {
	    flightTimesInfo = getFlightsManyMonths(departureAirport, arrivalAirport, limitDepartureTime, limitArrivalTime);
	}

	flightInfo.setFlightTimes(flightTimesInfo);

	return flightInfo;

    }

    /**
     * Retrieves the flight times info when the limit departur time and the
     * limit arrival times belongs to the same month
     * 
     * @param departureAirport
     *            departure airport
     * @param arrivalAirport
     *            arrival airport
     * @param limitDepartureTime
     * @param limitArrivalTime
     * @return flight times info that fits into the required limits
     */
    private List<FlightTimeInfo> getFlightsSameMonth(String departureAirport, String arrivalAirport, LocalDateTime limitDepartureTime,
	    LocalDateTime limitArrivalTime) {

	Schedule monthSchedule = schedulesConnector.getSchedule(departureAirport, arrivalAirport, limitDepartureTime.getYear(),
		limitDepartureTime.getMonthValue());

	return doGetMonthFlights(monthSchedule, limitDepartureTime, limitArrivalTime);
    }

    /**
     * Retrieves the flight times info when there is a month or more difference
     * between the limit departure time and the limit arrival time
     * 
     * @param departureAirport
     *            departure airport
     * @param arrivalAirport
     *            arrival airport
     * @param limitDepartureTime
     *            limit departure time
     * @param limitArrivalTime
     *            limit arrival time
     * @return the flight times info that fits into the required limits
     */
    private List<FlightTimeInfo> getFlightsManyMonths(String departureAirport, String arrivalAirport, LocalDateTime limitDepartureTime,
	    LocalDateTime limitArrivalTime) {

	List<FlightTimeInfo> flightTimesInfo = new ArrayList<FlightTimeInfo>();

	int currentMonth = limitDepartureTime.getMonthValue();
	int lastMonth = limitArrivalTime.getMonthValue();

	while (currentMonth <= lastMonth) {

	    Schedule monthSchedule = schedulesConnector.getSchedule(departureAirport, arrivalAirport, limitDepartureTime.getYear(), currentMonth);

	    flightTimesInfo.addAll(doGetMonthFlights(monthSchedule, limitDepartureTime, limitArrivalTime));

	    currentMonth++;
	}

	return flightTimesInfo;
    }

    /**
     * Retrieves the flight times info looking for the valid ones.
     * 
     * @param schedule
     *            monthly times info for a route
     * @param limitDepartureTime
     *            limit departure time to compare with monthly schedule
     * @param limitArrivalTime
     *            limit arrival time to compare with monthly schedule
     * @return the flight times info for a month that fits into the required
     *         limits
     */
    private List<FlightTimeInfo> doGetMonthFlights(Schedule schedule, LocalDateTime limitDepartureTime, LocalDateTime limitArrivalTime) {

	List<FlightTimeInfo> flightTimesInfo = new ArrayList<FlightTimeInfo>();

	if (schedule != null) {
	    List<ScheduleDay> days = schedule.getDays();
	    LocalDate currentDate = limitDepartureTime.toLocalDate();

	    while (currentDate.isBefore(limitArrivalTime.toLocalDate()) || currentDate.equals(limitArrivalTime.toLocalDate())) {

		LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.MIDNIGHT);
		ScheduleDay scheduleDay = days.stream().filter(day -> currentDateTime.getDayOfMonth() == day.getDay()).findAny().orElse(null);
		List<Flight> flights = null;

		if (scheduleDay != null) {
		    flights = scheduleDay.getFlights();
		}

		if (CollectionUtils.isNotEmpty(flights)) {
		    flights.stream().forEach(flight -> {

			LocalTime flightDepartureTime = LocalDateTimeUtils.parseStringToLocalTime(flight.getDepartureTime());
			LocalTime flightArrivalTime = LocalDateTimeUtils.parseStringToLocalTime(flight.getArrivalTime());

			LocalDateTime flightDepartureDateTime = currentDateTime.withHour(flightDepartureTime.getHour())
				.withMinute(flightDepartureTime.getMinute());
			LocalDateTime flightArrivalDateTime = currentDateTime.withHour(flightArrivalTime.getHour())
				.withMinute(flightArrivalTime.getMinute());

			if (flightTimesAreBetweenLimits(flightDepartureDateTime, flightArrivalDateTime, limitDepartureTime, limitArrivalTime)) {
			    FlightTimeInfo flightTimeInfo = new FlightTimeInfo(flightDepartureDateTime, flightArrivalDateTime);
			    flightTimesInfo.add(flightTimeInfo);
			}
		    });
		}

		currentDate = currentDate.plusDays(1);
	    }
	}

	return flightTimesInfo;
    }

    /**
     * Checks if the flight times given are between the departure and arrival
     * limit times given
     * 
     * @param flightDepartureTime
     *            the flight departure time
     * @param flightArrivalTime
     *            the flight arrival time
     * @param limitDepartureTime
     *            limit departure time
     * @param limitArrivalTime
     *            limit arrival time
     * @return true if the flight times are between the limits, false otherwise
     */
    private boolean flightTimesAreBetweenLimits(LocalDateTime flightDepartureTime, LocalDateTime flightArrivalTime, LocalDateTime limitDepartureTime,
	    LocalDateTime limitArrivalTime) {

	boolean flightTimesAreBetweenLimits = flightDepartureTime.compareTo(limitDepartureTime) >= 0;
	flightTimesAreBetweenLimits = flightTimesAreBetweenLimits && flightArrivalTime.compareTo(limitArrivalTime) <= 0;

	return flightTimesAreBetweenLimits;
    }

}
