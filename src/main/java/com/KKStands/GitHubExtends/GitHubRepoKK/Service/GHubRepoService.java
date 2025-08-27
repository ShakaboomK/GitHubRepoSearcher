package com.KKStands.GitHubExtends.GitHubRepoKK.Service;

import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.*;
import com.KKStands.GitHubExtends.GitHubRepoKK.Entity.GHubRepoEntity;
import com.KKStands.GitHubExtends.GitHubRepoKK.Repository.GHubRepoRepository;
import com.KKStands.GitHubExtends.GitHubRepoKK.Utils.SortTypeOption;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GHubRepoService {
    private final GHubRepoRepository  repository;

    private final GHubAPIClientService clientService;

    public SearchResponseDTO searchAndSaveRepositories(GHubSearchRequestDTO requestDTO){
        GHubAPIResponseDTO response = clientService.searchRepositories(requestDTO);
        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            return new SearchResponseDTO("NO Repositories are found", List.of());
        }
        List<GHubRepoEntity> entities = response.getItems().stream()
                .map(this::mapDTOtoEntity)
                .collect(Collectors.toList());
        repository.saveAll(entities);
        List<RepositoryResponseDTO>  responseDTOS =  entities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return new SearchResponseDTO("Repositories fetched and Saved 200",responseDTOS );
    }

    public GetRepositoriesResponseDTO getRepositories(String language, Integer minStars, String sortBy) {

        SortTypeOption sortOption;
        try {
            sortOption = (sortBy == null || sortBy.isBlank()) ? SortTypeOption.STARS : SortTypeOption.valueOf(sortBy.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Invalid sort value fallback
            sortOption = SortTypeOption.STARS;
        }

        String sortField;
        switch (sortOption) {
            case FORKS:   sortField = "forks"; break;
            case UPDATED: sortField = "lastUpdated"; break;
            case STARS:
            default:      sortField = "stars"; break;
        }

        Sort sort = Sort.by(Sort.Direction.DESC, sortField);

        Specification<GHubRepoEntity> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (language != null && !language.isBlank()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(criteriaBuilder.lower(root.get("language")), language.toLowerCase()));
            }
            if (minStars != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("stars"), minStars));
            }
            return predicate;
        };

        List<GHubRepoEntity> entities = repository.findAll(spec, sort);

        List<RepositoryResponseDTO> dtos = entities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new GetRepositoriesResponseDTO(dtos);
    }
    private GHubRepoEntity mapDTOtoEntity(GHubRepoDTO dto) {
        GHubRepoEntity entity = new GHubRepoEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOwner(dto.getOwner().getLogin());
        entity.setLanguage(dto.getLanguage());
        entity.setStars(dto.getStars());
        entity.setForks(dto.getForks());
        // Parse ISO date string into ZonedDateTime
        entity.setLastUpdated(ZonedDateTime.parse(dto.getUpdatedAt()));
        return entity;
    }

    private RepositoryResponseDTO mapEntityToDto(GHubRepoEntity entity) {
        RepositoryResponseDTO dto = new RepositoryResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOwner(entity.getOwner());
        dto.setLanguage(entity.getLanguage());
        dto.setStars(entity.getStars());
        dto.setForks(entity.getForks());
        dto.setLastUpdated(entity.getLastUpdated());
        return dto;
    }

}
