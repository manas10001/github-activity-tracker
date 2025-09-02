package com.example.github_activity_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.github_activity_tracker.model.RepositoryInfo;
import com.example.github_activity_tracker.service.GithubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/{username}")
    public ResponseEntity<List<RepositoryInfo>> getRepos(@PathVariable String username) {
        List<RepositoryInfo> repos = githubService.fetchUserRepositories(username);
        return ResponseEntity.ok(repos);
    }
}

