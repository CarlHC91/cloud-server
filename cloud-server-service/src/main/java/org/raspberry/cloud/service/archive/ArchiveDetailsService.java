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

	public ArchiveDetailsVO findOneById(ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(archiveDetailsVO.getIdArchive());
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

	public List<ArchiveDetailsVO> findAllByParent(DirectoryDetailsVO parentDirectoryVO) {
		List<ArchiveDetailsVO> archiveDetailsListVO = new ArrayList<>();

		for (ArchiveDetails archiveDetails : archiveDetailsDao.findAllByParent(parentDirectoryVO.getIdDirectory())) {
			ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
			archiveDetailsVO.setIdArchive(archiveDetails.getIdArchive());
			archiveDetailsVO.setIdParent(archiveDetails.getIdParent());
			archiveDetailsVO.setFileName(archiveDetails.getFileName());
			archiveDetailsVO.setFileSize(archiveDetails.getFileSize());
			archiveDetailsVO.setCreateDate(archiveDetails.getCreateDate());
			archiveDetailsVO.setUpdateDate(archiveDetails.getUpdateDate());

			archiveDetailsListVO.add(archiveDetailsVO);
		}

		return archiveDetailsListVO;
	}

	public ArchiveDetailsVO createOne(ArchiveDetailsVO archiveDetailsVO, MultipartFile multipartFile) {
		DirectoryDetails parentDirectory = directoryDetailsDao.findOneById(archiveDetailsVO.getIdParent());
		if (parentDirectory == null) {
			throw new ServiceException("ParentDirectory not exists");
		}

		File fileArchive = new File(parentDirectory.getFilePath(), archiveDetailsVO.getFileName());
		if (fileArchive.exists()) {
			throw new ServiceException("ArchiveDetails already exists");
		}

		try {
			InputStream inputStream = multipartFile.getInputStream();
			OutputStream outputStream = new FileOutputStream(fileArchive);

			transferStreams(inputStream, outputStream);

			outputStream.close();
		} catch (IOException ex) {
			throw new ServiceException("ArchiveDetails cannot created");
		}

		ArchiveDetails archiveDetails = new ArchiveDetails();
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

	public Boolean deleteOne(ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("ArchiveDetails not exists");
		}

		File fileArchive = new File(archiveDetails.getFilePath());
		if (!fileArchive.delete()) {
			throw new ServiceException("ArchiveDetails cannot deleted");
		}

		archiveDetailsDao.delete(archiveDetails);
		
		return true;
	}
	
	public Resource downloadOne(ArchiveDetailsVO archiveDetailsVO) {
		ArchiveDetails archiveDetails = archiveDetailsDao.findOneById(archiveDetailsVO.getIdArchive());
		if (archiveDetails == null) {
			throw new ServiceException("ArchiveDetails not exists");
		}

		File fileArchive = new File(archiveDetails.getFilePath());
		if (!fileArchive.exists()) {
			throw new ServiceException("FileArchive not exists");
		}
		
		try {
			InputStream inputStream = new FileInputStream(fileArchive);
			
			return new InputStreamResource(inputStream);
		} catch (IOException ex) {
			throw new ServiceException("Resource cannot downloaded");
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
