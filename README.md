# GitHub Repository Viewer API

This Spring Boot application provides a RESTful API that fetches and displays **non-forked GitHub repositories** for a given user, including information about **each branch and its latest commit SHA**.

---

## Features

- Returns a list of **public non-forked GitHub repositories** for a given username.
- For each repository:
  - Shows repository name
  - Shows owner login
  - Lists all branches with last commit SHA
- Returns a `404 Not Found` with a custom error body when the user does not exist.

---

## API Usage

### Endpoint

GET /api/github/USERNAME/repositories 

### Response Example:

```json
[
  {
    "repositoryName": "example-repo",
    "ownerLogin": "example-user",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123def456..."
      }
    ]
  }
]
```

### Error response
```json
{
  "message": "User not found",
  "status": 404
}
```
### Getting Started 

1. Clone the repo
git clone https://github.com/DawidSzoka1/GitHubRepositoriesAPi.git
cd GitHubRepositoriesAPi

2. Run the application
./mvnw spring-boot:run


### Runing tests

./mvnw test




