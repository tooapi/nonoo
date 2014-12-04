package com.fdp.nonoo.service;

import com.fdp.nonoo.entity.SafeKey;

public abstract interface MailService {

	public abstract void sendTestMail(String smtpFromMail, String toMail);

	public abstract void sendFindPasswordMail(String toMail, String username,
			SafeKey safeKey);

}