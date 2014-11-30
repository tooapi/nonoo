package com.fdp.nonoo.admin.controller;

import java.util.HashSet;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdp.nonoo.common.Message;
import com.fdp.nonoo.common.Pageable;
import com.fdp.nonoo.entity.Admin;
import com.fdp.nonoo.entity.BaseEntity;
import com.fdp.nonoo.entity.Role;
import com.fdp.nonoo.service.AdminService;
import com.fdp.nonoo.service.RoleService;

@Controller("adminController")
@RequestMapping({ "/admin" })
public class AdminController extends BaseController {

	@Resource(name = "adminService")
	private AdminService adminService;

	@Resource(name = "roleService")
	private RoleService roleService;

	@RequestMapping(value = { "/check_username" }, method = { RequestMethod.GET })
	@ResponseBody
	public boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username))
			return false;
		return (!(adminService.usernameExists(username)));
	}

	@RequestMapping(value = { "/add" }, method = { RequestMethod.GET })
	public String add(ModelMap model) {
		model.addAttribute("roles", roleService.findAll());
		return "/admin/add";
	}

	@RequestMapping(value = { "/save" }, method = { RequestMethod.POST })
	public String save(Admin admin, Long[] roleIds,
			RedirectAttributes redirectAttributes) {
		admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
		if (!(validate(admin, new Class[] { BaseEntity.Save.class })))
			return admin_error;
		if (adminService.usernameExists(admin.getUsername()))
			return admin_error;
		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		admin.setIsLocked(false);
		admin.setLoginFailureCount(0);
		admin.setLockedDate(null);
		admin.setLoginDate(null);
		admin.setLoginIp(null);
		adminService.save(admin);
		redirect(redirectAttributes, success);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = { RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("roles", roleService.findAll());
		model.addAttribute("admin", adminService.find(id));
		return "/admin/edit";
	}

	@RequestMapping(value = { "/update" }, method = { RequestMethod.POST })
	public String update(Admin admin, Long[] roleIds,
			RedirectAttributes redirectAttributes) {
		admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
		if (!validate(admin, new Class[] { BaseEntity.Update.class }))
			return admin_error;
		Admin localAdmin = (Admin) adminService.find(admin.getId());
		if (localAdmin == null)
			return admin_error;
		if (StringUtils.isNotEmpty(admin.getPassword()))
			admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		else
			admin.setPassword(localAdmin.getPassword());
		if ((localAdmin.getIsLocked())
				&& (!(admin.getIsLocked()))) {
			admin.setLoginFailureCount(0);
			admin.setLockedDate(null);
		} else {
			admin.setIsLocked(localAdmin.getIsLocked());
			admin.setLoginFailureCount(localAdmin.getLoginFailureCount());
			admin.setLockedDate(localAdmin.getLockedDate());
		}
		adminService.update(admin, new String[] { "username", "loginDate","loginIp", "orders" });
		redirect(redirectAttributes, success);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", adminService.findPage(pageable));
		return "/admin/list";
	}

	@RequestMapping(value = { "/delete" }, method = { RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		adminService.delete(ids);
		return success;
	}
}