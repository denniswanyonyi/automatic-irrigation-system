package com.kifiya.automatic_irrigation_system.events;

import com.kifiya.automatic_irrigation_system.dao.Land;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class AllocateTimeSlotEvent extends ApplicationEvent {

    private Land land;

    public AllocateTimeSlotEvent(Object source, Land land) {
        super(source);
        this.land = land;
    }

    public Land getLandID() {
        return land;
    }
}
