package com.task4.api.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ListIDRequest {

    @NotEmpty
    private List<Long> userIDs;

}
