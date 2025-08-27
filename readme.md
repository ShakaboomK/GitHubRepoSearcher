# GitHub Repository Searcher

## Overview

GitHub Repository Searcher is a `Spring Boot` REST API application that allows users to search GitHub repositories using the GitHub REST API, cache the results using `Redis`, store them in `PostgreSQL`, and retrieve stored repositories with filtering and sorting capabilities.

---

## ✨ Features

- Search GitHub repositories by query, language, and sort criteria.
- Store and update repository details in a `PostgreSQL` database.
- Retrieve stored repositories with filters (`language`, `minStars`) and sorting (`stars`, `forks`, `updated`).
- `Redis` caching to reduce GitHub API calls and handle rate limits efficiently.
- Robust error handling with custom exceptions and a global exception handler.
- Input validation with descriptive error messages.
- Unit and integration tests with `Testcontainers` for `PostgreSQL` and `Redis`.

---

## 🛠️ Technology Stack

- **Backend:** `Java 17+`, `Spring Boot 3`
- **Database:** `PostgreSQL`, `Spring Data JPA`
- **Caching:** `Redis`
- **HTTP Client:** `Spring WebClient`
- **Utilities:** `Lombok`
- **Testing:** `JUnit 5`, `Mockito`, `Testcontainers`
- **Build:** `Maven`
- **Containerization:** `Docker` (optional, for local environment)

---

## 🚀 Setup and Run

### Prerequisites

- `JDK 17+`
- `Maven 3.8+`
- `Docker` and `Docker Compose` (Recommended for local database and cache)
- A **GitHub Personal Access Token (PAT)** with `public_repo` scope.

### Steps

1.  **Clone the repository:**  Clone the [GitHubRepoSearcher repository](https://github.com/ShakaboomK/GitHubRepoSearcher.git) with the following commands
    ```bash
    git clone https://github.com/ShakaboomK/GitHubRepoSearcher.git
    cd GitHubRepoSearcher
    ```

2.  **Configure application properties:**
    Create a `application.properties` file in `src/main/resources` and add the following configuration. It's highly recommended to use environment variables for sensitive data like passwords and tokens.

    ```properties
    # PostgreSQL Configuration
    spring:
        datasource:
            url=jdbc:postgresql://localhost:5432/githubdb
            username:your_db_username
            password:your_db_password

    my:
      app:
        github:
            token:ghp_your_github_token
    ```

3.  **Start PostgreSQL and Redis servers:**
    The easiest way to run the required services locally is with Docker.

    ```bash
    # Start PostgreSQL Container
    docker run -d --name postgres-db -p 5432:5432 -e POSTGRES_DB=githubdb -e POSTGRES_USER=your_db_username -e POSTGRES_PASSWORD=your_db_password postgres

    # Start Redis Container
    docker run -d --name redis-cache -p 6379:6379 redis
    ```

4.  **Build and run the Spring Boot application:**
    ```bash
    # Build the project
    mvn clean install

    # Run the application
    mvn spring-boot:run
    ```
    The application will be running at `http://localhost:8080`.

---

##  API Endpoints

### 1. Search and Save GitHub Repositories

Searches GitHub for repositories based on the given criteria and saves them to the database.

- **Endpoint:** `POST /api/github/search`
- **Request Body:**
  ```json
  {
    "query": "spring boot",
    "language": "Java",
    "sort": "stars"
  }
### Response

**201 Created:** If new repositories were found and saved. The response body will contain the saved repository details.

---

## 2. Retrieve Stored Repositories

Retrieves repositories from the local PostgreSQL database with optional filtering and sorting.

- **Endpoint:** `GET /api/github/repositories`

- **Query Parameters (all optional):**
    - `language` (string): Filter by programming language (e.g., Java).
    - `minStars` (integer): Filter by a minimum number of stars (e.g., 100).
    - `sort` (string): Sort results by `"stars"`, `"forks"`, or `"updated"`. Defaults to `"stars"`.

- **Example URL:**  
  `/api/github/repositories?language=Java&minStars=100&sort=stars`

- **Response:**  
  **200 OK:** A JSON array of repository details matching the criteria.

---

## Caching Strategy

- To avoid hitting GitHub's API rate limits, this application uses **Redis** to cache the results of each unique search query.
- The default cache Time-To-Live (TTL) is **1 hour**.
- A unique cache key is constructed from the combination of query, language, and sort parameters.
- Cache configuration can be adjusted in the `RedisCacheConfig` class.

---

## 🛡️ Error Handling

- A global exception handler (`@ControllerAdvice`) provides consistent JSON error responses.
- Custom exceptions are used for specific error scenarios:
    - `BadRequestException` (400) for invalid input.
    - `RateLimitException` (429) when the GitHub API rate limit is exceeded.
    - `GitHubApiException` (502) for other errors from the GitHub API.
    - `NotFoundException` (404) when a resource is not found.

- **Example Error Response:**
# NOTE
- if you encounter any **TimeZoneError** while running the application, or you get **metadata not found** error
  do the following changes in your intelliJ IDE's configuration:
    - Go to Run -> Edit Configurations -> VM Options and set
      `-Duser.timezone=UTC`