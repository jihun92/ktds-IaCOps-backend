package com.ktds.IaCOps.common.vcs.gitlab.service;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.Ref;
import java.util.Scanner;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import java.net.URISyntaxException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Service
public class GitService {

	String gitlabUrl = "https://gitlab.iacops.site/gitlab-instance-d4e213ae/Monitoring.git";
	String userName = "root";
	String password = "new1234!";
	String localPath = "/Users/seungmi/Desktop/Monitoring";
	String strmasterRef = "refs/remotes/origin/master";
	
	//clone
	public String clone() {
		String result = "";
		try {
			CloneCommand cloneCommand = Git.cloneRepository()
						.setURI(gitlabUrl)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password))
						.setDirectory(new File(localPath));
			Git git = cloneCommand.call();
			result  = "Succeed clone your git your repository" + git.toString();	
			return result;
		} catch (Exception e) {
			result = "Failed to clone files to the local repository." + e.getMessage();
			e.printStackTrace();
		}
		return result;
	}

	//pull
	public  String pull() {
		String result = "";
		try {
			Repository repo = Git.open(new File(localPath)).getRepository();
            Git git = new Git(repo);
			// Fetch (체크 필요)
			/*git.fetch()
			   .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password))
			   .call();*/
			// Check master branch 
			Ref masterRef = repo.findRef(strmasterRef);
			System.out.println(masterRef);
			if (masterRef == null) {
				String resultMaster = "Master branch not found in the GitLab repository!";
				return resultMaster;
				}
			//pull
            PullCommand pullCommand = git.pull()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password));

            PullResult resultPull = pullCommand.call();
			result = resultPull.getMergeResult().toString();

        	// 최신 커밋 메시지 담기
        	Iterable<RevCommit> commits = git.log().setMaxCount(1).call();
        	for (RevCommit commit : commits) {
            	result += commit.getFullMessage();
        	}			
		return "Succeed to pull files to the local repository." + result;
		} catch (Exception e) {
			result = "Failed to pull files to the local repository." + e.getMessage();
			e.printStackTrace();
		}
		return result;
	}

	// add
	public String add(String filePattern) {
		String result = "";
		try (Repository repo = Git.open(new File(localPath)).getRepository()) {
			Git git = new Git(repo);
			git.add().addFilepattern(filePattern).call();
			result = "Added " + filePattern + " to staging area";
		} catch (IOException | GitAPIException e) {
			result = "Failed to add " + filePattern + ": " + e.getMessage();
		}
		return result;
	}

	// commit
    public String commit(String commitMessage) {
		//List<RevCommit> 
		String result = "";
        try (Repository repo = Git.open(new File(localPath)).getRepository()) {
            Git git = new Git(repo);

            // Commit the changes
            CommitCommand commit = git.commit();
            commit.setMessage(commitMessage).call();

            // Return the list of committed changes
            RevCommit[] commits = new RevWalk(repo).parseCommit(repo.resolve("HEAD")).getParents();
			System.out.println("commitsssssssss" + commits);
			result = "Succeed to commit changes to the local repository." + commits.toString() + commitMessage;
            return result;
        } catch (IOException | GitAPIException e) {
            result =  "Failed to commit changes to the local repository." + e.getMessage();
            e.printStackTrace();
        }
		return result;
	}

	//push
	public String push() {
		String result = "";
        try (Repository repo = Git.open(new File(localPath)).getRepository()) {
            Git git = new Git(repo);

            // Push the changes to the remote repository
            PushCommand push = git.push()
            		   .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password));
            push.setRemote(gitlabUrl);
			Iterable<PushResult> pushResults = push.call();
			// Push 결과 출력
			for (PushResult pushResult : pushResults) {
				Collection<RemoteRefUpdate> remoteUpdates = pushResult.getRemoteUpdates();
				for (RemoteRefUpdate remoteUpdate : remoteUpdates) {
						result += remoteUpdate.getStatus() + "\n";
						}
					}
			return result;
		} catch (IOException e) {
        	result = "failed to open the local repository." + e.getMessage();
            e.printStackTrace();
        } catch (GitAPIException e) {
			result = "Failed to push changes to the remote repository. " + e.getMessage();
			e.printStackTrace();
        }
		return result;
    }

	// local reset (local repository를 이전 버전으로 돌리는 것) 
	public String localReset(String commitId) {
		String result = "";
		try(Repository repo = new FileRepositoryBuilder().setGitDir(new File(localPath + "/.git")).build()){
			Git git = new Git(repo);
			ObjectId commitObjectId = repo.resolve(commitId);

			// check commit id
			if (commitObjectId == null) {
				System.out.println("Commit ID not found.");
				return null;	
			}
			// reset
			git.reset()
					.setMode(ResetCommand.ResetType.HARD)
					.setRef(commitObjectId.getName())
					.call();
	
			result = "Reset successful to commit: " + commitId;
			return result;
		} catch (Exception e) {
			result = "Error occurred while resetting repository: " + e.getMessage();
		}
		return result;
	}

	// remote reset (remote repository를 이전버전으로 돌리는 것)
	public String remoteReset(String commitId) {
		String result = "";
		try (Repository repo = Git.open(new File(localPath)).getRepository()) {
			Git git = new Git(repo);
			git.remoteAdd().setName("origin").setUri(new URIish(gitlabUrl)).call();
			//git.fetch().setRemote("origin").call();
			RevertCommand revert = git.revert().include(repo.resolve(commitId));
			revert.call();
	
			result =  "Remote repository reverted to commit " + commitId;
			return result;
		} catch (IOException | URISyntaxException | GitAPIException e) {
			result =  "An error occurred during remote repository reset: " + e.getMessage();
		}
		return result;
	}	
	
	


	public static void main(String[] args) {
		GitService repo = new GitService();
		String commitMessage;
		String filePattern = ".";
		// 돌아가고싶은 commitid
		String commitId = "2d1f5a2a210b9eb7a4c04a7dab025372f1f813c2";

		//clone
		//String clone = repo.clone();
		//System.out.println(clone);

		//pull
		//String pull = repo.pull();
		//System.out.println(pull);
		
		//add
		// String add = repo.add(filePattern);
		// System.out.println(add);
		
		//commit
		// Scanner sc = new Scanner(System.in);
		// System.out.println("commit message를 입력하세요.");
		// commitMessage = sc.nextLine();
		// String commit = repo.commit(commitMessage);
		// System.out.println(commit);
		
		//push
		// String push = repo.push();
		// System.out.println(push);

		// local reset
		//String localReset = repo.localReset(commitId);
		//System.out.println(localReset);

		// remote reset
		String remoteReset = repo.remoteReset(commitId);
		System.out.println(remoteReset);


		








	}

}

