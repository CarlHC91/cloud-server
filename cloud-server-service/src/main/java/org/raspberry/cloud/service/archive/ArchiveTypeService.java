package org.raspberry.cloud.service.archive;

import java.util.ArrayList;
import java.util.List;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.dao.archive.ArchiveTypeDao;
import org.raspberry.cloud.exception.ServiceException;
import org.raspberry.cloud.model.archive.ArchiveType;
import org.raspberry.cloud.pojos.entities.archive.ArchiveTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchiveTypeService {

	@Autowired
	private ArchiveTypeDao archiveTypeDao;
	
	public ArchiveTypeVO findOneById(UserDetailsVO userSessionVO, ArchiveTypeVO archiveTypeVO) {
		ArchiveType archiveType = archiveTypeDao.findOneById(archiveTypeVO.getIdType());
		if (archiveType == null) {
			return null;
		}

		archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setIdType(archiveType.getIdType());
		archiveTypeVO.setName(archiveType.getName());

		return archiveTypeVO;
	}
	
	public ArchiveTypeVO[] findAll(UserDetailsVO userSessionVO) {
		List<ArchiveTypeVO> archiveTypeListVO = new ArrayList<>();

		for (ArchiveType archiveType : archiveTypeDao.findAll()) {
			ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
			archiveTypeVO.setIdType(archiveType.getIdType());
			archiveTypeVO.setName(archiveType.getName());

			archiveTypeListVO.add(archiveTypeVO);
		}

		return archiveTypeListVO.toArray(new ArchiveTypeVO[archiveTypeListVO.size()]);
	}
	
	public ArchiveTypeVO createOne(UserDetailsVO userSessionVO, ArchiveTypeVO archiveTypeVO) {
		ArchiveType archiveType = new ArchiveType();
		archiveType.setName(archiveTypeVO.getName());
		archiveType = archiveTypeDao.save(archiveType);
		
		archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setIdType(archiveType.getIdType());
		archiveTypeVO.setName(archiveType.getName());
		
		return archiveTypeVO;
	}
	
	public ArchiveTypeVO updateOne(UserDetailsVO userSessionVO, ArchiveTypeVO archiveTypeVO) {
		ArchiveType archiveType = archiveTypeDao.findOneById(archiveTypeVO.getIdType());
		if (archiveType == null) {
			throw new ServiceException("ArchiveType not exists");
		}
		
		archiveType.setName(archiveTypeVO.getName());
		archiveType = archiveTypeDao.save(archiveType);
		
		archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setIdType(archiveType.getIdType());
		archiveTypeVO.setName(archiveType.getName());

		return archiveTypeVO;
	}

	public void deleteOne(UserDetailsVO userSessionVO, ArchiveTypeVO archiveTypeVO) {
		ArchiveType archiveType = archiveTypeDao.findOneById(archiveTypeVO.getIdType());
		if (archiveType == null) {
			throw new ServiceException("ArchiveType not exists");
		}
		
		archiveTypeDao.delete(archiveType);
	}

}
