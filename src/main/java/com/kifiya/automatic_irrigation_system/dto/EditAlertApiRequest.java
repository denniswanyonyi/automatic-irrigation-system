package com.kifiya.automatic_irrigation_system.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class EditAlertApiRequest extends ApiRequest{

    @JsonProperty("AlertInfo")
    private AlertsInfo alertsInfo;
}
