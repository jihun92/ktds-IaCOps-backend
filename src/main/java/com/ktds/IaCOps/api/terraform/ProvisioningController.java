package com.ktds.IaCOps.api.terraform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.IaCOps.common.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/terraform")
public class ProvisioningController {

    @Autowired
    ProvisioningService provisioningService;

    @GetMapping("/terraform-tfstatus")
    public ResponseEntity<ApiResponse<List<String>>> getTfStatus() {
        List<String> log = provisioningService.getTfStatus();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", log), HttpStatus.OK);
    }

    @PostMapping("/plan")
    public ResponseEntity<ApiResponse<List<String>>> plan() {
        try {
            List<String> log = provisioningService.plan();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", log), HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            log.info(e.toString());
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<List<String>>> apply() {

        try {
            List<String> log = provisioningService.apply();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", log), HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            log.info(e.toString());
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
