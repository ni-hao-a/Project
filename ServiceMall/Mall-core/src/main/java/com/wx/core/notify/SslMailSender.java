package com.wx.core.notify;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 支持SSL邮件发送
 * 
 * @author CHENBO
 *
 */
@Slf4j
public class SslMailSender {


	private String fromAddress;
	private String fromName;
	private String toAddress;
	private String host;
	private String port;
	private String userName;
	private String password;
	private String sslEnabled;

	/**
	 * 默认发送邮件的核心功能实现
	 */
	public boolean sendMails(String subject, String content) {

		String[] toAddressList = toAddress.split(";");// 可能分号分隔了多个用户

		String messageId = EmailHelper.sendHtml(host, Integer.valueOf(port), userName, password,
				"true".equals(sslEnabled), fromAddress, fromName, toAddressList, subject, content);
		log.info("邮件发送完成，messageId: {" + messageId + "}");
		if (StringUtils.isNotBlank(messageId)) {
			return true;
		} else {
			return false;
		}
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(String sslEnabled) {
		this.sslEnabled = sslEnabled;
	}


	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

}
