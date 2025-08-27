package com.KKStands.GitHubExtends.GitHubRepoKK.Controller;

import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubSearchRequestDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GetRepositoriesResponseDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.SearchResponseDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.Service.GHubRepoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GHubRepoController {

    private final GHubRepoService service;
    @GetMapping("/repositories")
    public ResponseEntity<GetRepositoriesResponseDTO> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(required = false, defaultValue = "stars") String sort
    ) {
        GetRepositoriesResponseDTO result = service.getRepositories(language, minStars, sort);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchAndSaveRepositories(@Valid @RequestBody GHubSearchRequestDTO request) {
        SearchResponseDTO response = service.searchAndSaveRepositories(request);

        HttpStatus status = (response.getRepositories() != null && !response.getRepositories().isEmpty())
                ? HttpStatus.CREATED : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

}
