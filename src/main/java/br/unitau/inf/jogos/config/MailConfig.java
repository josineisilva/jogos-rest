package br.unitau.inf.jogos.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	@Value("${mail.smtp.host}")
    private String host;
	@Value("${mail.smtp.port}")
    private Integer port;
    @Value("${mail.smtp.user}")
    private String user;
    @Value("${mail.smtp.password}")
    private String password;
    
	@Bean
    public JavaMailSender javaMailSender() { 
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.auth", true);
		mailProperties.put("mail.smtp.starttls.enable", true);
	    mailProperties.put("mail.smtp.starttls.required", true);
		sender.setJavaMailProperties(mailProperties);
		sender.setProtocol("smtp");
		sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(user);
        sender.setPassword(password);
        return sender;
    }
}