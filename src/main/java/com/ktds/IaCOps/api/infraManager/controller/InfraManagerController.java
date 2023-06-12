package com.ktds.IaCOps.api.infraManager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.IaCOps.api.infraManager.service.InfraManagerService;
import com.ktds.IaCOps.common.response.ApiResponse;

@RequestMapping("/api")
@RestController
public class InfraManagerController {

    @Autowired
    InfraManagerService infraManagerService;

    @PostMapping("/infra-manager/{ip}")
    public ResponseEntity<ApiResponse<List<String>>> run(@PathVariable String ip,
        @RequestBody Map<String, Object> yamlData) {
        String action = yamlData.get("action").toString();
        List<String> log = infraManagerService.run(ip, action);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", log), HttpStatus.OK);

    }
    


}
