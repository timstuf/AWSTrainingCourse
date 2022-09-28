package hello.utils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import hello.config.JwtAuthentication;
import hello.config.UserPrincipal;


public final class SecurityContextUtils {
	private SecurityContextUtils() {
	}

	public static UUID authenticatedUserId() {
		JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return authentication.getUserId();
	}

	public static String authenticatedUserName() {
		JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return authentication.getUserPrincipal().getUsername();
	}

	public static UserPrincipal authenticatedUserPrincipal() {
		JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return authentication.getUserPrincipal();
	}

	public static String getToken() {
		JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return authentication.getToken();
	}

	public static List<String> authenticatedUserRoles() {
		JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return authentication.getUserDetails().getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
	}

}
