package com.ktds.IaCOps.common.cli;

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
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CliService {

	public List<String> runCommand(String command) {

		List<String> outputLines = new ArrayList<>();

		CommandLine commandLine = CommandLine.parse(command);

		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(streamHandler);

		ExecuteWatchdog watchdog = new ExecuteWatchdog(180 * 1000);
		executor.setWatchdog(watchdog);

		try {
			int exitValue = executor.execute(commandLine);
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
