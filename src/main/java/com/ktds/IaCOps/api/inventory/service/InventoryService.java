package com.ktds.IaCOps.api.inventory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.common.inventory.component.InventoryComponent;
import com.ktds.IaCOps.common.inventory.model.Inventory;

@Service
public class InventoryService {

    @Autowired
    InventoryComponent inventoryComponent;

    public List<Inventory> getAllInventory() {
        return inventoryComponent.getAllInventory();
    }

}
