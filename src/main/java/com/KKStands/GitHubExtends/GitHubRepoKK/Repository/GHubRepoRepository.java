package com.KKStands.GitHubExtends.GitHubRepoKK.Repository;

import com.KKStands.GitHubExtends.GitHubRepoKK.Entity.GHubRepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GHubRepoRepository extends JpaRepository<GHubRepoEntity, Long>,JpaSpecificationExecutor<GHubRepoEntity> {

}
