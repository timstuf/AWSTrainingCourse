package hello.config;

import java.util.Objects;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;

import hello.utils.SecurityContextUtils;

@Aspect
@Component
public class XRayInspector extends AbstractXRayInterceptor {
	@Override
	@Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && bean(*)")
	protected void xrayEnabledClasses() {
		// Should be empty.
		// https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java-aop-spring.html#xray-sdk-java-aop-activate-xray
	}


	@Before("execution(* hello.controller..*(..)) && @within(org.springframework.web.bind.annotation.RestController)")
	public void defineAttributeUserId(JoinPoint joinPoint) {
		UUID userUuid = SecurityContextUtils.authenticatedUserId();
		if(Objects.nonNull(userUuid)) {
			AWSXRay.getCurrentSegment().putAnnotation("user_id", userUuid.toString());
		}
	}

}
