package com.fdp.nonoo.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fdp.nonoo.entity.Admin;
import com.fdp.nonoo.service.AdminService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-shiro.xml",
		"classpath:dispatcher-servlet.xml" })
public class AdminServiceTestCase {

	@Resource(name = "adminService")
	private AdminService adminService;

	
	@Test
	public void save() {
        Admin admin=new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("yanzhijun1985@163.com");
        admin.setIsEnabled(true);
        admin.setIsLocked(true);
        admin.setLoginFailureCount(0);
		adminService.save(admin);
	}

}
