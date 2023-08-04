package org.raspberry.cloud.service.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.dao.archive.ArchiveDetailsDao;
import org.raspberry.cloud.dao.archive.ArchiveFormatDao;
import org.raspberry.cloud.dao.directory.DirectoryDetailsDao;
import org.raspberry.cloud.exception.ServiceException;
import org.raspberry.cloud.model.archive.ArchiveDetails;
import org.raspberry.cloud.model.archive.ArchiveFormat;
import org.raspberry.cloud.model.directory.DirectoryDetails;
import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveFormatVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveTypeVO;
import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchiveDetailsService {

	@Autowired
	private ArchiveDetailsDao archiveDetailsDao;
	@Autowired
	private ArchiveFormatDao archiveFormatDao;
	
	@Autowired
	private DirectoryDetailsDao directoryDetailsDao;
	
	public ArchiveDetailsVO findOneById(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			return null;
		}

		archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
		archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
		archiveDetailsVO.setIdType(archiveDetails.getIdType());
		archiveDetailsVO.setIdFormat(archiveDetails.getIdFormat());
		archiveDetailsVO.setFileName(archiveDetails.getFileName());
		archiveDetailsVO.setFileSize(archiveDetails.getFileSize());
		archiveDetailsVO.setCreateDate(archiveDetails.getCreateDate());
		archiveDetailsVO.setUpdateDate(archiveDetails.getUpdateDate());

		return archiveDetailsVO;
	}
	
	public ArchiveDetailsVO[] findAllByParent(UserDetailsVO userSessionVO, DirectoryDetailsVO parentDirectoryVO) {
		List<ArchiveDetailsVO> archiveDetailsListVO = new ArrayList<>();

		for (ArchiveDetails archiveDetails : archiveDetailsDao.findAllByParent(userSessionVO.getIdUser(), parentDirectoryVO.getIdDirectory())) {
			ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
			archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
			archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
			archiveDetailsVO.setFileName(archiveDetails.getFileName());
			archiveDetailsVO.setFileSize(archiveDetails.getFileSize());
			archiveDetailsVO.setCreateDate(archiveDetails.getCreateDate());
			archiveDetailsVO.setUpdateDate(archiveDetails.getUpdateDate());

			archiveDetailsListVO.add(archiveDetailsVO);
		}

		return archiveDetailsListVO.toArray(new ArchiveDetailsVO[archiveDetailsListVO.size()]);
	}
	
	public ArchiveDetailsVO[] findAllByType(UserDetailsVO userSessionVO, ArchiveTypeVO archiveTypeVO) {
		List<ArchiveDetailsVO> archiveDetailsListVO = new ArrayList<>();

		for (ArchiveDetails archiveDetails : archiveDetailsDao.findAllByType(userSessionVO.getIdUser(), archiveTypeVO.getIdType())) {
			ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
			archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
			archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
			archiveDetailsVO.setIdType(archiveDetails.getIdType());
			archiveDetailsVO.setIdFormat(archiveDetails.getIdFormat());
			archiveDetailsVO.setFileName(archiveDetails.getFileName());
			archiveDetailsVO.setFileSize(archiveDetails.getFileSize());
			archiveDetailsVO.setCreateDate(archiveDetails.getCreateDate());
			archiveDetailsVO.setUpdateDate(archiveDetails.getUpdateDate());

			archiveDetailsListVO.add(archiveDetailsVO);
		}

		return archiveDetailsListVO.toArray(new ArchiveDetailsVO[archiveDetailsListVO.size()]);
	}
	
	public ArchiveDetailsVO createOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO, MultipartFile multipartFile) {
		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(archiveDetailsVO.getIdParent());
		File fileParent = getFilePath(userSessionVO, parentDirectoryVO);
		
		ArchiveFormatVO archiveFormatVO = new ArchiveFormatVO();
		for (ArchiveFormat archiveFormat : archiveFormatDao.findAll()) {
			if (archiveDetailsVO.getFileName().matches(archiveFormat.getRegex())) {
				archiveFormatVO.setIdFormat(archiveFormat.getIdFormat());
				archiveFormatVO.setIdType(archiveFormat.getIdType());
				archiveFormatVO.setRegex(archiveFormat.getRegex());
			}
		}
			
		File fileArchive = new File(fileParent, archiveDetailsVO.getFileName());
		int numArchive = 2;
		
		while (fileArchive.exists()) {
			String oldFileName = archiveDetailsVO.getFileName();
			String newFileName = oldFileName + " (" + numArchive + ")";
			
			int i = oldFileName.lastIndexOf(".");
			if (i > 0) {
				newFileName = oldFileName.substring(0, i) + " (" + numArchive + ")" + oldFileName.substring(i);
			}

			fileArchive = new File(fileParent, newFileName);
			numArchive++;
		}
		
		try {
			InputStream inputStream = multipartFile.getInputStream();
			OutputStream outputStream = new FileOutputStream(fileArchive);
			
			byte[] buffer = new byte[8192];
			int length;
			
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			
			outputStream.close();
		} catch (IOException ex) {
			throw new ServiceException("FileArchive cannot create");
		}
		
		ArchiveDetails archiveDetails = new ArchiveDetails();
		archiveDetails.setIdParent(parentDirectoryVO.getIdDirectory());
		archiveDetails.setIdUser(userSessionVO.getIdUser());
		archiveDetails.setIdType(archiveFormatVO.getIdType());
		archiveDetails.setIdFormat(archiveFormatVO.getIdFormat());
		archiveDetails.setFileName(fileArchive.getName());
		archiveDetails.setFileSize(fileArchive.length());
		archiveDetails.setCreateDate(new Date());
		archiveDetails = archiveDetailsDao.save(archiveDetails);
		
		archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
		archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
		archiveDetailsVO.setIdType(archiveDetails.getIdType());
		archiveDetailsVO.setIdFormat(archiveDetails.getIdFormat());
		archiveDetailsVO.setFileName(archiveDetails.getFileName());
		archiveDetailsVO.setFileSize(archiveDetails.getFileSize());
		archiveDetailsVO.setCreateDate(archiveDetails.getCreateDate());
		archiveDetailsVO.setUpdateDate(archiveDetails.getUpdateDate());

		return archiveDetailsVO;
	}
	
	public ArchiveDetailsVO renameOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("ArchiveDetails not exists");
		}
		
		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(archiveDetails.getIdParent());
		File fileParent = getFilePath(userSessionVO, parentDirectoryVO);
		
		ArchiveFormatVO archiveFormatVO = new ArchiveFormatVO();
		for (ArchiveFormat archiveFormat : archiveFormatDao.findAll()) {
			if (archiveDetailsVO.getFileName().matches(archiveFormat.getRegex())) {
				archiveFormatVO.setIdFormat(archiveFormat.getIdFormat());
				archiveFormatVO.setIdType(archiveFormat.getIdType());
				archiveFormatVO.setRegex(archiveFormat.getRegex());
			}
		}
		
		File oldFileArchive = new File(fileParent, archiveDetails.getFileName());
		File newFileArchive = new File(fileParent, archiveDetailsVO.getFileName());
		int numArchive = 2;
		
		while (newFileArchive.exists()) {
			String oldFileName = archiveDetailsVO.getFileName();
			String newFileName = oldFileName + " (" + numArchive + ")";
			
			int i = oldFileName.lastIndexOf(".");
			if (i > 0) {
				newFileName = oldFileName.substring(0, i) + " (" + numArchive + ")" + oldFileName.substring(i);
			}

			newFileArchive = new File(fileParent, newFileName);
			numArchive++;
		}
		
		if (!oldFileArchive.renameTo(newFileArchive)) {
			throw new ServiceException("FileArchive cannot rename");
		}
		
		archiveDetails.setIdType(archiveFormatVO.getIdType());
		archiveDetails.setIdFormat(archiveFormatVO.getIdFormat());
		archiveDetails.setFileName(newFileArchive.getName());
		archiveDetails.setUpdateDate(new Date());
		archiveDetails = archiveDetailsDao.save(archiveDetails);
		
		archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
		archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
		archiveDetailsVO.setIdType(archiveDetails.getIdType());
		archiveDetailsVO.setIdFormat(archiveDetails.getIdFormat());
		archiveDetailsVO.setFileName(archiveDetails.getFileName());
		archiveDetailsVO.setFileSize(archiveDetails.getFileSize());
		archiveDetailsVO.setCreateDate(archiveDetails.getCreateDate());
		archiveDetailsVO.setUpdateDate(archiveDetails.getUpdateDate());

		return archiveDetailsVO;
	}

	public void deleteOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("ArchiveDetails not exists");
		}
		
		File fileArchive = getFilePath(userSessionVO, archiveDetailsVO);
		if (!fileArchive.delete()) {
			throw new ServiceException("FileArchive cannot delete");
		}
		
		archiveDetailsDao.delete(archiveDetails);
	}

	public Resource downloadOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("ArchiveDetails not exists");
		}

		File fileArchive = getFilePath(userSessionVO, archiveDetailsVO);
		if (!fileArchive.exists()) {
			throw new ServiceException("FileArchive not exists");
		}

		try {
			InputStream inputStream = new FileInputStream(fileArchive);

			return new InputStreamResource(inputStream);
		} catch (IOException ex) {
			throw new ServiceException("FileArchive cannot download");
		}
	}
	
	private File getFilePath(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		if (directoryDetailsVO.getIdDirectory() == 0L) {
			return new File("files", userSessionVO.getUsername());
		}
		
		DirectoryDetails directoryDetails = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), directoryDetailsVO.getIdDirectory());
		if (directoryDetails == null) {
			throw new ServiceException("DirectoryDetails not exists");
		}
			
		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(directoryDetails.getIdParent());
		File fileParent = getFilePath(userSessionVO, parentDirectoryVO);
		
		return new File(fileParent, directoryDetails.getFileName());
	}
	
	private File getFilePath(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("ArchiveDetails not exists");
		}
			
		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(archiveDetails.getIdParent());
		File fileParent = getFilePath(userSessionVO, parentDirectoryVO);
		
		return new File(fileParent, archiveDetails.getFileName());
	}

}
