package hello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.ECSPlugin;
import com.amazonaws.xray.slf4j.SLF4JSegmentListener;
import com.amazonaws.xray.strategy.DefaultStreamingStrategy;
import com.amazonaws.xray.strategy.DefaultThrowableSerializationStrategy;
import com.amazonaws.xray.strategy.SegmentNamingStrategy;

@Configuration
public class XrayConfig {
	@Bean
	public AWSXRayServletFilter tracingFilter() {
		return new AWSXRayServletFilter(SegmentNamingStrategy.fixed("spring-docker"), buildXRayRecorder());
	}

	private AWSXRayRecorder buildXRayRecorder() {
		return AWSXRayRecorderBuilder.standard()
				.withSegmentListener(new SLF4JSegmentListener())
				.withPlugin(new ECSPlugin())
				.withStreamingStrategy(new DefaultStreamingStrategy(10))
				.withThrowableSerializationStrategy(new DefaultThrowableSerializationStrategy(0))
				.build();
	}
}
