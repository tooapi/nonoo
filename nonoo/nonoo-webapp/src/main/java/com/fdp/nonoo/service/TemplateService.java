package com.fdp.nonoo.service;

import java.util.List;

import com.fdp.nonoo.common.Template;

public abstract interface TemplateService {
	public abstract List<Template> getAll();

	public abstract List<Template> getList(Template.Type type);

	public abstract Template get(String id);

	public abstract String read(String id);

	public abstract String read(Template template);

	public abstract void write(String id, String content);

	public abstract void write(Template template, String content);

}