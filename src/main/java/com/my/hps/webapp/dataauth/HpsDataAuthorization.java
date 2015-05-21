package com.my.hps.webapp.dataauth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HpsDataAuthorization {
	
	TargetType type() default TargetType.Query;
	
	String attrName() default "base.id";
	
	public enum TargetType {
		Query,
		Criteria
	}
	
}
