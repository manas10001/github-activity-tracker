package com.example.github_activity_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitInfo {
    private String message;
    private String authorName;
    private String timestamp;
}
