package com.kifiya.automatic_irrigation_system.listeners;

import com.kifiya.automatic_irrigation_system.dao.Land;
import com.kifiya.automatic_irrigation_system.events.AllocateTimeSlotEvent;
import com.kifiya.automatic_irrigation_system.repositories.LandRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.List;

@Component
public class TimeSlotHandlerListener implements ApplicationListener<AllocateTimeSlotEvent> {

    @Autowired
    LandRepository landRepository;

    @Value("${irrigation_duration}")
    private int irrigationPeriodInHours;

    @Value("${irrigation_start_hour}")
    private int irrigationStartHour;

    @PostConstruct
    private void initialize() {

        /*
            Here we are going to ensure that start hour and the duration are actually valid time values to avoid
            unnecessary code handling incorrect time input values. If we encounter invalid input, then we will
            set a default. Below is a description of each field

                - irrigationStartHour - the hour automatic irrigation starts, applicable values are the
                range: 0 - 23, all limits inclusive. If invalid input is added, default start hour will be 6 AM

                - irrigationPeriodInHours - the number of hours to do the irrigation. Since an entire day can only
                consist of 24 hours, the applicable values are 1 - 24 hours. If an invalid input is provided, then
                the value defaults to 5 hours.
        */

        if(irrigationStartHour < 0 || irrigationStartHour > 23) {
            irrigationStartHour = 6;
        }

        if(irrigationPeriodInHours < 1 || irrigationPeriodInHours > 24) {
            irrigationPeriodInHours = 5;
        }
    }

    @Async
    @Override
    public void onApplicationEvent(AllocateTimeSlotEvent allocateTimeSlotEvent)
    {
        System.out.println("Listener invoked for assigning time slot");

        List<Land> plots = landRepository.findByStatusOrderByAreaDesc(Land.LandStatus.ACTIVE);

        // If so far the number of plots is less than the number of hours to irrigate,
        // then we configure so each land is irrigated at the top of the hour only
        if(plots.size() <= irrigationPeriodInHours){

            for(int i = 0; i < plots.size(); i++) {
                plots.get(i).setScheduledIrrigationTime(LocalTime.of(irrigationStartHour + i, 0));
            }
        }
        else {

            // Calculate time in minutes
            int irrigationPeriodInMinutes = irrigationPeriodInHours * 60;
            int irrigationIntervals = irrigationPeriodInMinutes / plots.size();

            // Let's update the scheduled time for the plots
            for (int i = 0; i < plots.size(); i++) {
                plots.get(i).setScheduledIrrigationTime(LocalTime.of(irrigationStartHour, 0).plusMinutes( (long) irrigationIntervals * i));
            }
        }

        try {
            landRepository.saveAll(plots);
        }
        catch(Exception e) {
            // Log failure to update irrigation timeslots
        }

    }

}
