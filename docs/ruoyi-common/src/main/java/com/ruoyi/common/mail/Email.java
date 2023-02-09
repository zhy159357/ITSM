package com.ruoyi.common.mail;

import lombok.Data;

@Data
public class Email {
	private String[] users;
	private String subject;
	private String content;
}
