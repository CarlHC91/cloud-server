package org.raspberry.cloud.service.directory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.dao.directory.DirectoryDetailsDao;
import org.raspberry.cloud.exception.ServiceException;
import org.raspberry.cloud.iface.archive.ArchiveDetailsIface;
import org.raspberry.cloud.iface.directory.DirectoryDetailsIface;
import org.raspberry.cloud.model.directory.DirectoryDetails;
import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectoryDetailsService {

	@Autowired
	private DirectoryDetailsDao directoryDetailsDao;

	@Autowired
	private ArchiveDetailsIface archiveDetailsIface;
	@Autowired
	private DirectoryDetailsIface directoryDetailsIface;

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
		DirectoryDetails parentDirectory = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), directoryDetailsVO.getIdParent());
		if (parentDirectory == null) {
			throw new ServiceException("Directory '" + directoryDetailsVO.getIdParent() + "' not exists");
		}
		
		File fileDirectory = new File(parentDirectory.getFilePath(), directoryDetailsVO.getFileName());
		int numDirectory = 2;
		
		while (fileDirectory.exists()) {
			String oldFileName = directoryDetailsVO.getFileName();
			String newFileName = oldFileName + " (" + numDirectory + ")";
			
			int i = oldFileName.lastIndexOf(".");
			if (i > 0) {
				newFileName = oldFileName.substring(0, i) + " (" + numDirectory + ")" + oldFileName.substring(i);
			}

			fileDirectory = new File(parentDirectory.getFilePath(), newFileName);
			numDirectory++;
		}
		
		if (!fileDirectory.mkdir()) {
			throw new ServiceException("File '" + fileDirectory.getName() + "' cannot created");
		}

		Long idDirectory = directoryDetailsDao.getMaxIdDirectory(userSessionVO.getIdUser());

		DirectoryDetails directoryDetails = new DirectoryDetails();
		directoryDetails.setIdUser(userSessionVO.getIdUser());
		directoryDetails.setIdDirectory(idDirectory == null ? 1L : idDirectory + 1);
		directoryDetails.setIdParent(parentDirectory.getIdDirectory());
		directoryDetails.setFilePath(fileDirectory.getAbsolutePath());
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

	public void deleteOne(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		for (ArchiveDetailsVO childArchiveVO : archiveDetailsIface.findAllByParent(userSessionVO, directoryDetailsVO)) {
			archiveDetailsIface.deleteOne(userSessionVO, childArchiveVO);
		}

		for (DirectoryDetailsVO childDirectoryVO : directoryDetailsIface.findAllByParent(userSessionVO, directoryDetailsVO)) {
			directoryDetailsIface.deleteOne(userSessionVO, childDirectoryVO);
		}

		DirectoryDetails directoryDetails = directoryDetailsDao.findOneById(userSessionVO.getIdUser(), directoryDetailsVO.getIdDirectory());
		if (directoryDetails == null) {
			throw new ServiceException("Directory '" + directoryDetailsVO.getIdDirectory() + "' not exists");
		}

		File fileDirectory = new File(directoryDetails.getFilePath());
		if (!fileDirectory.delete()) {
			throw new ServiceException("File '" + fileDirectory.getName() + "' cannot deleted");
		}

		directoryDetailsDao.delete(directoryDetails);
	}
	
}
