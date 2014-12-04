package com.fdp.nonoo.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fdp.nonoo.Application;
import com.fdp.nonoo.common.Template;
import com.fdp.nonoo.entity.SafeKey;
import com.fdp.nonoo.service.MailService;
import com.fdp.nonoo.service.TemplateService;

@Service("mailService")
public class MailServiceImpl implements MailService {

	@Resource(name = "javaMailSender")
	private JavaMailSenderImpl javaMailSender;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	@Resource(name = "templateService")
	private TemplateService templateService;

	private void asyncSend(MimeMessage mimeMessage) {
		try {
			taskExecutor.execute(new Runn(mimeMessage));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(String smtpFromMail, String smtpHost, Integer smtpPort,
			String smtpUsername, String smtpPassword, String toMail,
			String subject, String templatePath, Map<String, Object> model,
			boolean async) {
		Assert.hasText(smtpFromMail);
		Assert.hasText(smtpHost);
		Assert.notNull(smtpPort);
		Assert.hasText(smtpUsername);
		Assert.hasText(smtpPassword);
		Assert.hasText(toMail);
		Assert.hasText(subject);
		Assert.hasText(templatePath);
		try {

			
			String content ="test";
			javaMailSender.setHost(smtpHost);
			javaMailSender.setPort(smtpPort.intValue());
			javaMailSender.setUsername(smtpUsername);
			javaMailSender.setPassword(smtpPassword);
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mimeMessage, false, "utf-8");
			messageHelper.setFrom(MimeUtility.encodeWord("笑喷网") + " <"
					+ smtpFromMail + ">");
			messageHelper.setSubject(subject);
			messageHelper.setTo(toMail);
			messageHelper.setText(content, true);
			if (async) {
				asyncSend(mimeMessage);
				return;
			}
			javaMailSender.send(mimeMessage);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void send(String toMail, String subject, String templatePath,
			Map<String, Object> model, boolean async) {

		send(Application.getMailSmtpUsername(), Application.getMailSmtpHost(),
				Application.getMailSmtpPort(),
				Application.getMailSmtpUsername(),
				Application.getMailSmtpPassword(), toMail, subject,
				templatePath, model, async);
	}

	public void send(String toMail, String subject, String templatePath,
			Map<String, Object> model) {
		send(toMail, subject, templatePath, model, true);
	}

	public void send(String toMail, String subject, String templatePath) {
		send(toMail, subject, templatePath, null, true);
	}

	public void sendTestMail(String smtpFromMail, String toMail) {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("base", "abc");
		send(toMail, "测试", "common/testmail.jhtml", model, false);
	}

	public void send(String smtpFromMail, String smtpHost, Integer smtpPort,
			String smtpUsername, String smtpPassword, String[] toEmail,
			String subject, String[] cc, String content, String attachments[],
			boolean async) {
		Assert.hasText(smtpFromMail);
		Assert.hasText(smtpHost);
		Assert.notNull(smtpPort);
		Assert.hasText(smtpUsername);
		Assert.hasText(smtpPassword);
		try {

			javaMailSender.setHost(smtpHost);
			javaMailSender.setPort(Integer.valueOf(smtpPort));
			javaMailSender.setUsername(smtpUsername);
			javaMailSender.setPassword(smtpPassword);
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mimeMessage, false, "utf-8");
			messageHelper.setFrom(" <" + smtpFromMail + ">");
			messageHelper.setSubject(subject);
			messageHelper.setTo(toEmail);
			messageHelper.setCc(cc);
			Multipart multipart = new MimeMultipart();
			addContent(content, multipart);
			if (attachments != null) {
				try {
					addAttachments(attachments, multipart);
				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();
				}
				mimeMessage.setContent(multipart);
			}
			if (async) {
				asyncSend(mimeMessage);
				return;
			}
			javaMailSender.send(mimeMessage);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void addContent(String content, Multipart multipart)
			throws MessagingException {

		MimeBodyPart body = new MimeBodyPart();
		body.setText(content);
		multipart.addBodyPart(body);

	}

	public void addAttachments(String attachments[], Multipart multipart)
			throws MessagingException, UnsupportedEncodingException {
		for (int i = 0; i < attachments.length; i++) {
			if (attachments[i] != null) {
				MimeBodyPart mailArchieve = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(attachments[i]);
				mailArchieve.setDataHandler(new DataHandler(fds));
				mailArchieve.setFileName(MimeUtility.encodeText(fds.getName(),
						"UTF-8", "B"));
				multipart.addBodyPart(mailArchieve);
			}
		}
	}

	public void sendFindPasswordMail(String toMail, String username,
			SafeKey safeKey) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("username", username);
		model.put("safeKey", safeKey.getValue());
		
		String subject="密码找回";
		Template template = templateService.get("findPasswordMail");
		send(toMail,subject, template.getTemplatePath(),model);

	}

	class Runn implements Runnable {

		private MimeMessage mimeMessage;

		public Runn(MimeMessage mimeMessage) {
			this.mimeMessage = mimeMessage;
		}

		public void run() {
			javaMailSender.send(mimeMessage);

		}
	}

}
