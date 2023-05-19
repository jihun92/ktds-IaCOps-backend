package com.ktds.IaCOps.api.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    
    @NonNull private String id;
    private String ip;
    private String hostname;

}
