package com.ktds.IaCOps.iacengine.terraform.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktds.IaCOps.common.cli.CliService;

@Component
public class TerraformComponent {
    
	// @Value("${filePath.infra_code.infra_config.path}")
	// private String infra_code_path;

	@Autowired
	CliService cli;

	public List<String> plan() {
		String runCommand = "sudo terraform -chdir=/var/lib/iacops/iacops-project/terraform/ plan -auto-approve";
		return cli.runCommand(runCommand);
	}

    public List<String> apply() {
		String runCommand = "sudo terraform -chdir=/var/lib/iacops/iacops-project/terraform/ apply -auto-approve";
		return cli.runCommand(runCommand);
	}

}
