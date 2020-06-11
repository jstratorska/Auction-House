package com.aubgteam.auctionhouse.Services;

import com.aubgteam.auctionhouse.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class MailService {

    @Autowired
    private JavaMailSender sender;

    public void sendEmailToAdmins(List<User> admins, long id) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            for (User admin : admins) {
                helper.setTo(admin.getEmail());
            }
            helper.setFrom("softwareaubg@gmail.com");
            helper.setSubject("New Item Added");
            helper.setText("A new item was added. \n" +
                    "<html><body><a href='http://localhost:8080/admin/details/" + id + "'>Click to view it </a></body></html>", true);

            sender.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

