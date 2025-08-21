package com.kifiya.automatic_irrigation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kifiya.automatic_irrigation_system.dao.Land;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
    @Min(value = 1L, message = "Minimum land length is 1 metre")
    private long length;

    @JsonProperty("Width")
    @Min(value = 1L, message = "Minimum land width is 1 metre")
    private long width;

    @JsonProperty("Area")
    private long area;

    @JsonProperty("Crop")
    @Size(min=2, max=60, message = "Crop name must be 2 - 60 characters long")
    private String crop;

    @JsonProperty("AmountOfWater")
    @Min(value = 1, message = "Amount of water to be irrigated must be 1 litre or more")
    private int amountOfWater;

    @JsonProperty("Status")
    private Land.LandStatus status;

    @JsonProperty("DateAdded")
    private LocalDateTime dateAdded;

    @JsonProperty("LastIrrigated")
    private LocalDateTime lastIrrigated;

    @JsonProperty("ScheduledIrrigationTime")
    private LocalTime scheduledIrrigationTime;
}
