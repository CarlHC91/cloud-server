package org.raspberry.cloud.app.archive;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveTypeVO;
import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.raspberry.cloud.service.archive.ArchiveDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ArchiveDetailsRest {

	@Autowired
	private ArchiveDetailsService archiveDetailsService;

	@PostMapping("/api/archiveDetails/findOneById")
	@PreAuthorize("hasAuthority('/cloud/api/archiveDetails/findOneById')")
	public ArchiveDetailsVO findOneById(@RequestParam("id_archive") Long idArchive) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(idArchive);

		return archiveDetailsService.findOneById(userSessionVO, archiveDetailsVO);
	}
	
	@PostMapping("/api/archiveDetails/findAllByParent")
	@PreAuthorize("hasAuthority('/cloud/api/archiveDetails/findAllByParent')")
	public ArchiveDetailsVO[] findAllByParent(@RequestParam("id_parent") Long idParent) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(idParent);

		return archiveDetailsService.findAllByParent(userSessionVO, parentDirectoryVO);
	}
	
	@PostMapping("/api/archiveDetails/findAllByType")
	@PreAuthorize("hasAuthority('/cloud/api/archiveDetails/findAllByType')")
	public ArchiveDetailsVO[] findAllByType(@RequestParam("id_type") Long idType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveTypeVO archiveTypeVO = new ArchiveTypeVO();
		archiveTypeVO.setIdType(idType);

		return archiveDetailsService.findAllByType(userSessionVO, archiveTypeVO);
	}

	@PostMapping("/api/archiveDetails/createOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveDetails/createOne')")
	public ArchiveDetailsVO createOne(@RequestParam("id_parent") Long idParent, @RequestParam("file") MultipartFile multipartFile) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdParent(idParent);
		archiveDetailsVO.setFileName(multipartFile.getOriginalFilename());
		
		return archiveDetailsService.createOne(userSessionVO, archiveDetailsVO, multipartFile);
	}
	
	@PostMapping("/api/archiveDetails/renameOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveDetails/renameOne')")
	public ArchiveDetailsVO renameOne(@RequestParam("id_archive") Long idArchive, @RequestParam("file_name") String fileName) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(idArchive);
		archiveDetailsVO.setFileName(fileName);
		
		return archiveDetailsService.renameOne(userSessionVO, archiveDetailsVO);
	}

	@PostMapping("/api/archiveDetails/deleteOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveDetails/deleteOne')")
	public void deleteOne(@RequestParam("id_archive") Long idArchive) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(idArchive);

		archiveDetailsService.deleteOne(userSessionVO, archiveDetailsVO);
	}

	@GetMapping("/api/archiveDetails/downloadOne")
	@PreAuthorize("hasAuthority('/cloud/api/archiveDetails/downloadOne')")
	public Resource downloadOne(@RequestParam("id_archive") Long idArchive) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(idArchive);

		return archiveDetailsService.downloadOne(userSessionVO, archiveDetailsVO);
	}

}
