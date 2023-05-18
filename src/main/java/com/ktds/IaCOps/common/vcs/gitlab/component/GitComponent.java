package com.ktds.IaCOps.common.vcs.gitlab.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class GitComponent {
	@Value("${vcs.gitlab.url}")
	private String gitlabUrl;
	@Value("${vcs.gitlab.username}")
	private String userName;
	@Value("${vcs.gitlab.password}")
	private String password;
	@Value("${vcs.gitlab.localpath}")
	private String localPath;
	@Value("${vcs.gitlab.strmasterref}")
	private String strmasterRef;

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

	// localReset
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

			result = "Local Repository Reset successful to commit: " + commitId;
			return result;
		} catch (Exception e) {
			result = "Error occurred while resetting repository: " + e.getMessage();
		}
		return result;
	}

    // localRevert
    public String localRevert(String commitId) {
		String result = "";
		try(Repository repo = new FileRepositoryBuilder().setGitDir(new File(localPath + "/.git")).build()){
			Git git = new Git(repo);
			ObjectId commitObjectId = repo.resolve(commitId);
			// check commit id
			if (commitObjectId == null) {
				System.out.println("Commit ID not found.");
				return null;	
			}
            git.revert().include(commitObjectId).call();
            result = "Local Repository Revert successful to commit:" + commitId;
			return result;
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            result = "Error occurred while reverting local repository." + e.getMessage();
        }
		return result;
	}

    // remoteReset
    public String remoteReset(String commitId) {
		String result = "";
		try (Git git = Git.cloneRepository().setURI(gitlabUrl).call()) {
			// Reset to the specified commit
			git.reset().setRef(commitId).call();
			// Push the reset to the remote repository
			git.push().setRemote(gitlabUrl).call();
			result = "Remote repository reset to commit " + commitId;
			return result;
		} catch (GitAPIException e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;
    }
	// remoteRevert
	public String remoteRevert(Ref commitId) {
		Git git = null;
        try  {
			git = Git.open(new File(gitlabUrl));
			git.revert().include(commitId).call();
			git.push().setRemote(gitlabUrl).setPushAll().call();
            return "Remote repository has been revert.";
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            return "Error occurred while revertting remote repository." + e.getMessage();
        }
    }

}
