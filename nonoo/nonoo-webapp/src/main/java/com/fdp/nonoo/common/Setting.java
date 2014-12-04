package com.fdp.nonoo.common;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class Setting {

	private String siteName;
	private String siteUrl;
	private String cookiePath;
	private String cookieDomain;

	private Integer safeKeyExpiryTime;

	@NotEmpty
	@Length(max = 200)
	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@NotEmpty
	@Length(max = 200)
	public String getSiteUrl() {
		return this.siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = StringUtils.removeEnd(siteUrl, "/");
	}

	@NotEmpty
	@Length(max = 200)
	public String getCookiePath() {
		return this.cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		if ((cookiePath != null) && (!cookiePath.endsWith("/"))) {
			cookiePath = cookiePath + "/";
		}
		this.cookiePath = cookiePath;
	}

	@Length(max = 200)
	public String getCookieDomain() {
		return this.cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	@NotNull
	@Min(0L)
	public Integer getSafeKeyExpiryTime() {
		return this.safeKeyExpiryTime;
	}

	public void setSafeKeyExpiryTime(Integer safeKeyExpiryTime) {
		this.safeKeyExpiryTime = safeKeyExpiryTime;
	}

}
