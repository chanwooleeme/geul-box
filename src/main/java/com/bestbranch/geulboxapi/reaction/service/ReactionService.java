package com.bestbranch.geulboxapi.reaction.service;

import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.geul.exception.GeulNotFoundException;
import com.bestbranch.geulboxapi.geul.repository.GeulSpringDataJpaRepository;
import com.bestbranch.geulboxapi.reaction.domain.Reaction;
import com.bestbranch.geulboxapi.reaction.repository.ReactionSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionSpringDataJpaRepository reactionRepository;
    private final GeulSpringDataJpaRepository geulRepository;

    @Transactional
    public void react(Long userNo, Long geulNo) {
        if (!reactionRepository.findByUserNoAndGeulNo(userNo, geulNo).isPresent()) {
            Geul geul = geulRepository.findById(geulNo).orElseThrow(GeulNotFoundException::new);
            geul.increaseReactionCount();
            Reaction reaction = Reaction.of(userNo, geul);
            reactionRepository.save(reaction);
        }
    }

    @Transactional
    public void cancelReaction(Long userNo, Long geulNo) {
        Optional<Reaction> nullableReaction = reactionRepository.findByUserNoAndGeulNo(userNo, geulNo);
        nullableReaction.ifPresent(reactionRepository::delete);
        Geul geul = geulRepository.findById(geulNo).orElseThrow(GeulNotFoundException::new);
        geul.decreaseReactionCount();
    }
}
