package com.fdp.nonoo.service;

import com.fdp.nonoo.entity.Sn;

public abstract interface SnService
{
  public abstract String generate(Sn.Type type);
}