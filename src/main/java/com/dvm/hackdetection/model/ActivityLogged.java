package com.dvm.hackdetection.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityLogged {
    private String ip;
    private Long epochDate;
    private Action action;
    private String userName;

}
