package com.my.hps.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.hps.HpsBaseManager;
import org.appfuse.service.hps.HpsUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.hps.webapp.controller.queryparam.UserQueryParam;
import com.my.hps.webapp.exception.UserAccountExistsException;
import com.my.hps.webapp.model.HpsBase;
import com.my.hps.webapp.model.HpsUser;
import com.my.hps.webapp.model.PaginationResult;
import com.my.hps.webapp.model.enums.HpsUserType;
import com.my.hps.webapp.util.SecurityUtil;

@Controller
@RequestMapping("/admin/UserManagement/")
public class UserManagementController extends BaseFormController {
	
	private HpsUserManager hpsUserManager;
	private HpsBaseManager baseManager;
	private RoleManager roleManager;

	@RequestMapping(method=RequestMethod.GET,value="users")
	@ResponseBody
	public PaginationResult<HpsUser> getUsers(@ModelAttribute UserQueryParam param) {
		List<HpsUser> allUsers = hpsUserManager.getHpsUsers();
		List<HpsUser> resultUsers = new ArrayList<HpsUser>();
		for (HpsUser user : allUsers) {
			Set<Role> roles = user.getUser().getRoles();
			if (roles.size() > 0) {
				String roleName = roles.iterator().next().getName();
				user.setType(HpsUserType.valueOf(roleName));
			}
			if (isSearchResult(user, param)) {
				resultUsers.add(user);
			}
		}
		int total = resultUsers.size();
		PaginationResult<HpsUser> result = new PaginationResult<HpsUser>();
		result.setTotal(total);
		int nextPageOffset = param.getOffset() + param.getRows();
		if (param.getOffset() >= total) {
			result.setRows(new ArrayList<HpsUser>());
			return result;
		}
		if (nextPageOffset < total) {
			result.setRows(resultUsers.subList(param.getOffset(), nextPageOffset));
		} else {
			result.setRows(resultUsers.subList(param.getOffset(), total));
		}
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="user")
	@ResponseBody
	public HpsUser addUser(@ModelAttribute HpsUser user) {
		HpsBase hpsBaseTransient = user.getBase();
		HpsBase hpsBaseInDB = baseManager.getBaseByCode(hpsBaseTransient.getCode());
		user.setBase(hpsBaseInDB);
		String roleName = user.getType().getCode();
		Role role = roleManager.getRole(roleName);
		user.getUser().addRole(role);
		user = hpsUserManager.saveHpsUser(user);
		return user;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{baseCode}/operators")
	@ResponseBody
	public List<HpsUser> getOperators(@PathVariable String baseCode) {
		List<HpsUser> resultUsers = new ArrayList<HpsUser>();
		HpsUser currentUser = SecurityUtil.getCurrentUser();
		currentUser = hpsUserManager.get(currentUser.getId());
		// 先放入当前用户到resultUser中，确保其排在第一位
		resultUsers.add(currentUser);
		if (currentUser.getType() == HpsUserType.ROLE_USER) {
			// 如果当前登录的用户时操作员，则只返回自己
		} else {
			List<HpsUser> allUsers = hpsUserManager.getHpsUsers();
			for (HpsUser user : allUsers) {
				Set<Role> roles = user.getUser().getRoles();
				if (roles.size() > 0) {
					String roleName = roles.iterator().next().getName();
					user.setType(HpsUserType.valueOf(roleName));
				}
				if (user.getType() == HpsUserType.ROLE_USER
						&& user.getBase().getCode().equals(baseCode) // 多虑掉本基地以外的用户
						&& !user.getId().equals(currentUser.getId())) { // 过滤掉自己
					resultUsers.add(user);
				}
			}
		}
		return resultUsers;
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="user/{userId}")
	@ResponseBody
	public HpsUser updateUser(@PathVariable Long userId, @ModelAttribute HpsUser user) {
		HpsUser userInDB = hpsUserManager.getHpsUser(userId);
		userInDB.setPassword(user.getPassword());
		if (userInDB.getType() != user.getType()) {
			String roleName = user.getType().getCode();
			Role role = roleManager.getRole(roleName);
			userInDB.getUser().getRoles().clear();
			userInDB.getUser().addRole(role);
		}
		userInDB.setSex(user.getSex());
		userInDB.setUserName(user.getUserName());
		userInDB.setMobilePhoneNo(user.getMobilePhoneNo());
		HpsBase hpsBaseTransient = user.getBase();
		HpsBase hpsBaseInDB = baseManager.getBaseByCode(hpsBaseTransient.getCode());
		userInDB.setBase(hpsBaseInDB);
		try {
			hpsUserManager.saveHpsUser(userInDB);
		} catch (UserAccountExistsException e) {
			// 修改时账户名称不可以修改，忽略这个异常
		}
		return userInDB;
	}
	
	@RequestMapping(method=RequestMethod.DELETE,value="user/{userId}")
	@ResponseBody
	public void deleteUser(@PathVariable Long userId) {
		hpsUserManager.removeHpsUser(userId);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="user/{userId}")
	@ResponseBody
	public HpsUser getUser(@PathVariable Long userId) {
		HpsUser hpsUser = hpsUserManager.getHpsUser(userId);
		User appUser = hpsUser.getUser();
		Set<Role> roles = appUser.getRoles();
		if (roles.size() > 0) {
			String roleName = roles.iterator().next().getName();
			hpsUser.setType(HpsUserType.valueOf(roleName));
		}
		return hpsUser;
	}

	@Autowired
	public void setHpsUserManager(HpsUserManager hpsUserManager) {
		this.hpsUserManager = hpsUserManager;
	}
	
	@Autowired
	public void setBaseManager(HpsBaseManager baseManager) {
		this.baseManager = baseManager;
	}
	
	private boolean isSearchResult(HpsUser user, UserQueryParam condition) {
		if (condition.isEmpty()) {
			return true;
		}
		String baseCode = condition.getBaseCode();
		if (StringUtils.isNotEmpty(baseCode)) {
			HpsBase userBase = user.getBase();
			if (userBase == null || !baseCode.equals(user.getBase().getCode())) {
				return false;
			}
		}
		String typeCode = condition.getTypeCode();
		if (StringUtils.isNotEmpty(typeCode)) {
			if (!typeCode.equals(user.getType().getCode())) {
				return false;
			}
		}
		String sexCode = condition.getSexCode();
		if (StringUtils.isNotEmpty(sexCode)) {
			if (!sexCode.equals(user.getSex().getCode())) {
				return false;
			}
		}
		String accountName = condition.getAccountName();
		if (StringUtils.isNotEmpty(accountName)) {
			if (!user.getAccountName().contains(accountName)) {
				return false;
			}
		}
		String userName = condition.getUserName();
		if (StringUtils.isNotEmpty(userName)) {
			if (!user.getUserName().contains(userName)) {
				return false;
			}
		}
		String mobilePhoneNo = condition.getMobilePhoneNo();
		if (StringUtils.isNotEmpty(mobilePhoneNo)) {
			if (!user.getMobilePhoneNo().contains(mobilePhoneNo)) {
				return false;
			}
		}
		return true;
	}
	
	@Autowired
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }


}
