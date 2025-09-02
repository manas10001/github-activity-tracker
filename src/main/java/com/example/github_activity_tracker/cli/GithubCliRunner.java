package com.example.github_activity_tracker.cli;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.github_activity_tracker.model.RepositoryInfo;
import com.example.github_activity_tracker.service.GithubService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GithubCliRunner implements CommandLineRunner {

    private final GithubService githubService;

    @Override
    public void run(String... args) {
        if (args.length == 0) {
            return;
        }

        String username = args[0];
        System.out.println("Fetching repositories for: " + username);

        try {
            List<RepositoryInfo> repos = githubService.fetchUserRepositories(username);

            for (RepositoryInfo repo : repos) {
                System.out.println("\nRepository: " + repo.getName() + " (" + repo.getUrl() + ")");
                repo.getCommits().forEach(commit -> {
                        System.out.println("   ##Commit Details:");
                        System.out.println("   Author    - " + commit.getAuthorName());
                        System.out.println("   Timestamp - " + commit.getTimestamp());
                        System.out.println("   Message   - " + commit.getMessage() + "\n");
                    });
            }

            System.out.println("\nFound " + repos.size() + " repositories.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
