package hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";
	private static final String DOCUMENTATION_ENTRY_POINT = "/internal/documentation/**";
	private static final String ACTUATOR_INFO_ENDPOINT = "/actuator/info";
	private static final String ACTUATOR_ENDPOINTS = "/actuator/health";

	private final RestAuthenticationEntryPoint authenticationEntryPoint;
	private final AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().cacheControl();
		http.csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, ACTUATOR_ENDPOINTS).permitAll()
				.antMatchers(DOCUMENTATION_ENTRY_POINT).authenticated()
				.antMatchers(ACTUATOR_INFO_ENDPOINT).authenticated()
				.antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
				.and()
				.addFilterBefore(awsCognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling()
				.authenticationEntryPoint(this.authenticationEntryPoint);
	}

}
