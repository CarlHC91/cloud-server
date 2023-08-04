package org.raspberry.cloud.app.user;

import org.raspberry.auth.iface.user.UserSessionIface;
import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSessionRest {

	@Autowired
	private UserSessionIface userSessionIface;

	@PostMapping("/api/userSession/findOneByTokenApi")
	public UserDetailsVO findOneByTokenApi(@RequestParam("token_api") String tokenApi) {
		UserDetailsVO userSessionVO = new UserDetailsVO();
		userSessionVO.setTokenApi(tokenApi);

		return userSessionIface.findOneByTokenApi(userSessionVO);
	}

	@PostMapping("/api/userSession/findOneByUsername")
	public UserDetailsVO findOneByUsername(@RequestParam("username") String username, @RequestParam("password") String password) {
		UserDetailsVO userSessionVO = new UserDetailsVO();
		userSessionVO.setUsername(username);
		userSessionVO.setPassword(password);

		return userSessionIface.findOneByUsername(userSessionVO);
	}

}
