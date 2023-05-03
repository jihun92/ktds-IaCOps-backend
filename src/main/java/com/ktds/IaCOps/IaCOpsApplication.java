package com.ktds.IaCOps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ktds.IaCOps.common.cli.CliService;
import com.ktds.IaCOps.common.yaml.server.YamlService;
import com.ktds.IaCOps.iacengine.ansible.service.AnsibleService;

@SpringBootApplication
public class IaCOpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IaCOpsApplication.class, args);




//		// yml test
//		YamlService yamlService = new YamlService();
//		String rootPath = System.getProperty("user.dir");
//		String filePath = rootPath+"/src/main/resources/";
//		String fileName = "application.properties";
//
//		Map<String, Object> yamlData = yamlService.readYaml(filePath, fileName);
////		System.out.println(yamlData);
//
//		yamlService.writeYaml(filePath, fileName,"server.port", 8080, yamlData);
//
//
//		// cli TEST
//		CliService cli = new CliService();
//		List<String> out = cli.runCommand("ls -all");
//		System.out.println(out);


	}

}
