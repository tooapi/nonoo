package com.fdp.nonoo.service.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdp.nonoo.dao.RoleDao;
import com.fdp.nonoo.entity.Role;
import com.fdp.nonoo.service.RoleService;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements
		RoleService {
	@Resource(name = "roleDao")
	public void setBaseDao(RoleDao roleDao) {
		super.setBaseDao(roleDao);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public void save(Role role) {
		super.save(role);
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Role update(Role role) {
		return ((Role) super.update(role));
	}

	@Transactional
	@CacheEvict(value = { "authorization" }, allEntries = true)
	public Role update(Role role, String[] ignoreProperties) {
		return ((Role) super.update(role, ignoreProperties));
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
	public void delete(Role role) {
		super.delete(role);
	}
}