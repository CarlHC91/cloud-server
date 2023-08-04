package org.raspberry.cloud.service.archive;

import java.util.ArrayList;
import java.util.List;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.dao.archive.ArchiveFormatDao;
import org.raspberry.cloud.exception.ServiceException;
import org.raspberry.cloud.model.archive.ArchiveFormat;
import org.raspberry.cloud.pojos.entities.archive.ArchiveFormatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchiveFormatService {

	@Autowired
	private ArchiveFormatDao archiveFormatDao;
	
	public ArchiveFormatVO findOneById(UserDetailsVO userSessionVO, ArchiveFormatVO archiveFormatVO) {
		ArchiveFormat archiveFormat = archiveFormatDao.findOneById(archiveFormatVO.getIdFormat());
		if (archiveFormat == null) {
			return null;
		}

		archiveFormatVO = new ArchiveFormatVO();
		archiveFormatVO.setIdFormat(archiveFormat.getIdFormat());
		archiveFormatVO.setIdType(archiveFormat.getIdType());
		archiveFormatVO.setRegex(archiveFormat.getRegex());

		return archiveFormatVO;
	}
	
	public ArchiveFormatVO[] findAll(UserDetailsVO userSessionVO) {
		List<ArchiveFormatVO> archiveFormatListVO = new ArrayList<>();

		for (ArchiveFormat archiveFormat : archiveFormatDao.findAll()) {
			ArchiveFormatVO archiveFormatVO = new ArchiveFormatVO();
			archiveFormatVO.setIdFormat(archiveFormat.getIdFormat());
			archiveFormatVO.setIdType(archiveFormat.getIdType());
			archiveFormatVO.setRegex(archiveFormat.getRegex());

			archiveFormatListVO.add(archiveFormatVO);
		}

		return archiveFormatListVO.toArray(new ArchiveFormatVO[archiveFormatListVO.size()]);
	}
	
	public ArchiveFormatVO createOne(UserDetailsVO userSessionVO, ArchiveFormatVO archiveFormatVO) {
		ArchiveFormat archiveFormat = new ArchiveFormat();
		archiveFormat.setIdType(archiveFormatVO.getIdType());
		archiveFormat.setRegex(archiveFormatVO.getRegex());
		archiveFormat = archiveFormatDao.save(archiveFormat);
		
		archiveFormatVO = new ArchiveFormatVO();
		archiveFormatVO.setIdFormat(archiveFormat.getIdFormat());
		archiveFormatVO.setIdType(archiveFormat.getIdType());
		archiveFormatVO.setRegex(archiveFormat.getRegex());
		
		return archiveFormatVO;
	}
	
	public ArchiveFormatVO updateOne(UserDetailsVO userSessionVO, ArchiveFormatVO archiveFormatVO) {
		ArchiveFormat archiveFormat = archiveFormatDao.findOneById(archiveFormatVO.getIdFormat());
		if (archiveFormat == null) {
			throw new ServiceException("ArchiveFormat not exists");
		}
		
		archiveFormat.setIdType(archiveFormatVO.getIdType());
		archiveFormat.setRegex(archiveFormatVO.getRegex());
		archiveFormat = archiveFormatDao.save(archiveFormat);
		
		archiveFormatVO = new ArchiveFormatVO();
		archiveFormatVO.setIdFormat(archiveFormat.getIdFormat());
		archiveFormatVO.setIdType(archiveFormat.getIdType());
		archiveFormatVO.setRegex(archiveFormat.getRegex());

		return archiveFormatVO;
	}

	public void deleteOne(UserDetailsVO userSessionVO, ArchiveFormatVO archiveFormatVO) {
		ArchiveFormat archiveFormat = archiveFormatDao.findOneById(archiveFormatVO.getIdFormat());
		if (archiveFormat == null) {
			throw new ServiceException("ArchiveFormat not exists");
		}
		
		archiveFormatDao.delete(archiveFormat);
	}

}
