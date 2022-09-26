package hello.config;


import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AwsConfig {
	@PostConstruct
	public void init() {
		log.info("Init AWS configuration");
	}

//	@Bean
//	public AWSCognitoIdentityProvider amazonCognitoClient(@Value("${jwt.aws.users.userPoolArn}") String zytaraUserPoolArn) {
//		return AWSCognitoIdentityProviderClientBuilder
//				.standard()
//				.withRegion(Arn.fromString(zytaraUserPoolArn).getRegion())
//				.build();
//	}
//
//	@Bean
//	public AmazonSimpleEmailService amazonSesClient(@Value("${jwt.aws.users.userPoolArn}") String zytaraUserPoolArn) {
//		return AmazonSimpleEmailServiceClientBuilder.standard()
//				.withRegion(Arn.fromString(zytaraUserPoolArn).getRegion())
//				.build();
//	}

	@Bean
	public AmazonS3 amazonS3Client() {
		return AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();
	}

//	@Bean
//	public AWSSimpleSystemsManagement ssmClient() {
//		return AWSSimpleSystemsManagementClientBuilder
//				.standard()
//				.withRegion(Regions.US_EAST_1)
//				.build();
//	}
}

