package org.raspberry.cloud.app.directory;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.raspberry.cloud.service.directory.DirectoryDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryDetailsRest {

	@Autowired
	private DirectoryDetailsService directoryDetailsService;

	@PostMapping("/directoryDetails/findOneById")
	@PreAuthorize("hasAuthority('/cloud/directoryDetails/findOneById')")
	public DirectoryDetailsVO findOneById(@RequestParam("id_directory") Long idDirectory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		DirectoryDetailsVO directoryDetailsVO = new DirectoryDetailsVO();
		directoryDetailsVO.setIdDirectory(idDirectory);

		return directoryDetailsService.findOneById(userSessionVO, directoryDetailsVO);
	}
	
	@PostMapping("/directoryDetails/findAllByParent")
	@PreAuthorize("hasAuthority('/cloud/directoryDetails/findAllByParent')")
	public DirectoryDetailsVO[] findAllByParent(@RequestParam("id_parent") Long idParent) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		DirectoryDetailsVO parentDirectoryVO = new DirectoryDetailsVO();
		parentDirectoryVO.setIdDirectory(idParent);

		return directoryDetailsService.findAllByParent(userSessionVO, parentDirectoryVO);
	}

	@PostMapping("/directoryDetails/createOne")
	@PreAuthorize("hasAuthority('/cloud/directoryDetails/createOne')")
	public DirectoryDetailsVO createOne(@RequestParam("id_parent") Long idParent, @RequestParam("file_name") String fileName) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		DirectoryDetailsVO directoryDetailsVO = new DirectoryDetailsVO();
		directoryDetailsVO.setIdParent(idParent);
		directoryDetailsVO.setFileName(fileName);

		return directoryDetailsService.createOne(userSessionVO, directoryDetailsVO);
	}

	@PostMapping("/directoryDetails/deleteOne")
	@PreAuthorize("hasAuthority('/cloud/directoryDetails/deleteOne')")
	public void deleteOne(@RequestParam("id_directory") Long idDirectory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsVO userSessionVO = (UserDetailsVO) authentication.getPrincipal();

		DirectoryDetailsVO directoryDetailsVO = new DirectoryDetailsVO();
		directoryDetailsVO.setIdDirectory(idDirectory);

		directoryDetailsService.deleteOne(userSessionVO, directoryDetailsVO);
	}

}
