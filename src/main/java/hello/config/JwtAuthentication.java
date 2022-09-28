package hello.config;

import java.util.UUID;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jwt.JWTClaimsSet;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, exclude = "principal")
public class JwtAuthentication extends AbstractAuthenticationToken {
	private final transient Object principal;
	private final JWTClaimsSet jwtClaimsSet;
	private final UserDetails userDetails;
	private final UserPrincipal userPrincipal;
	private final String token;

	public JwtAuthentication(Object principal, JWTClaimsSet jwtClaimsSet, UserDetails userDetails, UserPrincipal userPrincipal, String token) {
		super(userDetails.getAuthorities());
		this.principal = principal;
		this.jwtClaimsSet = jwtClaimsSet;
		super.setAuthenticated(true);
		this.userDetails = userDetails;
		this.userPrincipal = userPrincipal;
		this.token = token;
	}

	public Object getCredentials() {
		return null;
	}

	public Object getPrincipal() {
		return this.principal;
	}

	public JWTClaimsSet getJwtClaimsSet() {
		return this.jwtClaimsSet;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public UUID getUserId() {
		return userPrincipal.getId();
	}

	public UserPrincipal getUserPrincipal() {
		return userPrincipal;
	}

	public String getToken() {
		return token;
	}
}
