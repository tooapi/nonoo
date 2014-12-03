package com.fdp.nonoo.service.impl;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fdp.nonoo.service.CacheService;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

	@Resource(name = "ehCacheManager")
	private CacheManager ehCacheManager;

	@Resource(name = "messageSource")
	private ReloadableResourceBundleMessageSource messageSource;

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	public String getDiskStorePath() {
		return ehCacheManager.getConfiguration().getDiskStoreConfiguration().getPath();
	}

	public int getCacheSize() {
		int i = 0;
		String[] cacheNames = ehCacheManager.getCacheNames();
		if (cacheNames != null)
			for (String cacheName : cacheNames) {
				Ehcache ehcache = ehCacheManager.getEhcache(cacheName);
				if (ehcache == null)
					continue;
				i += ehcache.getSize();
			}
		return i;
	}

	@CacheEvict(value = { "setting", "authorization", "logConfig", "template",
			"shipping", "area", "seo", "adPosition", "memberAttribute",
			"navigation", "tag", "friendLink", "brand", "article",
			"articleCategory", "product", "productCategory", "review",
			"consultation", "promotion" }, allEntries = true)
	public void clear() {
		messageSource.clearCache();
//		try {
//			freeMarkerConfigurer.getConfiguration().setSharedVariable("setting", SettingUtils.get());
//		} catch (TemplateModelException e) {
//			e.printStackTrace();
//		}
		freeMarkerConfigurer.getConfiguration().clearTemplateCache();
	}
}