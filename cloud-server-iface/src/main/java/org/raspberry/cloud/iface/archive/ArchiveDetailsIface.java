package org.raspberry.cloud.iface.archive;

import java.util.List;

import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.raspberry.cloud.pojos.operations.archivedetails.DeleteOne_IN;
import org.raspberry.cloud.pojos.operations.archivedetails.DeleteOne_OUT;
import org.raspberry.cloud.pojos.operations.archivedetails.FindAllByParent_IN;
import org.raspberry.cloud.pojos.operations.archivedetails.FindAllByParent_OUT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ArchiveDetailsIface {

	@Value("${iface.server.cloud.path}")
	private String path;

	public List<ArchiveDetailsVO> findAllByParent(DirectoryDetailsVO parentDirectoryVO) {
		String url = path + "/archiveDetails/findAllByParent";

		FindAllByParent_IN findAllByParent_IN = new FindAllByParent_IN();
		findAllByParent_IN.setParentDirectory(parentDirectoryVO);
		
		RestTemplate restTemplate = new RestTemplate();
		FindAllByParent_OUT findAllByParent_OUT = restTemplate.postForObject(url, findAllByParent_IN, FindAllByParent_OUT.class);

		return findAllByParent_OUT.getArchiveDetailsList();
	}

	public void deleteOne(ArchiveDetailsVO archiveDetailsVO) {
		String url = path + "/archiveDetails/deleteOne";

		DeleteOne_IN deleteOne_IN = new DeleteOne_IN();
		deleteOne_IN.setArchiveDetails(archiveDetailsVO);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForObject(url, deleteOne_IN, DeleteOne_OUT.class);
	}

}
