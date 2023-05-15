package com.ktds.IaCOps;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ktds.IaCOps.common.vcs.gitlab.service.GitService;

@SpringBootTest
class GitServiceTest {

	@Autowired
	private GitService GitService;

	@Test
	void contextLoads() {
		String commitMessage = "230504 commit test";
		String filePattern = ".";
		// 돌아가고싶은 commitid
		String commitId = "2d1f5a2a210b9eb7a4c04a7dab025372f1f813c2";

		//clone
		//String clone = GitService.clone();
		//System.out.println(clone);

		//pull
		// String pull = GitService.pull();
		// System.out.println(pull);

		//add
		//String add = GitService.add(filePattern);
		///System.out.println(add);

		//commit
		// Scanner sc = new Scanner(System.in);
		// System.out.println("commit message를 입력하세요.");
		// commitMessage = sc.nextLine();
		// String commit = GitService.commit(commitMessage);
		// System.out.println(commit);

		//push
		// String push = GitService.push();
		// System.out.println(push);

		// localReset
		// String localReset = GitService.localReset(commitId);
		// System.out.println(localReset);

		//localRevert & push
		String localRevert = GitService.localRevert(commitId);
		System.out.println(localRevert);
		String push = GitService.push();
		System.out.println(push);
		


		//remoteReset
		//String remoteReset = GitService.remoteReset(commitId);
		//System.out.println(remoteReset);

		//remoteRevert

	}

}
