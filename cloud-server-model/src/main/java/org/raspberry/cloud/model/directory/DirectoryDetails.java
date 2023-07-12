package org.raspberry.cloud.model.directory;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(DirectoryDetailsPK.class)
@Table(name = "DIRECTORY_DETAILS")
public class DirectoryDetails {

	@Id
	@Column(name = "ID_USER")
	private Long idUser;

	@Id
	@Column(name = "ID_DIRECTORY")
	private Long idDirectory;

	@Column(name = "ID_PARENT")
	private Long idParent;

	@Column(name = "FILE_PATH")
	private String filePath;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

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

	public Long getIdParent() {
		return idParent;
	}

	public void setIdParent(Long idParent) {
		this.idParent = idParent;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
