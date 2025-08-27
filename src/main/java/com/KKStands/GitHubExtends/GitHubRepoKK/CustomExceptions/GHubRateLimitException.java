package com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions;

public class GHubRateLimitException extends GHubBaseAppException{
    public GHubRateLimitException(String message) {
        super(message, 429);
    }
}
