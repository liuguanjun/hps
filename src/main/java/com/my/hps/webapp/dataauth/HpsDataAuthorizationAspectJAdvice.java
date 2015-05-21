package com.my.hps.webapp.dataauth;

import java.lang.reflect.Method;

import org.appfuse.dao.hibernate.HpsGenericDaoHibernate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HpsDataAuthorizationAspectJAdvice {
	
	@Pointcut("execution(public * org.appfuse.dao.hps.*.*(..)) && @annotation(com.my.hps.webapp.dataauth.HpsDataAuthorization)")  
    public void pointCutDao() {}
	
	@Around("pointCutDao()")
	public Object beforeDao(ProceedingJoinPoint joinPoint) throws Throwable {
		Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
		method = joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
		HpsDataAuthorization an = method.getAnnotation(HpsDataAuthorization.class);
		HpsGenericDaoHibernate.setCurrentThreadAuthorizationData(an);
		try {
			return joinPoint.proceed(joinPoint.getArgs());
		} finally {
			HpsGenericDaoHibernate.removeCurrentThreadAuthorizationData();
		}
	}
	

}
