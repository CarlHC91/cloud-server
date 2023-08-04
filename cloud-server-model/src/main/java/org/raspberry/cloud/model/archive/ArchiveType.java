package org.raspberry.cloud.model.archive;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ARCHIVE_TYPE")
public class ArchiveType {

	@Id
	@Column(name = "ID_TYPE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idType;

	@Column(name = "NAME")
	private String name;

	public Long getIdType() {
		return idType;
	}

	public void setIdType(Long idType) {
		this.idType = idType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
