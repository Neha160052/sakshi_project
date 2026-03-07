package com.ttn.ecommerceProject.ttnEcommerceProject.service.emailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void sendActivationEmail(String toEmail, String text,  String token) {
        String subject = "Activate your account";
        String activationLink = baseUrl + "/auth/activate?token=" + token;

        String body = """
                Account Activation

                Copy this link and paste it in Postman or browser:

                %s

                Postman request:
                GET %s
                """.formatted(activationLink, activationLink);

        sendMail(toEmail, subject, body);
    }

    @Override
    public void sendResetPasswordEmail(String toEmail, String token) {
        String subject = "Reset your password";
        String resetLink = baseUrl + "/auth/reset-password-page?token=" + token;

        String body = """
                <html>
                    <body>
                        <h3>Password Reset</h3>
                        <p>Please click the link below to reset your password:</p>
                        <a href="%s">Reset Password</a>
                    </body>
                </html>
                """.formatted(resetLink);

        sendHtmlMail(toEmail, subject, body);
    }

    private void sendMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    private void sendHtmlMail(String toEmail, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}