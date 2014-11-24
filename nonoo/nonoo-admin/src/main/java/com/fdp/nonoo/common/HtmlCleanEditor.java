package com.fdp.nonoo.common;

import java.beans.PropertyEditorSupport;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HtmlCleanEditor extends PropertyEditorSupport {
	private boolean trim;
	private boolean emptyAsNull;
	private Whitelist whitelist = Whitelist.none();

	public HtmlCleanEditor(boolean trim, boolean emptyAsNull) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
	}

	public HtmlCleanEditor(boolean trim, boolean emptyAsNull,
			Whitelist whitelist) {
		this.trim = trim;
		this.emptyAsNull = emptyAsNull;
		this.whitelist = whitelist;
	}

	public String getAsText() {
		Object object = getValue();
		return ((object != null) ? object.toString() : "");
	}

	public void setAsText(String text) {
		if (text != null) {
			String value = (this.trim) ? text.trim() : text;
			value = Jsoup.clean(value, this.whitelist);
			if ((this.emptyAsNull) && ("".equals(value))) {
				value = null;
			}
			setValue(value);
		} else {
			setValue(null);
		}
	}
}