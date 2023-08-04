package org.raspberry.cloud.pojos.entities.archive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class ArchiveFormatVO {

	@JsonProperty("id_format")
	private Long idFormat;

	@JsonProperty("id_type")
	private Long idType;

	@JsonProperty("regex")
	private String regex;

	public Long getIdFormat() {
		return idFormat;
	}

	public void setIdFormat(Long idFormat) {
		this.idFormat = idFormat;
	}

	public Long getIdType() {
		return idType;
	}

	public void setIdType(Long idType) {
		this.idType = idType;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

}