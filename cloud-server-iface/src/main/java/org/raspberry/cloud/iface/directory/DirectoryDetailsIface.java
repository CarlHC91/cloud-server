package org.raspberry.cloud.iface.directory;

import java.util.HashMap;
import java.util.Map;

import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DirectoryDetailsIface {

	@Value("${iface.server.cloud.path}")
	private String path;

	public DirectoryDetailsVO findOneById(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		String url = path + "/directoryDetails/findOneById?token_api={token_api}&id_directory={id_directory}";

		Map<String, Object> params = new HashMap<>();
		params.put("token_api", userSessionVO.getTokenApi());
		params.put("id_directory", directoryDetailsVO.getIdDirectory());

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.postForObject(url, null, DirectoryDetailsVO.class, params);
	}
	
	public DirectoryDetailsVO[] findAllByParent(UserDetailsVO userSessionVO, DirectoryDetailsVO parentDirectoryVO) {
		String url = path + "/directoryDetails/findAllByParent?token_api={token_api}&id_parent={id_parent}";

		Map<String, Object> params = new HashMap<>();
		params.put("token_api", userSessionVO.getTokenApi());
		params.put("id_parent", parentDirectoryVO.getIdDirectory());

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.postForObject(url, null, DirectoryDetailsVO[].class, params);
	}

	public void deleteOne(UserDetailsVO userSessionVO, DirectoryDetailsVO directoryDetailsVO) {
		String url = path + "/directoryDetails/deleteOne?token_api={token_api}&id_directory={id_directory}";

		Map<String, Object> params = new HashMap<>();
		params.put("token_api", userSessionVO.getTokenApi());
		params.put("id_directory", directoryDetailsVO.getIdDirectory());

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForObject(url, null, null, params);
	}

}
