package org.raspberry.cloud.service.directory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public DirectoryDetailsVO findOneById(DirectoryDetailsVO directoryDetailsVO) {
		DirectoryDetails directoryDetails = directoryDetailsDao.findOneById(directoryDetailsVO.getIdDirectory());
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

	public List<DirectoryDetailsVO> findAllByParent(DirectoryDetailsVO parentDirectoryVO) {
		List<DirectoryDetailsVO> directoryDetailsListVO = new ArrayList<>();

		for (DirectoryDetails directoryDetails : directoryDetailsDao.findAllByParent(parentDirectoryVO.getIdDirectory())) {
			DirectoryDetailsVO directoryDetailsVO = new DirectoryDetailsVO();
			directoryDetailsVO.setIdDirectory(directoryDetails.getIdDirectory());
			directoryDetailsVO.setIdParent(directoryDetails.getIdParent());
			directoryDetailsVO.setFileName(directoryDetails.getFileName());
			directoryDetailsVO.setCreateDate(directoryDetails.getCreateDate());
			directoryDetailsVO.setUpdateDate(directoryDetails.getUpdateDate());

			directoryDetailsListVO.add(directoryDetailsVO);
		}

		return directoryDetailsListVO;
	}

	public DirectoryDetailsVO createOne(DirectoryDetailsVO directoryDetailsVO) {
		DirectoryDetails parentDirectory = directoryDetailsDao.findOneById(directoryDetailsVO.getIdParent());
		if (parentDirectory == null) {
			throw new ServiceException("ParentDirectory not exists");
		}

		File fileDirectory = new File(parentDirectory.getFilePath(), directoryDetailsVO.getFileName());
		if (fileDirectory.exists()) {
			throw new ServiceException("DirectoryDetails already exists");
		}
		
		if (!fileDirectory.mkdir()) {
			throw new ServiceException("DirectoryDetails cannot created");
		}

		DirectoryDetails directoryDetails = new DirectoryDetails();
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

	public Boolean deleteOne(DirectoryDetailsVO directoryDetailsVO) {
		for (ArchiveDetailsVO childArchiveVO : archiveDetailsIface.findAllByParent(directoryDetailsVO)) {
			archiveDetailsIface.deleteOne(childArchiveVO);
		}

		for (DirectoryDetailsVO childDirectoryVO : directoryDetailsIface.findAllByParent(directoryDetailsVO)) {
			directoryDetailsIface.deleteOne(childDirectoryVO);
		}

		DirectoryDetails directoryDetails = directoryDetailsDao.findOneById(directoryDetailsVO.getIdDirectory());
		if (directoryDetails == null) {
			throw new ServiceException("DirectoryDetails not exists");
		}

		File fileDirectory = new File(directoryDetails.getFilePath());
		if (!fileDirectory.delete()) {
			throw new ServiceException("DirectoryDetails cannot deleted");
		}

		directoryDetailsDao.delete(directoryDetails);
		
		return true;
	}

}
