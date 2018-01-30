package com.ryanair.flight.model.input;

import java.util.List;

public class Schedule {

    private int month;

    private List<ScheduleDay> days;

    public int getMonth() {
	return month;
    }

    public void setMonth(int month) {
	this.month = month;
    }

    public List<ScheduleDay> getDays() {
	return days;
    }

    public void setDays(List<ScheduleDay> days) {
	this.days = days;
    }

}
