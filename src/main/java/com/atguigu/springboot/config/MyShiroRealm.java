package com.atguigu.springboot.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.springboot.domain.Permission;
import com.atguigu.springboot.domain.Role;
import com.atguigu.springboot.domain.User;
import com.atguigu.springboot.service.ILoginService;
/**
 * 实现AuthorizingRealm接口用户认证
 * @author T-shenw
 *
 */
public class MyShiroRealm extends AuthorizingRealm {

	//用于用户查询
	@Autowired
	private ILoginService loginService;
	
	//角色权限和对应权限添加
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//获取登录用户名
		String name = (String) principalCollection.getPrimaryPrincipal();
		//查询用户名称
		User user = loginService.findByName(name);
		//添加角色和权限
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		for (Role role : user.getRoles()) {
			//添加角色
			simpleAuthorizationInfo.addRole(role.getRoleName());
			for (Permission permission : role.getPermissions()) {
				//添加权限
				simpleAuthorizationInfo.addStringPermission(permission.getPermission());
			}
		}
		return simpleAuthorizationInfo;
	}

	/**
	 * 方面用于加密 参数：AuthenticationToken是从表单穿过来封装好的对象
	 */
	//用户认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		/*System.out.println("doGetAuthenticationInfo:" + authenticationToken);
		// 将AuthenticationToken强转为AuthenticationToken对象
		UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;

		// 获得从表单传过来的用户名
		String username = upToken.getUsername();*/

		//加这一不得目的是在Post请求的时候会先进认证，然后在到请求
		if(authenticationToken.getPrincipal() == null){
			return null;
		}
		//获取用户信息
		String name = authenticationToken.getPrincipal().toString();
		User user = loginService.findByName(name);
		if(user == null){
			//这里返回后会报出对应异常
//			throw new UnknownAccountException("无此用户名！");
			System.out.println("无此用户名！");
			return null;
		}else{
			// 从数据库中查询的密码
			String credentials = user.getPassword();
			// 颜值加密的颜，可以用用户名
			ByteSource credentialsSalt = ByteSource.Util.bytes(name);
			//这里验证authenticationToken和simpleAuthorizationInfo的信息
			SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name,credentials,credentialsSalt,getName());
			return simpleAuthenticationInfo;
		}
	}

}
