package com.fdp.nonoo.common;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;

public class DateEditor extends PropertyEditorSupport {
	private static final String defautlDateFormat = "yyyy-MM-dd HH:mm:ss";
	private boolean emptyAsNull;
	private String dateFormat = defautlDateFormat;

	public DateEditor(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	public DateEditor(boolean emptyAsNull, String dateFormat) {
		this.emptyAsNull = emptyAsNull;
		this.dateFormat = dateFormat;
	}

	public String getAsText() {
		Date localDate = (Date) getValue();
		return ((localDate != null) ? new SimpleDateFormat(dateFormat)
				.format(localDate) : "");
	}

	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String str = text.trim();
			if ((emptyAsNull) && ("".equals(str)))
				setValue(null);
			else
				try {
					setValue(DateUtils.parseDate(str,
							CommonAttributes.DATE_PATTERNS));
				} catch (ParseException localParseException) {
					setValue(null);
				}
		}
	}
}