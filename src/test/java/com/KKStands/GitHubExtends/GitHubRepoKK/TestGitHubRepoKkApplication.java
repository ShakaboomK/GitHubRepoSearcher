package com.KKStands.GitHubExtends.GitHubRepoKK;

import org.springframework.boot.SpringApplication;

public class TestGitHubRepoKkApplication {

	public static void main(String[] args) {
		SpringApplication.from(GitHubRepoKkApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
