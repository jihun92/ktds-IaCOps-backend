package com.ktds.IaCOps.api.git.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.IaCOps.common.response.ApiResponse;
import com.ktds.IaCOps.common.vcs.gitlab.component.GitComponent;

@RestController
@RequestMapping("/api/git")
public class GitController {

    @Autowired
    GitComponent gitComponent;

    @PostMapping("/add-all")
    public ResponseEntity<ApiResponse<String>> addAll() {
        try {
            String res = gitComponent.add(".");
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", res), HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/commit")
    public ResponseEntity<ApiResponse<String>> commit(
        @RequestBody Map<String, Object> commitMsg
    ) {
        try {
            String res = gitComponent.commit(commitMsg.get("commitMsg").toString());
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", res), HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/push")
    public ResponseEntity<ApiResponse<String>> push() {
        try {
            String res = gitComponent.push();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "success", res), HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
