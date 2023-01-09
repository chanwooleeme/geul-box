package com.bestbranch.geulboxapi.version.repository;

import com.bestbranch.geulboxapi.version.domain.ReleaseVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseSpringDataJpaRepository extends JpaRepository<ReleaseVersion, Long> {
}
