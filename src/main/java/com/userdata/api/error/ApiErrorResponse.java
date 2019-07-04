package com.userdata.api.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiErrorResponse {

    @JsonProperty("status")
    private final int httpStatusCode;

    @JsonProperty("message")
    private final String errorMessage;

}
