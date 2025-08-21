package com.kifiya.automatic_irrigation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlertsInfo {

    @JsonProperty("AlertID")
    private Long id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("IsRead")
    private boolean isRead;

    @JsonProperty("DateAdded")
    private LocalDateTime dateAdded;

    @JsonProperty("LastUpdated")
    private LocalDateTime lastUpdated;

    public AlertsInfo(LocalDateTime dateAdded, String description, LocalDateTime lastUpdated, String title) {
        this.dateAdded = dateAdded;
        this.description = description;
        this.lastUpdated = lastUpdated;
        this.title = title;
    }
}
