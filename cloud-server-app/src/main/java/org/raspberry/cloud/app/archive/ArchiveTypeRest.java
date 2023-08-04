package org.raspberry.cloud.app.archive;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveTypeVO;
import org.raspberry.cloud.service.archive.ArchiveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArchiveTypeRest {

	@Autowired
	private ArchiveTypeService archiveTypeService;

	@PostMapping("/api/archiveType/findOneById")
	@PreAuthorize("hasAuthority('/cloud/api/archiveType/findOneById')")
	public ArchiveTypeVO findOneById(@RequestParam("id_type") Long idType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setIdType(idType);

		return archiveTypeService.findOneById(userSessionVO, archiveTypeVO);
	}
	
	@PostMapping("/api/archiveType/findAll")
	@PreAuthorize("hasAuthority('/cloud/api/archiveType/findAll')")
	public ArchiveTypeVO[] findAll() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		return archiveTypeService.findAll(userSessionVO);
	}

	@PostMapping("/api/archiveType/createOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveType/createOne')")
	public ArchiveTypeVO createOne(@RequestParam("name") String name) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setName(name);
		
		return archiveTypeService.createOne(userSessionVO, archiveTypeVO);
	}
	
	@PostMapping("/api/archiveType/updateOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveType/updateOne')")
	public ArchiveTypeVO updateOne(@RequestParam("id_type") Long idType, @RequestParam("name") String name) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setIdType(idType);
		archiveTypeVO.setName(name);
		
		return archiveTypeService.updateOne(userSessionVO, archiveTypeVO);
	}

	@PostMapping("/api/archiveType/deleteOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveType/deleteOne')")
	public void deleteOne(@RequestParam("id_type") Long idType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setIdType(idType);

		archiveTypeService.deleteOne(userSessionVO, archiveTypeVO);
	}

}
