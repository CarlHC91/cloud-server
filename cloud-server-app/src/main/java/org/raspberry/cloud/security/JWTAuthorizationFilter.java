package org.raspberry.cloud.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.raspberry.auth.iface.role.RolePermissionIface;
import org.raspberry.auth.iface.user.UserRoleIface;
import org.raspberry.auth.iface.user.UserSessionIface;
import org.raspberry.auth.pojos.entities.role.RoleDetailsVO;
import org.raspberry.auth.pojos.entities.role.RolePermissionVO;
import org.raspberry.auth.pojos.entities.user.UserDetailsVO;
import org.raspberry.auth.pojos.entities.user.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private RolePermissionIface rolePermissionIface;

	@Autowired
	private UserRoleIface userRoleIface;
	@Autowired
	private UserSessionIface userSessionIface;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>());

		String tokenApi = request.getParameter("token_api");
		if (tokenApi != null) {
			UserDetailsVO userSessionVO = new UserDetailsVO();
			userSessionVO.setTokenApi(tokenApi);

			UserDetailsVO userDetailsVO = userSessionIface.findOneByTokenApi(userSessionVO);
			if (userDetailsVO != null) {
				List<GrantedAuthority> authorityList = new ArrayList<>();

				for (UserRoleVO userRoleVO : userRoleIface.findAllByUser(userSessionVO, userDetailsVO)) {
					RoleDetailsVO roleDetailsVO = new RoleDetailsVO();
					roleDetailsVO.setIdRole(userRoleVO.getIdRole());
					
					for (RolePermissionVO rolePermissionVO : rolePermissionIface.findAllByRole(userSessionVO, roleDetailsVO)) {
						GrantedAuthority authority = new SimpleGrantedAuthority(rolePermissionVO.getName());

						authorityList.add(authority);
					}
				}

				authentication = new UsernamePasswordAuthenticationToken(userDetailsVO, tokenApi, authorityList);
			}
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

}