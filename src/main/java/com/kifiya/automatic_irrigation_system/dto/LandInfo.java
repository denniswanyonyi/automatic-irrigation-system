package com.kifiya.automatic_irrigation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kifiya.automatic_irrigation_system.dao.Land;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LandInfo {

    @JsonProperty("LandID")
    private Long id;

    @JsonProperty("Length")
    private long length;

    @JsonProperty("Width")
    private long width;

    @JsonProperty("Area")
    private long area;

    @JsonProperty("Crop")
    private String crop;

    @JsonProperty("Status")
    private Land.LandStatus status;

    @JsonProperty("DateAdded")
    private LocalDateTime dateAdded;

    @JsonProperty("LastIrrigated")
    private LocalDateTime lastIrrigated;

    @JsonProperty("ScheduledIrrigationTime")
    private LocalTime scheduledIrrigationTime;
}
