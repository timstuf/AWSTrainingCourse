package hello.config;

import static java.util.List.of;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import hello.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsCognitoIdTokenProcessor {
	private static final String ROLE_PREFIX = "ROLE_";
	private final JwtConfiguration jwtConfiguration;
	private final ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

	private final UserService userService;

	public Authentication authenticate(HttpServletRequest request) {
		try {
			String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
			if(idToken != null) {
				JWTClaimsSet claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken), null);
				validateIssuer(claims);
				verifyIfIdToken(claims);
				String username = getUserName(claims);
				String[] userRoles = getUserRoles(claims);
				if(username != null) {

					UserPrincipal zytaraUserPrincipal = getOrCreateUser(username, getEmail(claims));
					User securityUser = new User(username, "", of());
					UserDetails userDetails = createUserDetails(username, userRoles);

					return new JwtAuthentication(securityUser, claims, userDetails, zytaraUserPrincipal, idToken);
				}
			}
		} catch(ParseException e) {
			log.warn("Token is invalid " + e.getMessage());
		} catch(Exception e) {
			log.warn("Sorry, something went wrong! " + e.getMessage());
		}
		return null;
	}


	private UserDetails createUserDetails(String username, String[] userRoles) {
		return new UserDetails() {

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return Arrays.stream(userRoles)
						.map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
						.collect(Collectors.toList());
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public String getUsername() {
				return username;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isEnabled() {
				return true;
			}
		};
	}

	private String getUserName(JWTClaimsSet claims) {
		return getClaim(claims, this.jwtConfiguration.getUserNameField());
	}

	private String getEmail(JWTClaimsSet claims) {
		return getClaim(claims, this.jwtConfiguration.getUserEmailField());
	}

	private String getClaim(JWTClaimsSet claims, String fieldName) {
		Object value = claims.getClaims().get(fieldName);
		if(value == null) {
			throw new IllegalArgumentException(String.format("Claim '%s' is null", fieldName));
		}
		return value.toString();
	}

	private String[] getUserRoles(JWTClaimsSet claims) {
		JSONArray groups = (JSONArray) claims.getClaims().get(this.jwtConfiguration.getGroupsField());
		List<String> adminRoles = new ArrayList<>();
		if(groups != null) {
			adminRoles = groups.stream().map(Object::toString).collect(Collectors.toList());
		}
		return adminRoles.toArray(String[]::new);
	}

	private void verifyIfIdToken(JWTClaimsSet claims) {
		if(!"id".equals(claims.getClaims().get("token_use"))) {
			log.debug("JWT Token is not an ID Token");
			throw new RuntimeException("Not an ID token");
		}
	}

	private void validateIssuer(JWTClaimsSet claims) {
		if(!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
			log.debug("Issuer {} does not match cognito idp {}",
					claims.getIssuer(),
					this.jwtConfiguration.getCognitoIdentityPoolUrl());
			throw new RuntimeException("Another identity pool");
		}
	}

	private String getBearerToken(String token) {
		return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
	}

	private UserPrincipal getOrCreateUser(String username, String email) {
		hello.entity.User user;
		UserPrincipal principal;
		try {
			user = userService.findByUsername(username);
			principal = new UserPrincipal(user.getId(), email, username);
		} catch(Exception e) {
			user = userService.createUser(email, username);
			principal = new UserPrincipal(user.getId(), email, username);
		}
		return principal;
	}

}
