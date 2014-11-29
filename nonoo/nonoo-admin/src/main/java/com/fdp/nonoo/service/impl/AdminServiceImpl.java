package com.fdp.nonoo.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdp.nonoo.common.Principal;
import com.fdp.nonoo.dao.AdminDao;
import com.fdp.nonoo.entity.Admin;
import com.fdp.nonoo.entity.Role;
import com.fdp.nonoo.service.AdminService;

@Service("adminService")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements
		AdminService {

	@Resource(name = "adminDao")
	private AdminDao adminDao;

	@Resource(name = "adminDao")
	public void setBaseDao(AdminDao adminDao) {
		super.setBaseDao(adminDao);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return adminDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		return adminDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<String> findAuthorities(Long id) {
		List<String> authorities = new ArrayList<String>();
		Admin admin = (Admin) adminDao.find(id);
		if (admin != null) {
			Iterator<Role> iterator = admin.getRoles().iterator();
			while (iterator.hasNext()) {
				Role role = (Role) iterator.next();
				authorities.addAll(role.getAuthorities());
			}
		}
		return authorities;
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null)
			return subject.isAuthenticated();
		return false;
	}

	@Transactional(readOnly = true)
	public Admin getCurrent() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null)
				return (Admin) adminDao.find(principal.getId());
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null)
				return principal.getUsername();
		}
		return null;
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void save(Admin admin) {
		super.save(admin);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Admin update(Admin admin) {
		return (Admin) super.update(admin);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Admin update(Admin admin, String[] ignoreProperties) {
		return (Admin) super.update(admin, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Long[] ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void delete(Admin admin) {
		super.delete(admin);
	}
}