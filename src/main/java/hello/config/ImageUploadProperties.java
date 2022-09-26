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
		prefix = "image-upload"
)
@NoArgsConstructor
public class ImageUploadProperties {
	private String s3Bucket;
	private String cloudfrontDistributionBaseUrl;
}
