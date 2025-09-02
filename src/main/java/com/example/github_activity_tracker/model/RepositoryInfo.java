package com.example.github_activity_tracker.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryInfo {
    private String name;
    private String url;
    private List<CommitInfo> commits;
}

