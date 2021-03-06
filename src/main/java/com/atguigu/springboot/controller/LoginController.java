package com.atguigu.springboot.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.atguigu.springboot.domain.Role;
import com.atguigu.springboot.domain.User;
import com.atguigu.springboot.service.ILoginService;
//@RestController
//@ResponseBody
@Controller
public class LoginController {
	@Autowired
	private ILoginService loginService;
	
	//退出的时候是get请求，主要是用于退出
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String login(){
		return "login";
	}

	//post登录
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,@RequestParam("password") String password,boolean rememberMe){
		//添加用户认证信息
		Subject subject = SecurityUtils.getSubject();
		// 验证用户是否验证，即是否登录
		if (!subject.isAuthenticated()) {
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
			usernamePasswordToken.setUsername(username);
			usernamePasswordToken.setPassword(password.toCharArray());
			// remembermMe记住密码
			usernamePasswordToken.setRememberMe(rememberMe);
			//进行验证，这里可以捕获异常，然后返回对应信息
			subject.login(usernamePasswordToken);
			System.out.println("loginSuccess");
			return "index";
		}
		return "index";
	}
	@RequestMapping(value = "/index")
	public String index(){
		return "index";
	}

	@RequestMapping(value = "/success")
	public String success(){
		return "success";
	}

	//登出
	/*@ResponseBody
	@RequestMapping(value = "/logout")
	public String logout(){
		System.out.println("logout");
		return "logout";
	}*/
	
	//错误页面展示
	@RequestMapping(value = "/error",method = RequestMethod.POST)
	public String error(){
		return "error ok!";
	}

	@RequestMapping(value = "/User")
	public String user(){
		return "User";
	}

	//数据初始化
	@RequestMapping(value = "/addUser",method = RequestMethod.POST)
	public String addUser(@RequestParam("username") String username,@RequestParam("password") String password,Map<String,Object> map){
//        Object salt = null;//盐值
		//盐值用的用的是对用户名的加密（测试用的"sw"）
		ByteSource credentialsSalt01 = ByteSource.Util.bytes(username);
		Object credential = password;//密码
		String hashAlgorithmName = "MD5";//加密方式
		//1024指的是加密的次数
		Object simpleHash = new SimpleHash(hashAlgorithmName, credential,
				credentialsSalt01, 1024);
		System.out.println("加密后的值----->" + simpleHash);
		map.put("username",username);
		map.put("password",simpleHash);
		User user = loginService.addUser(map);
		System.out.println("addUser is ok! \n" + user);
		return "success";
	}
	@RequestMapping(value = "/Role")
	public String role(){
		return "Role";
	}

	//角色初始化
	@RequestMapping(value = "/addRole",method = RequestMethod.POST)
	public String addRole(@RequestParam("userId") String userId,@RequestParam("roleName") String roleName,Map<String,Object> map){
		map.put("userId",userId);
		map.put("roleName",roleName);
		Role role = loginService.addRole(map);
		System.out.println("addRole is ok! \n");
		return "success";
	}
	
	//权限注解的使用
	@ResponseBody
	@RequiresRoles("admin")
	@RequiresPermissions("create")
	@RequestMapping(value = "/create")
	public String create(){
		return "Create success!";
	}
	//rememnber获取用户信息
	@ResponseBody
	@RequestMapping(value = "/userinfo")
	public String userinfo(){
		Subject subject = SecurityUtils.getSubject();
		System.out.println(subject.getPrincipal());
		return subject.toString();
	}
	/**
	 * /属于user角色
	 * @RequiresRoles("user")
	 *
	 * //必须同时属于user和admin角色
	 * @RequiresRoles({"user","admin"})
	 *
	 * //属于user或者admin之一;修改logical为OR 即可
	 * @RequiresRoles(value={"user","admin"},logical=Logical.OR)
	 * 1
	 */
	/**
	 * //符合index:hello权限要求
	 * @RequiresPermissions("index:hello")
	 *
	 * //必须同时复核index:hello和index:world权限要求
	 * @RequiresPermissions({"index:hello","index:world"})
	 *
	 * //符合index:hello或index:world权限要求即可
	 * @RequiresPermissions(value={"index:hello","index:world"},logical=Logical.OR)
	 * 1
	 */
}
