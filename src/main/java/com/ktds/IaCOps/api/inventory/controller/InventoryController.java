package com.ktds.IaCOps.api.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.IaCOps.api.inventory.service.InventoryService;
import com.ktds.IaCOps.common.inventory.model.Inventory;
import com.ktds.IaCOps.common.response.ApiResponse;

@RestController
@RequestMapping("/api")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @GetMapping("/inventories")
    public ResponseEntity<ApiResponse<List<Inventory>>> getAllInventory() {
        try {
            List<Inventory> inventories = inventoryService.getAllInventory();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", inventories), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/inventories/{id}")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventory() {
         // TODO: 실제 구현 필요
        return null;
    }


}