package com.atguigu.springboot.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ShiroConfiguration {
	//将自己的验证方式加入容器
	@Bean
	public MyShiroRealm myShiroRealm(){
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}
	
	//权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 注入自定义的realm;
		securityManager.setRealm(myShiroRealm());
		// 注入缓存管理器;
		securityManager.setCacheManager(ehCacheManager());
		return securityManager;
	}
	
	//Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String,String> map = new HashMap<String,String>();
		//登出
		map.put("/logout", "logout");//无参，注销，执行后会直接跳转到shiroFilterFactoryBean.setLoginUrl(); 设置的 url
		//anon 无参,开放权限,可以理解为匿名访问
		map.put("/index", "anon");
		map.put("/css/**", "anon");
		map.put("/img/**", "anon");
		map.put("/js/**", "anon");
		map.put("/hello","anon");
		map.put("/User","anon");
		map.put("/Role","anon");
		map.put("/addUser","anon");
		map.put("/addRole","anon");
		map.put("/login","anon");
		//对所有用户认证
		map.put("/**", "authc");
		/**
		 * shiro主体思路：shiro默认第一次走MyShiroRealm 第二次走controller
		 * 问题：未授权跳转到了登录页面，而不是首页
		 * 方案：第一次走controller的login，未授权跳转到首页
		 * 实现方法：
		 *			1）增加map.put("/login","anon");controller层登录
		 *			2）	shiroFilterFactoryBean.setLoginUrl("/index");设置为授权跳转到首页
		 *			3）保留index页面直接跳转MyShiroRealm直接登录方法:shiroFilterFactoryBean.setLoginUrl("/index");
		 *				备注：不设置默认login.jsp
		 *			4)	shiroFilterFactoryBean.setSuccessUrl("/index");登录成功后还是首页
		 */
		//登录
		shiroFilterFactoryBean.setLoginUrl("/index");
		//首页
		shiroFilterFactoryBean.setSuccessUrl("/index");
		//错误页面，认证部通过跳转
		// 未授权的页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		return shiroFilterFactoryBean;
	}
	/*
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码; )
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(1024);// 散列的次数，比如散列两次，相当于md5(md5(""));
		return hashedCredentialsMatcher;
	}

	/*
	 * 开启shiro aop注解支持 使用代理方式;所以需要开启代码支持;
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
	 */
	/*@Bean
	@ConditionalOnMissingBean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
		defaultAAP.setProxyTargetClass(true);
		return defaultAAP;
	}*/
	/*
	 * shiro缓存管理器;
	 * 需要注入对应的其它的实体类中-->安全管理器：securityManager可见securityManager是整个shiro的核心；
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		System.out.println("ShiroConfiguration.getEhCacheManager()");
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		return cacheManager;
	}
}
