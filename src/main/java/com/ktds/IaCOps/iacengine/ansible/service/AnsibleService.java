package com.ktds.IaCOps.iacengine.ansible.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ktds.IaCOps.common.cli.CliService;

@Service
public class AnsibleService {

	private String playbookName;
	private List<String> targetHost;

	CliService cli = new CliService();

	public void runPlaybook() {
		String runCommand = "ansible-playbook"+"/ansible/playbook/"+playbookName+"-i hosts"+targetHost;
		cli.runCommand(runCommand);
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
