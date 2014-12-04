package com.fdp.nonoo.dao;

import com.fdp.nonoo.entity.Sn;

public abstract interface SnDao {
	public abstract String generate(Sn.Type type);
}