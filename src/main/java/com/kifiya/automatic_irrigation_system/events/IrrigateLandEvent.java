package com.kifiya.automatic_irrigation_system.events;

import com.kifiya.automatic_irrigation_system.dao.Land;
import org.springframework.context.ApplicationEvent;

public class IrrigateLandEvent extends ApplicationEvent {

    private Land land;

    public IrrigateLandEvent(Object source, Land land) {
        super(source);
        this.land = land;
    }

    public Land getLand() {
        return land;
    }
}
