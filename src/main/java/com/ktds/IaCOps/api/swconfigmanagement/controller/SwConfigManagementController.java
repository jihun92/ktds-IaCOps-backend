package com.ktds.IaCOps.api.swconfigmanagement.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.IaCOps.api.inventory.controller.InventoryController;
import com.ktds.IaCOps.api.swconfigmanagement.service.SwConfigManagementService;
import com.ktds.IaCOps.common.response.ApiResponse;

@RestController
@RequestMapping("/api")
public class SwConfigManagementController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    SwConfigManagementService swConfigManagementService;

    
    @GetMapping("/sw-config-management/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHostSwConfig(
        @PathVariable String id
    ){

        try {
            Map<String, Object> hostSwConfig = swConfigManagementService.getHostSwConfig(id);
            ResponseEntity<ApiResponse<Map<String, Object>>> response = new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", hostSwConfig), HttpStatus.OK);
            return response;
        } catch (Exception e) {
            logger.info("Error Get All inventories");
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/sw-config-management/{id}")
    public ResponseEntity<ApiResponse<Void>> setHostSwConfig(
        @PathVariable String id,
        @RequestBody Map<String, Object> yamlData
    ){

        try {
            swConfigManagementService.setHostSwConfig(id, yamlData);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", null), HttpStatus.OK);
            
        } catch (Exception e) {
            logger.info("Error Get All inventories");
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/sw-config-management/{id}/dryrun")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> dryrun(
        @PathVariable String id
    ){

        try {
            Map<String, List<String>> result = swConfigManagementService.dryRun(id);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", result), HttpStatus.OK);
            
        } catch (Exception e) {
            logger.info("Error Get All inventories");
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    
}
