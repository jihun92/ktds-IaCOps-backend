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
		String runCommand = "sudo ansible-playbook "+"-u ansible --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null' --extra-vars ansible_become_pass=new1234! -vvv  "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts)+",";

		
		List<String> res = new ArrayList<>();

		// 명령 쉘을 생성한다
		runCommand = "echo "+"\""+runCommand+"\""+" >> cmd.sh";

		log.debug(runCommand);
		res = cli.runCommand(runCommand);
		log.debug(res.toString());
		res = cli.runCommand("sudo sh /home/centos/cmd.sh");

		return res;

		
	}

	public List<String> dryRunPlaybook() {
		String runCommand = "sudo ansible-playbook --check"+"-u ansible --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null' --extra-vars ansible_become_pass=new1234! -vvv  "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts)+",";
		log.debug(runCommand);
		return cli.runCommand(runCommand);
	}

	public List<String> dryDiffRunPlaybook() {
		String runCommand = "sudo ansible-playbook --check --diff"+"-u ansible --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null' --extra-vars ansible_become_pass=new1234! -vvv  "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts)+",";
		log.debug(runCommand);
		return cli.runCommand(runCommand);
	}

	// public List<String> runPlaybook(String extraVars) {
	// 	String extraVarsOption = " --extra-vars " + "\""+  extraVars +"\"";
	// 	String runCommand = "sudo ansible-playbook "+"-u ansible --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null' -vvv  "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts)+extraVarsOption;
	// 	log.debug(runCommand);
	// 	return cli.runCommand(runCommand);
	// }

	// public List<String> dryRunPlaybook(String extraVars) {
	// 	String extraVarsOption = " --extra-vars " + "\""+  extraVars +"\"";
	// 	String runCommand = "sudo ansible-playbook --check"+"-u ansible --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null' -vvv  "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts)+extraVarsOption;
	// 	log.debug(runCommand);
	// 	return cli.runCommand(runCommand);
	// }

	// public List<String> dryDiffRunPlaybook(String extraVars) {
	// 	String extraVarsOption = " --extra-vars " + "\""+  extraVars +"\"";
	// 	String runCommand = "sudo ansible-playbook --check --diff"+"-u ansible --private-key=/root/.ssh/id_rsa --ssh-extra-args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null' -vvv  "+playbookPath+playbookName+" -i "+String.join(" ", this.targetHosts)+extraVarsOption;
	// 	log.debug(runCommand);
	// 	return cli.runCommand(runCommand);
	// }


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

	// public void selectExtraVars(String extraVars) {
		
	// }

	public void setAnsibleConfig() {

	}

}
