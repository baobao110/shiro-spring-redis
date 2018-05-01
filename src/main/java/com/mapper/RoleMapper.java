package com.mapper;

import com.entity.role;

import java.util.Set;

public interface RoleMapper {
    public Set<role> getRoleByUsername(String username);
}
