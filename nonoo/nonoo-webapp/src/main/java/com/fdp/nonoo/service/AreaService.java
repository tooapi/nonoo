package com.fdp.nonoo.service;

import java.util.List;

import com.fdp.nonoo.entity.Area;

public abstract interface AreaService extends BaseService<Area, Long>
{
  public abstract List<Area> findRoots();

  public abstract List<Area> findRoots(Integer count);
}