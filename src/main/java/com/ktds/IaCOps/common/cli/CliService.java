package com.ktds.IaCOps.common.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CliService {

	public List<String> runCommand(String command) {

		List<String> output = new ArrayList<>();

		try {

			Process process = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;

			while ((line = br.readLine()) != null) {
				output.add(line);
			}

		} catch (IOException e) {
			return null;
		}

		return output;
	}

	public List<String> runCommand(String[] command) throws IOException {

		List<String> output = new ArrayList<>();

		Process process = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = null;

		while ((line = br.readLine()) != null) {
			output.add(line);
		}

		return output;
	}

}
