package org.raspberry.cloud.model.archive;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ARCHIVE_FORMAT")
public class ArchiveFormat {

	@Id
	@Column(name = "ID_FORMAT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFormat;

	@Column(name = "ID_TYPE")
	private Long idType;

	@Column(name = "REGEX")
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
