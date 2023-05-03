package com.ktds.IaCOps.iacengine.ansible.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.common.cli.CliService;

@Service
public class AnsibleService {

	private String playbookName;
	private List<String> targetHost;
	
	@Value("${ansible.playbook_path}")
	private String playbookPath;

	CliService cli = new CliService();

	public List<String> runPlaybook() {
		String runCommand = "ansible-playbook "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHost);
		return cli.runCommand(runCommand);
	}

	public void selectPlaybook(String playbookName) {
		this.playbookName = playbookName;
	}

	public void setHost(List<String> targetHost) {
		this.targetHost = targetHost;
	}

	public void setHostVars() {

	}

	public void selectExtraVars() {

	}

	public void setAnsibleConfig() {

	}

}
