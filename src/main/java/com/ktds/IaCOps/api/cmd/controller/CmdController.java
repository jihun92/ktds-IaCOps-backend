package com.ktds.IaCOps.api.cmd.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.IaCOps.common.cli.CliComponent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cmd")
public class CmdController {

    @Autowired
    CliComponent cliComponent;

    // @PostMapping
    // public List<String> runCmd(@RequestBody Map<String, Object> yamlData) {
    //     return cliService.runCommand(yamlData.get("cmd").toString());

    // }

    @PostMapping
    public List<String> run() {

        List<String> outputLines = new ArrayList<>();

        CommandLine cmdLine = new CommandLine("sudo");
        cmdLine.addArgument("ansible-playbook");
        cmdLine.addArgument("-u");
        cmdLine.addArgument("ansible");
        cmdLine.addArgument("--private-key=/root/.ssh/id_rsa");
        cmdLine.addArgument("--ssh-extra-args");
        cmdLine.addArgument("-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null", false);
        cmdLine.addArgument("--extra-vars");
        cmdLine.addArgument("ansible_become_pass=new1234!", false);
        cmdLine.addArgument("-vvv");
        cmdLine.addArgument("/var/lib/iacops/ktds-IaCOps-ansible/playbook/nginx_latest_running_true.yaml");
        cmdLine.addArgument("-i");
        cmdLine.addArgument("52.79.111.182,");

        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(streamHandler);

        ExecuteWatchdog watchdog = new ExecuteWatchdog(180 * 1000);
        executor.setWatchdog(watchdog);

        try {
            int exitValue = executor.execute(cmdLine);
            // Convert output to List<String>:
            String output = outputStream.toString();
            outputLines = Arrays.asList(output.split("\\r?\\n"));

        } catch (ExecuteException e) {
            log.debug(e.toString());
            return null;
        } catch (IOException e) {
            log.debug(e.toString());
            return null;
        }

        return outputLines;

    }

}
