package com.bestbranch.geulboxapi.geul.service.dto;


import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.Optional;


@Getter
public class GeulUpdateRequest {
    private String geulContent;
    private String geulSources;
    private String geulAuthor;
    private String pageOfBook;
    private Boolean isPrivate;
    private Geul.Category geulCategory;

    public Geul.Category getGeulCategory() {
        return Optional.ofNullable(this.geulCategory).orElse(Geul.Category.BOOK);
    }

    @JsonIgnore
    public Boolean isPrivate() {
        return Optional.ofNullable(this.isPrivate).orElse(true);
    }

}
