package com.netcrackerg4.marketplace.model.domain.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

// review: why not to put it in subpackage of src/main/java/com/netcrackerg4/marketplace/exception?

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ErrorBody {
    private final String message;
    private final String description;
}
