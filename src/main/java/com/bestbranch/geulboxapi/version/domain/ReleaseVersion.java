package com.bestbranch.geulboxapi.version.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

@Getter
@Entity
public class ReleaseVersion {

	public static final Long RELEASE_VERSION_NO = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "release_version_no")
	private Long releaseVersionNo;

	@Column(name = "minimum_version")
	private String minimumVersion;

	@Column(name = "maximum_version")
	private String maximumVersion;

	public boolean minimumReleaseVersionIsLargerThan(String version) {
		String[] splitVersions = version.split("\\.");
		String[] splitMinimumReleaseVersions = this.minimumVersion.split("\\.");
		try {
			int majorVersion = Integer.parseInt(splitVersions[0]);
			int minorVersion = Integer.parseInt(splitVersions[1]);
			int hotfixVersion = Integer.parseInt(splitVersions[2]);
			int minimumMajorReleaseVersion = Integer.parseInt(splitMinimumReleaseVersions[0]);
			int minimumMinorReleaseVersion = Integer.parseInt(splitMinimumReleaseVersions[1]);
			int minimumHotfixReleaseVersion = Integer.parseInt(splitMinimumReleaseVersions[2]);

			if (minimumMajorReleaseVersion != majorVersion) {
				return minimumMajorReleaseVersion > majorVersion;
			} else if (minimumMinorReleaseVersion != minorVersion) {
				return minimumMinorReleaseVersion > minorVersion;
			} else if (minimumHotfixReleaseVersion != hotfixVersion) {
				return minimumHotfixReleaseVersion > hotfixVersion;
			} else {
				return false;
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Version 형식이 잘못됐습니다.");
		}
	}
}
