package com.netcrackerg4.marketplace.model.domain.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ErrorListBody {
    Map<String,String> error;
}
