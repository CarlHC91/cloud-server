package org.raspberry.cloud.service.directory;

import java.io.File;
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
import org.springframework.stereotype.Service;

@Service
public class DirectoryDetailsService {

	@Autowired
	private ArchiveDetailsDao archiveDetailsDao;
	
	@Autowired
	private DirectoryDetailsDao directoryDetailsDao;
	
	public DirectoryDetailsVO findOneById(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		DirectoryDetails directoryDetails = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), directoryDetailsVO.getIdDirectory());
		if (directoryDetails == null) {
			return null;
		}

		directoryDetailsVO = new DirectoryDetailsVO();
		directoryDetailsVO.setIdDirectory(directoryDetails.getIdDirectory());
		directoryDetailsVO.setIdParent(directoryDetails.getIdParent());
		directoryDetailsVO.setFileName(directoryDetails.getFileName());
		directoryDetailsVO.setCreateDate(directoryDetails.getCreateDate());
		directoryDetailsVO.setUpdateDate(directoryDetails.getUpdateDate());

		return directoryDetailsVO;
	}

	public DirectoryDetailsVO[] findAllByParent(UserDetailsVO userSessionVO, DirectoryDetailsVO parentDirectoryVO) {
		List<DirectoryDetailsVO> directoryDetailsListVO = new ArrayList<>();

		for (DirectoryDetails directoryDetails : directoryDetailsDao.findAllByParent(userSessionVO.getIdUser(), parentDirectoryVO.getIdDirectory())) {
			DirectoryDetailsVO directoryDetailsVO = new DirectoryDetailsVO();
			directoryDetailsVO.setIdDirectory(directoryDetails.getIdDirectory());
			directoryDetailsVO.setIdParent(directoryDetails.getIdParent());
			directoryDetailsVO.setFileName(directoryDetails.getFileName());
			directoryDetailsVO.setCreateDate(directoryDetails.getCreateDate());
			directoryDetailsVO.setUpdateDate(directoryDetails.getUpdateDate());

			directoryDetailsListVO.add(directoryDetailsVO);
		}

		return directoryDetailsListVO.toArray(new DirectoryDetailsVO[directoryDetailsListVO.size()]);
	}

	public DirectoryDetailsVO createOne(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(directoryDetailsVO.getIdParent());
		File fileParent = getFilePath(userSessionVO, parentDirectoryVO);
		
		File fileDirectory = new File(fileParent, directoryDetailsVO.getFileName());
		int numDirectory = 2;
		
		while (fileDirectory.exists()) {
			String oldFileName = directoryDetailsVO.getFileName();
			String newFileName = oldFileName + " (" + numDirectory + ")";
			
			fileDirectory = new File(fileParent, newFileName);
			numDirectory++;
		}
		
		if (!fileDirectory.mkdir()) {
			throw new ServiceException("FileDirectory cannot create");
		}
		
		DirectoryDetails directoryDetails = new DirectoryDetails();
		directoryDetails.setIdParent(directoryDetailsVO.getIdParent());
		directoryDetails.setIdUser(userSessionVO.getIdUser());
		directoryDetails.setFileName(fileDirectory.getName());
		directoryDetails.setCreateDate(new Date());
		directoryDetails = directoryDetailsDao.save(directoryDetails);
		
		directoryDetailsVO = new DirectoryDetailsVO();
		directoryDetailsVO.setIdDirectory(directoryDetails.getIdDirectory());
		directoryDetailsVO.setIdParent(directoryDetails.getIdParent());
		directoryDetailsVO.setFileName(directoryDetails.getFileName());
		directoryDetailsVO.setCreateDate(directoryDetails.getCreateDate());
		directoryDetailsVO.setUpdateDate(directoryDetails.getUpdateDate());

		return directoryDetailsVO;
	}
	
	public DirectoryDetailsVO renameOne(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		DirectoryDetails directoryDetails = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), directoryDetailsVO.getIdDirectory());
		if (directoryDetails == null) {
			throw new ServiceException("DirectoryDetails not exists");
		}
		
		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(directoryDetails.getIdParent());
		File fileParent = getFilePath(userSessionVO, parentDirectoryVO);
		
		File oldFileDirectory = new File(fileParent, directoryDetails.getFileName());
		File newFileDirectory = new File(fileParent, directoryDetailsVO.getFileName());
		int numDirectory = 2;
		
		while (newFileDirectory.exists()) {
			String oldFileName = directoryDetailsVO.getFileName();
			String newFileName = oldFileName + " (" + numDirectory + ")";
			
			newFileDirectory = new File(fileParent, newFileName);
			numDirectory++;
		}
		
		if (!oldFileDirectory.renameTo(newFileDirectory)) {
			throw new ServiceException("FileDirectory cannot rename");
		}
		
		directoryDetails.setFileName(newFileDirectory.getName());
		directoryDetails.setUpdateDate(new Date());
		directoryDetails = directoryDetailsDao.save(directoryDetails);
		
		directoryDetailsVO = new DirectoryDetailsVO();
		directoryDetailsVO.setIdDirectory(directoryDetails.getIdDirectory());
		directoryDetailsVO.setIdParent(directoryDetails.getIdParent());
		directoryDetailsVO.setFileName(directoryDetails.getFileName());
		directoryDetailsVO.setCreateDate(directoryDetails.getCreateDate());
		directoryDetailsVO.setUpdateDate(directoryDetails.getUpdateDate());

		return directoryDetailsVO;
	}

	public void deleteOne(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		DirectoryDetails directoryDetails = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), directoryDetailsVO.getIdDirectory());
		if (directoryDetails == null) {
			throw new ServiceException("DirectoryDetails not exists");
		}
		
		for (DirectoryDetails childDirectory : directoryDetailsDao.findAllByParent(userSessionVO.getIdUser(), directoryDetailsVO.getIdDirectory())) {
			DirectoryDetailsVO childDirectoryVO = new DirectoryDetailsVO();
			childDirectoryVO.setIdDirectory(childDirectory.getIdDirectory());
			deleteOne(userSessionVO, childDirectoryVO);
		}
			
		for (ArchiveDetails childArchive : archiveDetailsDao.findAllByParent(userSessionVO.getIdUser(), directoryDetailsVO.getIdDirectory())) {
			ArchiveDetailsVO childArchiveVO = new ArchiveDetailsVO();
			childArchiveVO.setIdArchive(childArchive.getIdArchive());
			deleteOne(userSessionVO, childArchiveVO);
		}
		
		File fileDirectory = getFilePath(userSessionVO, directoryDetailsVO);
		if (!fileDirectory.delete()) {
			throw new ServiceException("FileDirectory cannot delete");
		}
		
		directoryDetailsDao.delete(directoryDetails);
	}
	
	private void deleteOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
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
