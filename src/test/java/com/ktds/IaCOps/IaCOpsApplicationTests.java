package com.ktds.IaCOps;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ktds.IaCOps.iacengine.ansible.component.AnsibleComponent;


@SpringBootTest
class IaCOpsApplicationTests {

	@Autowired
	private AnsibleComponent ansibleComponent;

	@Test
	void contextLoads() {

			// List<String> inven = Arrays.asList("localhost");
			// ansibleService.setHost(inven);
			// ansibleService.selectPlaybook("debugMsg.ymal");
			// List<String> rs = ansibleService.runPlaybook();

			// assertNotNull(rs);
	
			// System.out.println(rs);


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
