package com.bestbranch.geulboxapi.user.profile.geul.service.dto;

import com.bestbranch.geulboxapi.geul.search.service.dto.GeulView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ProfileGeulsView {
    private final List<GeulView> geuls;
    private final Long geulCount;
}
