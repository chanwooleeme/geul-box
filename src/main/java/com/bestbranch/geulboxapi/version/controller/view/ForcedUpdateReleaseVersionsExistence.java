package com.bestbranch.geulboxapi.version.controller.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "from")
public class ForcedUpdateReleaseVersionsExistence {
	private final Boolean isExistForcedUpdateReleaseVersions;
}
