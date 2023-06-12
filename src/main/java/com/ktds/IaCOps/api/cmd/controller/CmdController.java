package com.ktds.IaCOps.api.cmd.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.IaCOps.common.cli.CliService;

@RestController
@RequestMapping("/api/cmd")
public class CmdController {


    @Autowired
    CliService cliService;

    @PostMapping
    public List<String> runCmd(@RequestBody Map<String, Object> yamlData){
        return cliService.runCommand(yamlData.get("cmd").toString());

    }
    
}
