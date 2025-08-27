package com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions;

public class GHubApiException extends GHubBaseAppException{
    public GHubApiException(String message){
        super(message,502);
    }
}
