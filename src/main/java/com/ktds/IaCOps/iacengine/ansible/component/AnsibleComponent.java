package com.ktds.IaCOps.iacengine.ansible.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ktds.IaCOps.common.cli.CliComponent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AnsibleComponent {

	private String playbookName;

	private String targetHosts;

	@Value("${ansible.playbook_path}")
	private String playbookPath;

	@Autowired
	CliComponent cliComponent;

	public List<String> runPlaybook() {

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
		cmdLine.addArgument(playbookPath + playbookName);
		cmdLine.addArgument("-i");
		cmdLine.addArgument(targetHosts + ",");

		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(streamHandler);

		ExecuteWatchdog watchdog = new ExecuteWatchdog(180 * 1000);
		executor.setWatchdog(watchdog);

		ExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
				String output = outputStream.toString();
				outputLines.addAll(Arrays.asList(output.split("\\r?\\n")));
				log.debug(e.toString());
			}

			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				String output = outputStream.toString();
				outputLines.addAll(Arrays.asList(output.split("\\r?\\n")));
			}
		};

		try {
			executor.execute(cmdLine, resultHandler);
		} catch (IOException e) {
			log.debug(e.toString());
			return null;
		}

		while (watchdog.isWatching()) {
			try {
				Thread.sleep(500); // Pause for a short period before checking again
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		log.debug(outputLines.toString());
		return outputLines;
	}

	public List<String> dryRunPlaybook() {
		String runCommand = "sudo ansible-playbook --check"
				+ "-u ansible --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null' --extra-vars ansible_become_pass=new1234! -vvv  "
				+ playbookPath + playbookName + " -i " + String.join(" ", this.targetHosts) + ",";
		log.debug(runCommand);
		return cliComponent.runCommand(runCommand);
	}

	public List<String> dryDiffRunPlaybook() {
		List<String> outputLines = new ArrayList<>();

		CommandLine cmdLine = new CommandLine("sudo");
		cmdLine.addArgument("ansible-playbook");
		cmdLine.addArgument("--check");
		cmdLine.addArgument("--diff");
		cmdLine.addArgument("-u");
		cmdLine.addArgument("ansible");
		cmdLine.addArgument("--private-key=/root/.ssh/id_rsa");
		cmdLine.addArgument("--ssh-extra-args");
		cmdLine.addArgument("-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null", false);
		cmdLine.addArgument("--extra-vars");
		cmdLine.addArgument("ansible_become_pass=new1234!", false);
		cmdLine.addArgument("-vvv");
		cmdLine.addArgument(playbookPath + playbookName);
		cmdLine.addArgument("-i");
		cmdLine.addArgument(targetHosts + ",");

		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(streamHandler);

		ExecuteWatchdog watchdog = new ExecuteWatchdog(180 * 1000);
		executor.setWatchdog(watchdog);

		ExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				super.onProcessFailed(e);
				String output = outputStream.toString();
				outputLines.addAll(Arrays.asList(output.split("\\r?\\n")));
				log.debug(e.toString());
			}

			@Override
			public void onProcessComplete(int exitValue) {
				super.onProcessComplete(exitValue);
				String output = outputStream.toString();
				outputLines.addAll(Arrays.asList(output.split("\\r?\\n")));
			}
		};

		try {
			executor.execute(cmdLine, resultHandler);
		} catch (IOException e) {
			log.debug(e.toString());
			return null;
		}

		while (watchdog.isWatching()) {
			try {
				Thread.sleep(500); // Pause for a short period before checking again
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		log.debug(outputLines.toString());
		return outputLines;
	}

	// public List<String> runPlaybook(String extraVars) {
	// String extraVarsOption = " --extra-vars " + "\""+ extraVars +"\"";
	// String runCommand = "sudo ansible-playbook "+"-u ansible
	// --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no
	// -o UserKnownHostsFile=/dev/null' -vvv "+playbookPath+playbookName+" -i
	// "+String.join(" ", this.targetHosts)+extraVarsOption;
	// log.debug(runCommand);
	// return cli.runCommand(runCommand);
	// }

	// public List<String> dryRunPlaybook(String extraVars) {
	// String extraVarsOption = " --extra-vars " + "\""+ extraVars +"\"";
	// String runCommand = "sudo ansible-playbook --check"+"-u ansible
	// --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no
	// -o UserKnownHostsFile=/dev/null' -vvv "+playbookPath+playbookName+" -i
	// "+String.join(" ", this.targetHosts)+extraVarsOption;
	// log.debug(runCommand);
	// return cli.runCommand(runCommand);
	// }

	// public List<String> dryDiffRunPlaybook(String extraVars) {
	// String extraVarsOption = " --extra-vars " + "\""+ extraVars +"\"";
	// String runCommand = "sudo ansible-playbook --check --diff"+"-u ansible
	// --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no
	// -o UserKnownHostsFile=/dev/null' -vvv "+playbookPath+playbookName+" -i
	// "+String.join(" ", this.targetHosts)+extraVarsOption;
	// log.debug(runCommand);
	// return cli.runCommand(runCommand);
	// }

	public void selectPlaybook(String playbookName) {
		this.playbookName = playbookName;
	}

	public void setHost(String targetHost) {
		this.targetHosts = targetHosts;
	}

	public void setHostVars() {

	}

	// public void selectExtraVars(String extraVars) {

	// }

	public void setAnsibleConfig() {

	}

}
