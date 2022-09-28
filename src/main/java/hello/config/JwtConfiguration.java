package hello.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(
		prefix = "jwt.aws"
)
@NoArgsConstructor
public class JwtConfiguration {
	private String userPoolId;
	private String identityPoolId;
	private String jwkUrl;
	private String region;
	private String userNameField = "cognito:username";
	private String groupsField = "cognito:groups";
	private String userEmailField = "email";
	private String userBirthdateField = "birthdate";
	private int connectionTimeout = 2000;
	private int readTimeout = 2000;
	private String httpHeader = "Authorization";

	public String getJwkUrl() {
		return this.jwkUrl != null && !this.jwkUrl.isEmpty()
				? this.jwkUrl
				: String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json",
				this.region, this.userPoolId);
	}

	public void setJwkUrl(String jwkUrl) {
		this.jwkUrl = jwkUrl;
	}

	public String getCognitoIdentityPoolUrl() {
		return String.format("https://cognito-idp.%s.amazonaws.com/%s", this.region, this.userPoolId);
	}
}
