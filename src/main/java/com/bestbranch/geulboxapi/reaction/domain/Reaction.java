package com.bestbranch.geulboxapi.reaction.domain;

import com.bestbranch.geulboxapi.geul.domain.Geul;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reaction")
@Getter
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_no")
    private Long reactionNo;

    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "geul_no")
    private Long geulNo;

    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geul_no", insertable = false, updatable = false)
    private Geul geul;*/

    protected Reaction() {
    }

    public static Reaction of(Long userNo, Geul geul) {
        Reaction reaction = new Reaction();
        reaction.userNo = userNo;
        reaction.geulNo = geul.getGeulNo();
        return reaction;
    }
}
