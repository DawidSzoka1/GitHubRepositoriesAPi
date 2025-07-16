package com.example.githubapirepo.service;

import com.example.githubapirepo.dto.BranchDTO;
import com.example.githubapirepo.dto.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class GitHubApiService {
    private final RestTemplate restTemplate;

    @Autowired
    public GitHubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getNonForkedRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        try {
            ResponseEntity<GitHubRepository[]> response = restTemplate.getForEntity(url, GitHubRepository[].class);
            System.out.println(Arrays.toString(response.getBody()));
            GitHubRepository[] repos = response.getBody();
            if (repos == null) return List.of();

            List<Map<String, Object>> result = new ArrayList<>();
            for (GitHubRepository repository : repos) {
                if (repository.isFork()) {
                    continue;
                }
                String branchesUrl = "https://api.github.com/repos/" + username + "/" + repository.getName() + "/branches";
                BranchDTO[] branches = restTemplate.getForObject(branchesUrl, BranchDTO[].class);

                if (branches == null) continue;
                Map<String, Object> repoInfo = getRepoInfo(repository, branches);
                result.add(repoInfo);
            }
            return result;
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private static Map<String, Object> getRepoInfo(GitHubRepository repository, BranchDTO[] branches) {
        List<Map<String, Object>> branchList = new ArrayList<>();
        for (BranchDTO branch : branches) {
            Map<String, Object> branchInfo = new HashMap<>();
            branchInfo.put("name", branch.getName());
            branchInfo.put("lastCommitSha", branch.getCommit().getSha());
            branchList.add(branchInfo);
        }
        Map<String, Object> repoInfo = new HashMap<>();
        repoInfo.put("repositoryName", repository.getName());
        repoInfo.put("ownerLogin", repository.getOwner().getLogin());
        repoInfo.put("branches", branchList);
        return repoInfo;
    }
}
