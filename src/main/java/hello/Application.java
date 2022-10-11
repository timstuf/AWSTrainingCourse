package hello;


import static com.nimbusds.jose.JWSAlgorithm.RS256;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import hello.config.JwtConfiguration;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {
	private final JwtConfiguration jwtConfiguration;

	@RequestMapping("/")
	public String home() {
		InetAddress ip;
		String hostname;
		try {
			ip = Inet4Address.getLocalHost();
			hostname = ip.getHostName();
			System.out.println("Your current IP address : " + ip.getHostAddress());
			System.out.println("Your current Hostname : " + hostname);
			return hostname + " " + ip.getHostAddress();
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}

		return "Hello Docker World";
	}

	@Bean
	public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor() throws MalformedURLException {
		ResourceRetriever resourceRetriever =
				new DefaultResourceRetriever(jwtConfiguration.getConnectionTimeout(),
						jwtConfiguration.getReadTimeout());
		URL jwkSetURL = new URL(jwtConfiguration.getJwkUrl());
		JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwkSetURL, resourceRetriever);
		ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
		JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(RS256, keySource);
		jwtProcessor.setJWSKeySelector(keySelector);
		return jwtProcessor;
	}

	public static void main(String[] args) {
		SpringApplication.run(hello.Application.class, args);
	}


}
