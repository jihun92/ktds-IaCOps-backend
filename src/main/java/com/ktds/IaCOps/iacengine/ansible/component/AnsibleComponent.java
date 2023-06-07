package com.ktds.IaCOps.iacengine.ansible.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ktds.IaCOps.common.cli.CliService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AnsibleComponent {

	private String playbookName;

	private List<String> targetHosts;
	
	@Value("${ansible.playbook_path}")
	private String playbookPath;

	@Autowired
	CliService cli;

	public List<String> runPlaybook() {
		String runCommand = "ansible-playbook"+" "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts+",");
		return cli.runCommand(runCommand);
	}

	public List<String> dryRunPlaybook() {
		String runCommand = "ansible-playbook --check"+" "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts+",");
		return cli.runCommand(runCommand);
	}

	public List<String> dryDiffRunPlaybook() {
		String runCommand = "ansible-playbook --check --diff"+" "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts+",");
		// return cli.runCommand(runCommand);
		return cli.runCommand("env");
	}

	public void selectPlaybook(String playbookName) {
		this.playbookName = playbookName;
	}

	public void setHost(List<String> targetHosts) {
		this.targetHosts = targetHosts;
	}

	public void setHost(String targetHost) {
		
		List<String> targetHosts = new ArrayList<>();
		targetHosts.add(targetHost);
		this.targetHosts = targetHosts;
		
	}

	public void setHostVars() {

	}

	public void selectExtraVars() {

	}

	public void setAnsibleConfig() {

	}

}
