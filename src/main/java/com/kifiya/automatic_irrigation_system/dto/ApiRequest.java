package com.kifiya.automatic_irrigation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiRequest {

    @JsonProperty("RequestRefID")
    private String requestRefID;

    @JsonProperty("OperationName")
    private String operationName;

    @JsonProperty("ReferenceData")
    private List<Parameter> referenceData;

}
