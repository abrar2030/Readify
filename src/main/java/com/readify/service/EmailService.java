<<<<<<< HEAD
package com.readify.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendEmail(String to, String subject, String message) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(to);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);
    this.mailSender.send(mailMessage);
  }
}
=======
package com.readify.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendEmail(String to, String subject, String message) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(to);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);
    this.mailSender.send(mailMessage);
  }
}
>>>>>>> 834bc75 (Update ReadMe File)
