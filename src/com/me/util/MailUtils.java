package com.me.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * JavaMail发送邮件:前提是QQ邮箱里帐号设置要开启POP3/SMTP协议
 */
public class MailUtils {

	/**
	 * @Method: createSimpleMail
	 * @Description: 创建一封只包含文本的邮件
	 */

	public static void sendMail(String email, String emailMsg) throws Exception {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		// 发送邮件协议名称
		prop.setProperty("mail.transport.protocol", "smtp");
		// 开启debug调试，以便在控制台查看
		prop.setProperty("mail.debug", "true");
		// 设置邮件服务器主机名
		prop.setProperty("mail.host", "smtp.qq.com");
		// 发送服务器需要身份验证
		prop.setProperty("mail.smtp.auth", "true");
		// 开启SSL加密，否则会失败
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.socketFactory", sf);

		// 创建session
		Session session = Session.getInstance(prop);
	
		// 通过session得l到transport对象
		Transport ts = session.getTransport();
		// 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）
		ts.connect("smtp.qq.com", "1129812096", "wothdqikdmzigjba");
		// 创建邮件
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress("1129812096@qq.com"));
		// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				email));
		// 邮件的标题
		message.setSubject("请激活");
		// 邮件的文本内容
		message.setContent(emailMsg,"text/html;charset=utf-8");
		// 发送邮件
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
	}

 
}