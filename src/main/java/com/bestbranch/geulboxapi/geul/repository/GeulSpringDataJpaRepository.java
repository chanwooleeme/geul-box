package com.bestbranch.geulboxapi.geul.repository;

import com.bestbranch.geulboxapi.geul.domain.Geul;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeulSpringDataJpaRepository extends JpaRepository<Geul, Long> {
    List<Geul> findByUserNo(Long userNo);
}
