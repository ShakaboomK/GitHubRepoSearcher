package com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions;

public class BadRequestException extends GHubBaseAppException{
    public BadRequestException(String message) {
        super(message, 400);
    }
}
