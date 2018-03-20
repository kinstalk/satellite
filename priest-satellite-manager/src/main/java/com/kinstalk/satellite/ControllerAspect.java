package com.kinstalk.satellite;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by digitZhang on 16/8/29.
 */
@Aspect
public class ControllerAspect {

	private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Pointcut("execution(* com.kinstalk.satellite.web.*.*(..)) ")
	public void costPointcut() {
	}

	@Around("costPointcut()")
	public Object costTime(ProceedingJoinPoint pjp) throws Throwable {
		Object result = pjp.proceed();

		try {

		} catch (Throwable throwable) {
			logger.error("process operation log exception!", throwable);
		}

		return result;
	}
}