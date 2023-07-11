package org.raspberry.cloud.model.directory;

import java.io.Serializable;

public class DirectoryDetailsPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idUser;

	private Long idDirectory;

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getIdDirectory() {
		return idDirectory;
	}

	public void setIdDirectory(Long idDirectory) {
		this.idDirectory = idDirectory;
	}

}
