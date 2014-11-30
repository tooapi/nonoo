package com.fdp.nonoo.dao.impl;

import org.springframework.stereotype.Repository;

import com.fdp.nonoo.dao.RoleDao;
import com.fdp.nonoo.entity.Role;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {
}