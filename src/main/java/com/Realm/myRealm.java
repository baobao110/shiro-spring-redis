package com.Realm;

import com.entity.Permission;
import com.entity.User;
import com.entity.role;
import com.mapper.RoleMapper;
import com.mapper.UserMapper;
import com.mapper.permissionMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class myRealm  extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private permissionMapper permissionMapper;

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体信息中传过来的信息中获取用户名
        String username=(String)authenticationToken.getPrincipal();

        //2 通过用户名从数据库中获取凭证
        User user=getPasswordByUsername(username);
        if(null==user.getPassword()){
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),getName());
        //密码加盐
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Jack"));
        return simpleAuthenticationInfo;
    }

    public User getPasswordByUsername(String username){
        return userMapper.getByuserName(username);
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username=(String)principalCollection.getPrimaryPrincipal();
        Set<String> role=getRolesByUsername(username);
       Set<String > permissions=getPermissionByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(role);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    public Set<String> getRolesByUsername(String username){
        Set<String >role=new HashSet<String>();
        Set<role> set=roleMapper.getRoleByUsername(username);
        for(role i:set){
            role.add(i.getRole_name());
        }
        return role;
    }

    public Set<String> getPermissionByUsername(String username){
        Set<String>permission=new HashSet<String>();
        Set<Permission> permissions=permissionMapper.getPermissionByUsername(username);
        for(Permission i:permissions){
            permission.add(i.getPermission());
        }
        return permission;

    }

}
