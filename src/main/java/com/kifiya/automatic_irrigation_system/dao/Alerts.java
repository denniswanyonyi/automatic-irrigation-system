package com.kifiya.automatic_irrigation_system.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_alerts")
@Entity
public class Alerts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private boolean isRead;
    private LocalDateTime dateAdded;
    private LocalDateTime lastUpdated;

    public Alerts(LocalDateTime dateAdded, String description, LocalDateTime lastUpdated, String title) {
        this.dateAdded = dateAdded;
        this.description = description;
        this.lastUpdated = lastUpdated;
        this.title = title;
    }
}
