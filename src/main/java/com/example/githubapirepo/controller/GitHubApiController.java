package com.example.githubapirepo.controller;

import com.example.githubapirepo.service.GitHubApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class GitHubApiController {

    private final GitHubApiService gitHubApiService;

    @Autowired
    public GitHubApiController(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    @GetMapping("/{username}/repositories")
    public ResponseEntity<?> getRepositories(@PathVariable String username) {
        List<Map<String, Object>> repos = gitHubApiService.getNonForkedRepositories(username);
        return ResponseEntity.ok(repos);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleNotFound(ResponseStatusException e){
        Map<String, Object> error = new HashMap<>();
        error.put("status", 404);
        error.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

