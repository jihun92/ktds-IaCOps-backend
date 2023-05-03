package com.ktds.IaCOps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import com.ktds.IaCOps.iacengine.ansible.service.AnsibleService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IaCOpsApplicationTests {

	@Autowired
	private AnsibleService ansibleService;

	@Test
	void contextLoads() {

			List<String> inven = Arrays.asList("localhost");
			ansibleService.setHost(inven);
			ansibleService.selectPlaybook("debugMsg.ymal");
			List<String> rs = ansibleService.runPlaybook();
				
			assertEquals(rs, 1);
	
			System.out.println(rs);

	}

}
