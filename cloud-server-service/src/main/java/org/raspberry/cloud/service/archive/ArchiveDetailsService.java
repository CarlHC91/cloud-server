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
import java.util.UUID;

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
		DirectoryDetails rootDirectory = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), 0L);
		if (rootDirectory == null) {
			File fileRoot = new File("files", UUID.randomUUID().toString());

			while (fileRoot.exists()) {
				fileRoot = new File("files", UUID.randomUUID().toString());
			}
			
			if (!fileRoot.mkdir()) {
				throw new ServiceException("File '" + fileRoot.getName() + "' cannot created");
			}
			
			rootDirectory = new DirectoryDetails();
			rootDirectory.setIdUser(userSessionVO.getIdUser());
			rootDirectory.setIdDirectory(0L);
			rootDirectory.setIdParent(null);
			rootDirectory.setFilePath(fileRoot.getAbsolutePath());
			rootDirectory.setFileName(fileRoot.getName());
			rootDirectory.setCreateDate(new Date());
			rootDirectory = directoryDetailsDao.save(rootDirectory);
		}
		
		DirectoryDetails parentDirectory = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), archiveDetailsVO.getIdParent());
		if (parentDirectory == null) {
			throw new ServiceException("Directory '" + archiveDetailsVO.getIdParent() + "' not exists");
		}
		
		File fileArchive = new File(parentDirectory.getFilePath(), archiveDetailsVO.getFileName());
		int numDirectory = 2;
		
		while (fileArchive.exists()) {
			String oldFileName = archiveDetailsVO.getFileName();
			String newFileName = oldFileName + " (" + numDirectory + ")";
			
			int i = oldFileName.lastIndexOf(".");
			if (i > 0) {
				newFileName = oldFileName.substring(0, i) + " (" + numDirectory + ")" + oldFileName.substring(i);
			}

			fileArchive = new File(parentDirectory.getFilePath(), newFileName);
			numDirectory++;
		}
		
		try {
			InputStream inputStream = multipartFile.getInputStream();
			OutputStream outputStream = new FileOutputStream(fileArchive);

			transferStreams(inputStream, outputStream);

			outputStream.close();
		} catch (IOException ex) {
			throw new ServiceException("File '" + fileArchive.getName() + "' cannot created");
		}

		Long idArchive = archiveDetailsDao.getMaxIdArchive(userSessionVO.getIdUser());
		
		ArchiveDetails archiveDetails = new ArchiveDetails();
		archiveDetails.setIdUser(userSessionVO.getIdUser());
		archiveDetails.setIdArchive(idArchive == null ? 1L : idArchive + 1);
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
			throw new ServiceException("File '" + fileArchive.getName() + "' cannot deleted");
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
			throw new ServiceException("File '" + fileArchive.getName() + "' not exists");
		}
		
		try {
			InputStream inputStream = new FileInputStream(fileArchive);
			
			return new InputStreamResource(inputStream);
		} catch (IOException ex) {
			throw new ServiceException("File '" + fileArchive.getName() + "' cannot downloaded");
		}
	}
	
	private void transferStreams(InputStream inputStream, OutputStream outputStream) throws IOException {
		int pos = 0;
		byte[] buffer = new byte[1024];

		while ((pos = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, pos);
		}
	}

}
