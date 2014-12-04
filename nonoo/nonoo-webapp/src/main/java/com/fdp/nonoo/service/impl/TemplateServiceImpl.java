package com.fdp.nonoo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.fdp.nonoo.common.CommonAttributes;
import com.fdp.nonoo.common.Template;
import com.fdp.nonoo.service.TemplateService;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService,
		ServletContextAware {
	private ServletContext servletContext;

	@Value("${template.loader_path}")
	private String[] folder;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@SuppressWarnings("unchecked")
	@Cacheable({ "template" })
	public List<Template> getAll() {
		try {
			File config = new ClassPathResource(CommonAttributes.FDP_XML_PATH)
					.getFile();
			Document document = new SAXReader().read(config);
			List<Template> templates = new ArrayList<Template>();
			List<Element> result = document.selectNodes("/fdp/template");
			Iterator<Element> iterator = result.iterator();
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				Template template = getTemplate(element);
				templates.add(template);
			}
			return templates;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Cacheable({ "template" })
	public List<Template> getList(Template.Type type) {
		if (type != null)
			try {
				File config = new ClassPathResource(
						CommonAttributes.FDP_XML_PATH).getFile();
				Document document = new SAXReader().read(config);
				List<Template> templateList = new ArrayList<Template>();
				List<Element> elements = document.selectNodes("/fdp/template[@type='" + type + "']");
				Iterator<Element> iterator = elements.iterator();
				while (iterator.hasNext()) {
					Element element = (Element) iterator.next();
					Template template = getTemplate(element);
					templateList.add(template);
				}
				return templateList;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		return getAll();
	}

	@Cacheable({ "template" })
	public Template get(String id) {
		try {
			File config = new ClassPathResource(CommonAttributes.FDP_XML_PATH)
					.getFile();
			Document document = new SAXReader().read(config);
			Element element = (Element) document
					.selectSingleNode("/fdp/template[@id='" + id + "']");
			Template template = getTemplate(element);
			return template;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String read(String id) {
		Template template = get(id);
		return read(template);
	}

	public String read(Template template) {
		String path = servletContext.getRealPath(folder[0]
				+ template.getTemplatePath());
		File config = new File(path);
		String templateName = null;
		try {
			templateName = FileUtils.readFileToString(config, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return templateName;
	}

	public void write(String id, String content) {
		Template template = get(id);
		write(template, content);
	}

	public void write(Template template, String content) {
		String path = servletContext.getRealPath(folder[0]
				+ template.getTemplatePath());
		File config = new File(path);
		try {
			FileUtils.writeStringToFile(config, content, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Template getTemplate(Element element) {
		String id = element.attributeValue("id");
		String type = element.attributeValue("type");
		String name = element.attributeValue("name");
		String templatePath = element.attributeValue("templatePath");
		String staticPath = element.attributeValue("staticPath");
		String description = element.attributeValue("description");
		Template template = new Template();
		template.setId(id);
		template.setType(Template.Type.valueOf(type));
		template.setName(name);
		template.setTemplatePath(templatePath);
		template.setStaticPath(staticPath);
		template.setDescription(description);
		return template;
	}

}