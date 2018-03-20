package com.kinstalk.satellite.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
/**
 * Created by zhangchuanqi on 16/9/1.
 */
public class SendMail {
    private MailSender mailSender;

    private Logger logger = LoggerFactory.getLogger(SendMail.class);

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Boolean sendMail(String from, String[] to, String subject, String msg) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(msg);
            mailSender.send(message);
            return true;
        }catch (Exception e){
            logger.info(e.getMessage());
            return false;

        }

    }

}
