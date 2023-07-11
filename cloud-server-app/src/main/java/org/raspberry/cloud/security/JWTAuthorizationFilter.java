package org.raspberry.cloud.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.raspberry.auth.iface.user.UserAuthorityIface;
import org.raspberry.auth.iface.user.UserSessionIface;
import org.raspberry.auth.pojos.entities.user.UserAuthorityVO;
import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private UserAuthorityIface userAuthorityIface;
	@Autowired
	private UserSessionIface userSessionIface;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String tokenApi = request.getParameter("token_api");

		if (tokenApi != null) {
			UserDetailsVO userSessionVO = new UserDetailsVO();
			userSessionVO.setTokenApi(tokenApi);

			UserDetailsVO userDetailsVO = userSessionIface.findOneByTokenApi(userSessionVO);
			if (userDetailsVO != null) {
				List<GrantedAuthority> authorityList = new ArrayList<>();

				for (UserAuthorityVO userAuthorityVO : userAuthorityIface.findAllByUser(userSessionVO, userDetailsVO)) {
					GrantedAuthority authority = new SimpleGrantedAuthority(userAuthorityVO.getName());

					authorityList.add(authority);
				}

				SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetailsVO, tokenApi, authorityList));
			}
		}

		filterChain.doFilter(request, response);
	}

}