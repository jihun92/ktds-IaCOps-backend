package com.ktds.IaCOps.api.swconfigmanagement.model;

import lombok.Data;

@Data
public class CommonProperties {
    private String version;
    private String state;
    private boolean install;
}