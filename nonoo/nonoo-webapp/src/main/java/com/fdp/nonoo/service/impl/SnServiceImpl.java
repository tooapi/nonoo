package com.fdp.nonoo.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdp.nonoo.dao.SnDao;
import com.fdp.nonoo.entity.Sn;
import com.fdp.nonoo.service.SnService;

@Service("snService")
public class SnServiceImpl implements SnService {

	@Resource(name = "snDao")
	private SnDao snDao;

	@Transactional
	public String generate(Sn.Type type) {
		return this.snDao.generate(type);
	}
}