package org.raspberry.cloud.app.archive;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
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

	@PostMapping("/archiveDetails/findAllByParent")
	@PreAuthorize("hasAuthority('/cloud/archiveDetails/findAllByParent')")
	public ArchiveDetailsVO[] findAllByParent(@RequestParam("id_parent") Long idParent) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(idParent);

		return archiveDetailsService.findAllByParent(userSessionVO, parentDirectoryVO);
	}

	@PostMapping("/archiveDetails/createOne")
	@PreAuthorize("hasAuthority('/cloud/archiveDetails/createOne')")
	public ArchiveDetailsVO createOne(@RequestParam("id_parent") Long idParent, @RequestParam("file_name") String fileName, @RequestParam("content") MultipartFile multipartFile) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdParent(idParent);
		archiveDetailsVO.setFileName(fileName);
		
		return archiveDetailsService.createOne(userSessionVO, archiveDetailsVO, multipartFile);
	}

	@PostMapping("/archiveDetails/deleteOne")
	@PreAuthorize("hasAuthority('/cloud/archiveDetails/deleteOne')")
	public void deleteOne(@RequestParam("id_archive") Long idArchive) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(idArchive);

		archiveDetailsService.deleteOne(userSessionVO, archiveDetailsVO);
	}

	@GetMapping("/archiveDetails/downloadOne")
	@PreAuthorize("hasAuthority('/cloud/archiveDetails/downloadOne')")
	public Resource downloadOne(@RequestParam("id_archive") Long idArchive) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(idArchive);

		return archiveDetailsService.downloadOne(userSessionVO, archiveDetailsVO);
	}

}
