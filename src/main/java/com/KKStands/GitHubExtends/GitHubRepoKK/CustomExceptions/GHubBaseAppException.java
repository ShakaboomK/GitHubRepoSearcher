package com.KKStands.GitHubExtends.GitHubRepoKK.CustomExceptions;

import lombok.experimental.StandardException;


public class GHubBaseAppException extends RuntimeException {
    private final int status;
    public GHubBaseAppException(String message, int status) {
        super(message);
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
