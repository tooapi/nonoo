package com.fdp.nonoo.admin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdp.nonoo.common.Message;
import com.fdp.nonoo.common.Pageable;
import com.fdp.nonoo.entity.Role;
import com.fdp.nonoo.service.RoleService;

@Controller("roleController")
@RequestMapping({ "/role" })
public class RoleController extends BaseController {

	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	@RequestMapping(value = { "/add" }, method = { RequestMethod.GET })
	public String add() {
		return "/admin/role/add";
	}

	@RequestMapping(value = { "/save" }, method = { RequestMethod.POST })
	public String save(Role role, RedirectAttributes redirectAttributes) {

		role.setIsSystem((false));
		role.setAdmins(null);
		roleService.save(role);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = { RequestMethod.GET })
	public String edit(Long id, ModelMap model) {

		model.addAttribute("role", roleService.find(id));
		return "/admin/role/edit";
	}

	@RequestMapping(value = { "/update" }, method = { RequestMethod.POST })
	public String update(Role role, RedirectAttributes redirectAttributes) {

		Role localRole = roleService.find(role.getId());
		if ((localRole == null) || (localRole.getIsSystem()))
			return admin_error;
		roleService.update(role, new String[] { "isSystem", "admins" });
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { RequestMethod.GET })
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", roleService.findPage(pageable));
		return "/admin/role/list";
	}

	@RequestMapping(value = { "/delete" }, method = { RequestMethod.POST })
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Role role = (Role) roleService.find(id);
				if ((role != null)
						&& (((role.getIsSystem()) || ((role
								.getAdmins() != null) && (!(role.getAdmins()
								.isEmpty()))))))
					return Message.error("admin.role.deleteExistNotAllowed",
							new Object[] { role.getName() });
			}
			roleService.delete(ids);
		}
		return success;
	}

}