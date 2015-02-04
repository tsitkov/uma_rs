package org.mit.uma.rcserver.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceRegistrationResponse {

	@JsonProperty("_rev")
	String revision;
	
	@JsonProperty("_id")
	String protectionId;
	
	public String  getRevision() {
		return revision;
	}
	
	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	public String getProtectionId() {
		return protectionId;
	}

	public void setProtectionId(String protectionId) {
		this.protectionId = protectionId;
	}

	
}
