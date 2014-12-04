package com.fdp.nonoo.dao;

import java.util.List;

import com.fdp.nonoo.entity.FriendLink;

public abstract interface FriendLinkDao extends BaseDao<FriendLink, Long>
{
  public abstract List<FriendLink> findList(FriendLink.Type type);
}