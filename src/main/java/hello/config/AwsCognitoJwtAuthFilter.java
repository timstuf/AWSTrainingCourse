package hello.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AwsCognitoJwtAuthFilter extends OncePerRequestFilter {
	private final transient AwsCognitoIdTokenProcessor cognitoIdTokenProcessor;


	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		Authentication authentication;
		try {
			authentication = this.cognitoIdTokenProcessor.authenticate(request);
			if(authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch(Exception e) {
			String error = "Cognito ID Token processing error " + e.getMessage();
			log.error(error);
			SecurityContextHolder.clearContext();
		}
		addSecurityHeadersToResponse(response);
		filterChain.doFilter(request, response);
	}

	private void addSecurityHeadersToResponse(HttpServletResponse response) {
		response.addHeader("Strict-Transport-Security", "max-age=63072000; includeSubDomains;");
		response.addHeader("Content-Security-Policy", "frame-ancestors 'none'; default-src 'self'; script-src 'self'; img-src 'self'; style-src 'self';");
		response.addHeader("Referrer-Policy", "no-referrer;");
	}
}

