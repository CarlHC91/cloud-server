package org.raspberry.cloud.iface.directory;

import java.util.List;

import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.raspberry.cloud.pojos.operations.directorydetails.DeleteOne_IN;
import org.raspberry.cloud.pojos.operations.directorydetails.DeleteOne_OUT;
import org.raspberry.cloud.pojos.operations.directorydetails.FindAllByParent_IN;
import org.raspberry.cloud.pojos.operations.directorydetails.FindAllByParent_OUT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DirectoryDetailsIface {

	@Value("${iface.server.cloud.path}")
	private String path;

	public List<DirectoryDetailsVO> findAllByParent(DirectoryDetailsVO parentDirectoryVO) {
		String url = path + "/directoryDetails/findAllByParent";

		FindAllByParent_IN findAllByParent_IN = new FindAllByParent_IN();
		findAllByParent_IN.setParentDirectory(parentDirectoryVO);
		
		RestTemplate restTemplate = new RestTemplate();
		FindAllByParent_OUT findAllByParent_OUT = restTemplate.postForObject(url, findAllByParent_IN, FindAllByParent_OUT.class);

		return findAllByParent_OUT.getDirectoryDetailsList();
	}

	public void deleteOne(DirectoryDetailsVO directoryDetailsVO) {
		String url = path + "/directoryDetails/deleteOne";

		DeleteOne_IN deleteOne_IN = new DeleteOne_IN();
		deleteOne_IN.setDirectoryDetails(directoryDetailsVO);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForObject(url, deleteOne_IN, DeleteOne_OUT.class);
	}

}
