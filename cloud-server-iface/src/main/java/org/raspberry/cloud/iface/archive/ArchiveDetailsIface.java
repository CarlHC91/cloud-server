package org.raspberry.cloud.iface.archive;

import java.util.HashMap;
import java.util.Map;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;
import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ArchiveDetailsIface {

	@Value("${iface.server.cloud.path}")
	private String path;

	public ArchiveDetailsVO[] findAllByParent(UserDetailsVO userSessionVO, DirectoryDetailsVO parentDirectoryVO) {
		String url = path + "/archiveDetails/findAllByParent?token_api={token_api}&id_parent={id_parent}";

		Map<String, String> params = new HashMap<>();
		params.put("token_api", userSessionVO.getTokenApi());
		params.put("id_parent", Long.toString(parentDirectoryVO.getIdDirectory()));

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.postForObject(url, null, ArchiveDetailsVO[].class, params);
	}

	public void deleteOne(UserDetailsVO userSessionVO, ArchiveDetailsVO archiveDetailsVO) {
		String url = path + "/archiveDetails/deleteOne?token_api={token_api}&id_archive={id_archive}";

		Map<String, String> params = new HashMap<>();
		params.put("token_api", userSessionVO.getTokenApi());
		params.put("id_archive", Long.toString(archiveDetailsVO.getIdArchive()));

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForObject(url, null, String.class, params);
	}

}
