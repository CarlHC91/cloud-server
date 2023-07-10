package org.raspberry.cloud.app.archive;

import java.util.List;

import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
import org.raspberry.cloud.pojos.operations.archivedetails.CreateOne_OUT;
import org.raspberry.cloud.pojos.operations.archivedetails.DeleteOne_IN;
import org.raspberry.cloud.pojos.operations.archivedetails.DeleteOne_OUT;
import org.raspberry.cloud.pojos.operations.archivedetails.FindAllByParent_IN;
import org.raspberry.cloud.pojos.operations.archivedetails.FindAllByParent_OUT;
import org.raspberry.cloud.service.archive.ArchiveDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ArchiveDetailsRest {

	@Autowired
	private ArchiveDetailsService archiveDetailsService;

	@PostMapping(produces = "application/json", consumes = "application/json", value = "/archiveDetails/findAllByParent")
	public ResponseEntity<FindAllByParent_OUT> findAllByParent(RequestEntity<FindAllByParent_IN> requestEntityVO) {
		FindAllByParent_IN findAllByParent_IN = requestEntityVO.getBody();

		List<ArchiveDetailsVO> archiveDetailsListVO = archiveDetailsService.findAllByParent(findAllByParent_IN.getParentDirectory());

		FindAllByParent_OUT findAllByParent_OUT = new FindAllByParent_OUT();
		findAllByParent_OUT.setArchiveDetailsList(archiveDetailsListVO);

		return ResponseEntity.ok(findAllByParent_OUT);
	}
	
	@PostMapping(produces = "application/json", consumes = "multipart/form-data", value = "/archiveDetails/createOne")
	public ResponseEntity<CreateOne_OUT> createOne(@RequestParam("id_parent") Long idParent, @RequestParam("file") MultipartFile multipartFile) {
		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdParent(idParent);
		archiveDetailsVO.setFileName(multipartFile.getOriginalFilename());

		archiveDetailsVO = archiveDetailsService.createOne(archiveDetailsVO, multipartFile);

		CreateOne_OUT createOne_OUT = new CreateOne_OUT();
		createOne_OUT.setArchiveDetails(archiveDetailsVO);

		return ResponseEntity.ok(createOne_OUT);
	}
	
	@PostMapping(produces = "application/json", consumes = "application/json", value = "/archiveDetails/deleteOne")
	public ResponseEntity<DeleteOne_OUT> deleteOne(RequestEntity<DeleteOne_IN> requestEntityVO) {
		DeleteOne_IN deleteOne_IN = requestEntityVO.getBody();

		archiveDetailsService.deleteOne(deleteOne_IN.getArchiveDetails());

		DeleteOne_OUT deleteOne_OUT = new DeleteOne_OUT();
		
		return ResponseEntity.ok(deleteOne_OUT);
	}

	@GetMapping(produces = "application/json", consumes = "multipart/form-data", value = "/archiveDetails/downloadOne")
	public ResponseEntity<Resource> downloadOne(@RequestParam("id_archive") Long idArchive) {
		ArchiveDetailsVO archiveDetailsVO = new ArchiveDetailsVO();
		archiveDetailsVO.setIdArchive(idArchive);

		Resource resource = archiveDetailsService.downloadOne(archiveDetailsVO);
		 
		return ResponseEntity.ok(resource);
	}

}
