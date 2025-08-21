package com.kifiya.automatic_irrigation_system.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_land")
@Entity
@ToString
public class Land {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long length;
    private long width;
    private long area;
    private String crop;
    private int amountOfWater;
    private LandStatus status;
    private IrrigationStatus irrigationStatus;
    private LocalDateTime dateAdded;
    private LocalDateTime lastIrrigated;
    private LocalTime scheduledIrrigationTime;

    public Land(long length, long width, long area, String crop, int amountOfWater, LandStatus status, LocalDateTime dateAdded, LocalDateTime lastIrrigated) {
        this.dateAdded = dateAdded;
        this.lastIrrigated = lastIrrigated;
        this.length = length;
        this.status = status;
        this.crop = crop;
        this.width = width;
        this.area = area;
        this.amountOfWater = amountOfWater;
    }

    public Land(long length, long width, String crop, int amountOfWater, LandStatus status, IrrigationStatus irrigationStatus, LocalDateTime dateAdded, LocalTime scheduledIrrigationTime) {
        this.dateAdded = dateAdded;
        this.scheduledIrrigationTime = scheduledIrrigationTime;
        this.length = length;
        this.status = status;
        this.irrigationStatus = irrigationStatus;
        this.crop = crop;
        this.width = width;
        this.amountOfWater = amountOfWater;
    }

    public enum LandStatus {
        ACTIVE,
        SUSPENDED,
    }

    public enum IrrigationStatus {
        PENDING_IRRIGATION,
        IRRIGATION_SUCCESSFUL,
        IRRIGATION_FAILED
    }
}
