package com.KKStands.GitHubExtends.GitHubRepoKK.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient createWebClientBean(){
        return  WebClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }
}
