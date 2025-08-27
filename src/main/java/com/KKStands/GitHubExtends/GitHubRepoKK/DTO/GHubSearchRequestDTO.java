package com.KKStands.GitHubExtends.GitHubRepoKK.DTO;

import com.KKStands.GitHubExtends.GitHubRepoKK.Utils.SortTypeOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GHubSearchRequestDTO {
    //used to represent client's search request body
    private String query;
    private String language;
    private SortTypeOption sort;
}
