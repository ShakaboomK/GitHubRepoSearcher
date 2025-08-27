package com.KKStands.GitHubExtends.GitHubRepoKK.UnitTests;

import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubAPIResponseDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubRepoDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.GHubSearchRequestDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.DTO.SearchResponseDTO;
import com.KKStands.GitHubExtends.GitHubRepoKK.Entity.GHubRepoEntity;
import com.KKStands.GitHubExtends.GitHubRepoKK.Repository.GHubRepoRepository;
import com.KKStands.GitHubExtends.GitHubRepoKK.Service.GHubAPIClientService;
import com.KKStands.GitHubExtends.GitHubRepoKK.Service.GHubRepoService;
import com.KKStands.GitHubExtends.GitHubRepoKK.Utils.SortTypeOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GHubRepoServiceTest {
    @Mock
    private GHubRepoRepository repository;

    @Mock
    private GHubAPIClientService clientService;
    @InjectMocks
    private GHubRepoService service;

    private GHubRepoEntity sampleEntity;
    private GHubRepoDTO sampleGhubRepoDTO;

    @BeforeEach
    void setup() {
        sampleEntity = new GHubRepoEntity();
        sampleEntity.setId(1L);
        sampleEntity.setName("Sample Repo 1");
        sampleEntity.setDescription("description");
        sampleEntity.setOwner("Owner1");
        sampleEntity.setLanguage("Java");
        sampleEntity.setStars(100);
        sampleEntity.setForks(10);
        sampleEntity.setLastUpdated(ZonedDateTime.now());

        sampleGhubRepoDTO = new GHubRepoDTO();
        sampleGhubRepoDTO.setId(1L);
        sampleGhubRepoDTO.setName("Sample Repo 2");
        sampleGhubRepoDTO.setDescription("description");
        sampleGhubRepoDTO.setLanguage("java");
        sampleGhubRepoDTO.setStars(100);
        sampleGhubRepoDTO.setForks(10);
        sampleGhubRepoDTO.setUpdatedAt(ZonedDateTime.now().toString());
        GHubRepoDTO.Owner owner = new GHubRepoDTO.Owner();
        owner.setLogin("Owner1");
        sampleGhubRepoDTO.setOwner(owner);

    }

    @Test
    void testSearchAndSaveRepositories_success(){
        GHubAPIResponseDTO responseDTO = new GHubAPIResponseDTO();
        responseDTO.setItems(List.of(sampleGhubRepoDTO));
        when(clientService.searchRepositories(any())).thenReturn(responseDTO);
        when(repository.saveAll(anyList())).thenReturn(List.of(sampleEntity));

        GHubSearchRequestDTO request = new GHubSearchRequestDTO();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort(SortTypeOption.STARS);

        SearchResponseDTO result = service.searchAndSaveRepositories(request);

        assertEquals("Repositories fetched and saved successfully", result.getMessage());
        assertFalse(result.getRepositories().isEmpty());
        verify(repository, times(1)).saveAll(anyList());
    }


}
