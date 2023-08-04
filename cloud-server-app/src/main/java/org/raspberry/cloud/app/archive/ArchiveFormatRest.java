package org.raspberry.cloud.app.archive;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveFormatVO;
import org.raspberry.cloud.service.archive.ArchiveFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArchiveFormatRest {

	@Autowired
	private ArchiveFormatService archiveFormatService;

	@PostMapping("/api/archiveFormat/findOneById")
	@PreAuthorize("hasAuthority('/cloud/api/archiveFormat/findOneById')")
	public ArchiveFormatVO findOneById(@RequestParam("id_format") Long idFormat) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveFormatVO archiveFormatVO = new ArchiveFormatVO();
		archiveFormatVO.setIdFormat(idFormat);

		return archiveFormatService.findOneById(userSessionVO, archiveFormatVO);
	}
	
	@PostMapping("/api/archiveFormat/findAll")
	@PreAuthorize("hasAuthority('/cloud/api/archiveFormat/findAll')")
	public ArchiveFormatVO[] findAll() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		return archiveFormatService.findAll(userSessionVO);
	}

	@PostMapping("/api/archiveFormat/createOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveFormat/createOne')")
	public ArchiveFormatVO createOne(@RequestParam("id_type") Long idType, @RequestParam("regex") String regex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveFormatVO archiveFormatVO = new ArchiveFormatVO();
		archiveFormatVO.setIdType(idType);
		archiveFormatVO.setRegex(regex);
		
		return archiveFormatService.createOne(userSessionVO, archiveFormatVO);
	}
	
	@PostMapping("/api/archiveFormat/updateOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveFormat/updateOne')")
	public ArchiveFormatVO updateOne(@RequestParam("id_format") Long idFormat, @RequestParam("id_type") Long idType, @RequestParam("regex") String regex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveFormatVO archiveFormatVO = new ArchiveFormatVO();
		archiveFormatVO.setIdFormat(idFormat);
		archiveFormatVO.setIdType(idType);
		archiveFormatVO.setRegex(regex);
		
		return archiveFormatService.updateOne(userSessionVO, archiveFormatVO);
	}

	@PostMapping("/api/archiveFormat/deleteOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveFormat/deleteOne')")
	public void deleteOne(@RequestParam("id_format") Long idFormat) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveFormatVO archiveFormatVO = new ArchiveFormatVO();
		archiveFormatVO.setIdFormat(idFormat);

		archiveFormatService.deleteOne(userSessionVO, archiveFormatVO);
	}

}
