package com.example.githubapirepo;


import com.example.githubapirepo.dto.GitHubRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubApiIntegrationTest {

    @LocalServerPort
    private int port;


    private final TestRestTemplate restTemplate;

    @Autowired
    public GitHubApiIntegrationTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    public void nonForkedRepository() {
        String username = "octocat";

        String url = "http://localhost:" + port + "/api/github/" + username + "/repositories";
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<?> repositories = response.getBody();
        assertThat(repositories).isNotNull();
        assertThat(repositories).isNotEmpty();

        Object first = repositories.getFirst();
        assertThat(first).isInstanceOf(Map.class);

        Map<String, Object> repository = (Map<String, Object>) first;
        assertThat(repository).isNotNull();
        assertThat(repository).isNotEmpty();
        assertThat(repository).containsKeys("repositoryName", "ownerLogin", "branches");
        List<?> branches = (List<?>) repository.get("branches");
        assertThat(branches).isNotNull();
        assertThat(branches).isNotEmpty();
    }
}
