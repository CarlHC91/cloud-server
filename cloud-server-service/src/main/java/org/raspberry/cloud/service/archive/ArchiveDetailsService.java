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
import org.raspberry.cloud.dao.directory.DirectoryDetailsDao;
import org.raspberry.cloud.exception.ServiceException;
import org.raspberry.cloud.model.archive.ArchiveDetails;
import org.raspberry.cloud.model.directory.DirectoryDetails;
import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
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
	private DirectoryDetailsDao directoryDetailsDao;

	public ArchiveDetailsVO findOneById(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			return null;
		}

		archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
		archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
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

	public ArchiveDetailsVO createOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO, MultipartFile multipartFile) {
		generateRoot(userSessionVO);
		
		DirectoryDetails parentDirectory = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdParent());
		if (parentDirectory == null) {
			throw new ServiceException("Directory '" + archiveDetailsVO.getIdParent() + "' not exists");
		}

		File fileArchive = new File(parentDirectory.getFilePath(), archiveDetailsVO.getFileName());

		try {
			InputStream inputStream = multipartFile.getInputStream();
			OutputStream outputStream = new FileOutputStream(fileArchive);

			transferStreams(inputStream, outputStream);

			outputStream.close();
		} catch (IOException ex) {
			throw new ServiceException("Archive '" + fileArchive.getName() + "' cannot created");
		}

		ArchiveDetails archiveDetails = generatePK(userSessionVO);
		archiveDetails.setIdParent(parentDirectory.getIdDirectory());
		archiveDetails.setFilePath(fileArchive.getAbsolutePath());
		archiveDetails.setFileName(fileArchive.getName());
		archiveDetails.setFileSize(fileArchive.length());
		archiveDetails.setCreateDate(new Date());
		archiveDetails = archiveDetailsDao.save(archiveDetails);

		archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
		archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
		archiveDetailsVO.setFileName(archiveDetails.getFileName());
		archiveDetailsVO.setFileSize(archiveDetails.getFileSize());
		archiveDetailsVO.setCreateDate(archiveDetails.getCreateDate());
		archiveDetailsVO.setUpdateDate(archiveDetails.getUpdateDate());

		return archiveDetailsVO;
	}

	public void deleteOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("Archive '" + archiveDetailsVO.getIdArchive() + "' not exists");
		}

		File fileArchive = new File(archiveDetails.getFilePath());
		if (!fileArchive.delete()) {
			throw new ServiceException("Archive '" + fileArchive.getName() + "' cannot deleted");
		}

		archiveDetailsDao.delete(archiveDetails);
	}
	
	public Resource downloadOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("Archive '" + archiveDetailsVO.getIdArchive() + "' not exists");
		}

		File fileArchive = new File(archiveDetails.getFilePath());
		if (!fileArchive.exists()) {
			throw new ServiceException("Archive '" + fileArchive.getName() + "' not exists");
		}
		
		try {
			InputStream inputStream = new FileInputStream(fileArchive);
			
			return new InputStreamResource(inputStream);
		} catch (IOException ex) {
			throw new ServiceException("Archive '" + fileArchive.getName() + "' cannot downloaded");
		}
	}
	
	private void generateRoot(UserDetailsVO userSessionVO) {
		File fileRoot = new File("files");
		if (!fileRoot.exists()) {
			throw new ServiceException("Directory '" + fileRoot.getName() + "' not exists");
		}
		
		DirectoryDetails parentDirectory = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), 0L);
		if (parentDirectory == null) {
			File fileParent = new File(fileRoot, userSessionVO.getUsername());
			if (!fileParent.mkdir()) {
				throw new ServiceException("Directory '" + fileParent.getName() + "' cannot created");
			}

			parentDirectory = new DirectoryDetails();
			parentDirectory.setIdUser(userSessionVO.getIdUser());
			parentDirectory.setIdDirectory(0L);
			parentDirectory.setFilePath(fileParent.getAbsolutePath());
			parentDirectory.setFileName(fileParent.getName());
			parentDirectory.setCreateDate(new Date());
			parentDirectory = directoryDetailsDao.save(parentDirectory);
		}
	}
	
	private ArchiveDetails generatePK(UserDetailsVO userSessionVO) {
		ArchiveDetails archiveDetails = new ArchiveDetails();
		archiveDetails.setIdUser(userSessionVO.getIdUser());
		archiveDetails.setIdArchive(0L);
		
		for (ArchiveDetails childArchive : archiveDetailsDao.findAll(userSessionVO.getIdUser())) {
			if (childArchive.getIdArchive() > archiveDetails.getIdArchive()) {
				archiveDetails.setIdArchive(childArchive.getIdArchive());
			}
		}
		
		archiveDetails.setIdArchive(archiveDetails.getIdArchive() + 1);

		return archiveDetails;
	}
	
	private void transferStreams(InputStream inputStream, OutputStream outputStream) throws IOException {
		int pos = 0;
		byte[] buffer = new byte[1024];

		while ((pos = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, pos);
		}
	}

}
