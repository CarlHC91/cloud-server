package org.raspberry.cloud.model.archive;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ARCHIVE_DETAILS")
public class ArchiveDetails {

	@Id
	@Column(name = "ID_ARCHIVE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idArchive;

	@Column(name = "ID_PARENT")
	private Long idParent;

	@Column(name = "ID_USER")
	private Long idUser;

	@Column(name = "ID_TYPE")
	private Long idType;

	@Column(name = "ID_FORMAT")
	private Long idFormat;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_SIZE")
	private Long fileSize;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	public Long getIdArchive() {
		return idArchive;
	}

	public void setIdArchive(Long idArchive) {
		this.idArchive = idArchive;
	}

	public Long getIdParent() {
		return idParent;
	}

	public void setIdParent(Long idParent) {
		this.idParent = idParent;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getIdType() {
		return idType;
	}

	public void setIdType(Long idType) {
		this.idType = idType;
	}

	public Long getIdFormat() {
		return idFormat;
	}

	public void setIdFormat(Long idFormat) {
		this.idFormat = idFormat;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
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
