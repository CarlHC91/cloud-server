package org.raspberry.cloud.app.directory;

import java.util.List;

import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.raspberry.cloud.pojos.operations.directorydetails.CreateOne_IN;
import org.raspberry.cloud.pojos.operations.directorydetails.CreateOne_OUT;
import org.raspberry.cloud.pojos.operations.directorydetails.DeleteOne_IN;
import org.raspberry.cloud.pojos.operations.directorydetails.DeleteOne_OUT;
import org.raspberry.cloud.pojos.operations.directorydetails.FindAllByParent_IN;
import org.raspberry.cloud.pojos.operations.directorydetails.FindAllByParent_OUT;
import org.raspberry.cloud.service.directory.DirectoryDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryDetailsRest {

	@Autowired
	private DirectoryDetailsService directoryDetailsService;

	@PostMapping(produces = "application/json", consumes = "application/json", value = "/directoryDetails/findAllByParent")
	public ResponseEntity<FindAllByParent_OUT> findAllByParent(RequestEntity<FindAllByParent_IN> requestEntityVO) {
		FindAllByParent_IN findAllByParent_IN = requestEntityVO.getBody();

		List<DirectoryDetailsVO> directoryDetailsListVO = directoryDetailsService.findAllByParent(findAllByParent_IN.getParentDirectory());

		FindAllByParent_OUT findAllByParent_OUT = new FindAllByParent_OUT();
		findAllByParent_OUT.setDirectoryDetailsList(directoryDetailsListVO);

		return ResponseEntity.ok(findAllByParent_OUT);
	}
	
	@PostMapping(produces = "application/json", consumes = "application/json", value = "/directoryDetails/createOne")
	public ResponseEntity<CreateOne_OUT> createOne(RequestEntity<CreateOne_IN> requestEntityVO) {
		CreateOne_IN createOne_IN = requestEntityVO.getBody();

		DirectoryDetailsVO directoryDetailsVO = directoryDetailsService.createOne(createOne_IN.getDirectoryDetails());

		CreateOne_OUT createOne_OUT = new CreateOne_OUT();
		createOne_OUT.setDirectoryDetails(directoryDetailsVO);

		return ResponseEntity.ok(createOne_OUT);
	}
	
	@PostMapping(produces = "application/json", consumes = "application/json", value = "/directoryDetails/deleteOne")
	public ResponseEntity<DeleteOne_OUT> deleteOne(RequestEntity<DeleteOne_IN> requestEntityVO) {
		DeleteOne_IN deleteOne_IN = requestEntityVO.getBody();

		directoryDetailsService.deleteOne(deleteOne_IN.getDirectoryDetails());

		DeleteOne_OUT deleteOne_OUT = new DeleteOne_OUT();
		
		return ResponseEntity.ok(deleteOne_OUT);
	}
	
}
