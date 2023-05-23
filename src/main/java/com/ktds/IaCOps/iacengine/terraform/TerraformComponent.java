package com.ktds.IaCOps.iacengine.terraform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ktds.IaCOps.common.cli.CliService;

@Component
public class TerraformComponent {
    
	@Value("${filePath.infra_code.infra_config.path}")
	private String infra_code_path;

	@Autowired
	CliService cli;

	public List<String> plan() {
		String runCommand = "echo " + infra_code_path;
		return cli.runCommand(runCommand);
	}

    public List<String> apply() {
		String runCommand = "echo " + infra_code_path;
		return cli.runCommand(runCommand);
	}

}
