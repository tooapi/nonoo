package com.fdp.nonoo.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fdp.nonoo.common.LogConfig;
import com.fdp.nonoo.service.LogConfigService;

@Service("logConfigService")
public class LogConfigServiceImpl implements LogConfigService {
	@Cacheable({ "logConfig" })
	public List<LogConfig> getAll() {
		try {
			File file = new ClassPathResource("/template.xml").getFile();
			Document document = new SAXReader().read(file);
			@SuppressWarnings("unchecked")
			List<Element> nodes = document.selectNodes("/nonoo/logConfig");
			List<LogConfig> configs = new ArrayList<LogConfig>();
			Iterator<Element> iterator = nodes.iterator();
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				String operation = element.attributeValue("operation");
				String urlPattern = element.attributeValue("urlPattern");
				LogConfig logConfig = new LogConfig();
				logConfig.setOperation(operation);
				logConfig.setUrlPattern(urlPattern);
				configs.add(logConfig);
			}
			return configs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}