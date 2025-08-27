package com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions;

public class NotFoundException extends GHubBaseAppException{
    public NotFoundException(String message){
        super(message, 404);
    }
}
