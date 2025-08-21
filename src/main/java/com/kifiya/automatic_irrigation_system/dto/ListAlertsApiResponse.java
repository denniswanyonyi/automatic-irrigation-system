package com.kifiya.automatic_irrigation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ListAlertsApiResponse extends ApiResponse {

    @JsonProperty("AlertsInfo")
    public List<AlertsInfo> alertsInfo;
}
