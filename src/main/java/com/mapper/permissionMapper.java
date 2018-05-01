package com.mapper;

import com.entity.Permission;

import java.util.Set;

public interface permissionMapper {

    public Set<Permission> getPermissionByUsername(String username);
}
