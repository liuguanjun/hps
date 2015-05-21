package com.my.hps.webapp.util;

import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.enums.HpsUserType;

public class SecurityUtil {
	
	private SecurityUtil() {
		
	}
	
    public static HpsUser getCurrentUser() {
    	SecurityContext context = SecurityContextHolder.getContext();
    	if (context == null) {
    		return null;
    	}
    	Authentication auth = context.getAuthentication();
    	if (auth == null) {
    		return null;
    	}
		User principal =  (User) auth.getPrincipal();
		HpsUser hpsUser = principal.getHpsUser();
		if (hpsUser == null) {
			hpsUser = new HpsUser();
			hpsUser.setUser(principal);
		}
		for (Role role : principal.getRoles()) {
			HpsUserType type = HpsUserType.valueOf(role.getName());
			hpsUser.setType(type);
		}
		return hpsUser;
    }

}
