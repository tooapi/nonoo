package com.fdp.nonoo.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdp.nonoo.common.Message;
import com.fdp.nonoo.entity.Area;
import com.fdp.nonoo.entity.BaseEntity;
import com.fdp.nonoo.service.AreaService;

@Controller("areaController")
@RequestMapping({ "/area" })
public class AreaController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@RequestMapping(value = { "/add" }, method = { RequestMethod.GET })
	public String add(Long parentId, ModelMap model) {
		model.addAttribute("parent", areaService.find(parentId));
		return "/admin/area/add";
	}

	@RequestMapping(value = { "/save" }, method = { RequestMethod.POST })
	public String save(Area area, Long parentId,
			RedirectAttributes redirectAttributes) {
		area.setParent(areaService.find(parentId));
		if (!(validate(area, new Class[] { BaseEntity.Save.class })))
			return admin_error;
		area.setFullName(null);
		area.setTreePath(null);

		areaService.save(area);
		redirect(redirectAttributes, success);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = { RequestMethod.GET })
	public String edit(Long id, ModelMap model) {
		model.addAttribute("area", areaService.find(id));
		return "/admin/area/edit";
	}

	@RequestMapping(value = { "/update" }, method = { RequestMethod.POST })
	public String update(Area area, RedirectAttributes redirectAttributes) {
		if (!validate(area, new Class[] { BaseEntity.Update.class }))
			return admin_error;
		areaService.update(area, new String[] { "fullName", "treePath",
				"parent", "children" });
		redirect(redirectAttributes, success);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = { RequestMethod.GET })
	public String list(Long parentId, ModelMap model) {
		Area parent = (Area) areaService.find(parentId);
		if (parent != null) {
			model.addAttribute("parent", parent);
			List<Area> areas = parent.getChildren();
			model.addAttribute("areas", areas);
		} else {
			model.addAttribute("areas", areaService.findRoots());
		}
		return "/area/list";
	}

	@RequestMapping(value = { "/delete" }, method = { RequestMethod.POST })
	@ResponseBody
	public Message delete(Long id) {
		areaService.delete(id);
		return success;
	}
}