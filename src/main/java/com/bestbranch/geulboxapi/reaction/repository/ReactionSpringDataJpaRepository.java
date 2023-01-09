package com.bestbranch.geulboxapi.reaction.repository;

import com.bestbranch.geulboxapi.reaction.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionSpringDataJpaRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserNoAndGeulNo(Long userNo, Long geulNo);
}
