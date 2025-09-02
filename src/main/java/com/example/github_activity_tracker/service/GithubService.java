package com.example.github_activity_tracker.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.github_activity_tracker.exception.GithubApiException;
import com.example.github_activity_tracker.model.CommitInfo;
import com.example.github_activity_tracker.model.RepositoryInfo;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String githubToken;
    
    @Value("${github.baseURL}")
    private String BASE_URL;

    public List<RepositoryInfo> fetchUserRepositories(String username) {
        List<RepositoryInfo> repositories = new ArrayList<>();
        int page = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            while (true) {
                String url = BASE_URL + "/users/" + username + "/repos?per_page=100&page=" + page;
                ResponseEntity<JsonNode[]> response =
                        restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode[].class);

                JsonNode[] repoNodes = response.getBody();
                if (repoNodes == null || repoNodes.length == 0) {
                    break;
                }

                for (JsonNode repoNode : repoNodes) {
                    String repoName = repoNode.get("name").asText();
                    String repoUrl = repoNode.get("html_url").asText();

                    List<CommitInfo> commits = fetchCommits(username, repoName, entity);

                    repositories.add(new RepositoryInfo(repoName, repoUrl, commits));
                }

                page++;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GithubApiException("User not found: " + username);
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new GithubApiException("Rate limit exceeded or access forbidden");
            } else {
                throw new GithubApiException("GitHub API error: " + e.getMessage());
            }
        }

        return repositories;
    }

    private List<CommitInfo> fetchCommits(String username, String repoName, HttpEntity<String> entity) {
        String url = BASE_URL + "/repos/" + username + "/" + repoName + "/commits?per_page=20";

        try {
            ResponseEntity<JsonNode[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode[].class);

            JsonNode[] commitNodes = response.getBody();
            if (commitNodes == null) return Collections.emptyList();

            List<CommitInfo> commits = new ArrayList<>();
            for (JsonNode commitNode : commitNodes) {
                JsonNode commit = commitNode.get("commit");
                String message = commit.get("message").asText();
                String authorName = commit.get("author").get("name").asText();
                String timestamp = commit.get("author").get("date").asText();
                commits.add(new CommitInfo(message, authorName, timestamp));
            }
            return commits;
        } catch (HttpClientErrorException e) {
            return Collections.emptyList();
        }
    }

}

