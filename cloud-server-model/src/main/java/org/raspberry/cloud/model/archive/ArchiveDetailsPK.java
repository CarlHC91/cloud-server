package org.raspberry.cloud.model.archive;

import java.io.Serializable;

public class ArchiveDetailsPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idUser;

	private Long idArchive;

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getIdArchive() {
		return idArchive;
	}

	public void setIdArchive(Long idArchive) {
		this.idArchive = idArchive;
	}

}
