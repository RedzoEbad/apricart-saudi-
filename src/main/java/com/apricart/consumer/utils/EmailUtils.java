package com.apricart.consumer.utils;


import com.apricart.consumer.emailtemplates.*;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.service.EmailService;
import com.apricart.consumer.service.Impl.CustomerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

@Component
public class EmailUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    EmailService emailService;
    @Autowired
    private Environment environment;
    private String salesEmail;
    private String baseURL;
    public static String SUBJECT = "";
    private static final String imagePath = "/options/stream/alsaudia.png";
    private String imageURL = "";
    @PostConstruct
    public void init() {
        this.baseURL = environment.getProperty("server.consumer.baseurl");
        this.salesEmail = environment.getProperty("email.sales");
        this.imageURL = baseURL + imagePath;
    }

    public void sendEmail(Customer customer, String subject, EmailTemplate emailTemplate) throws Exception {
        try {
            String[] emails = {customer.getEmail()};
            emailService.sendHtmlMessage(emails, subject, emailTemplate.getMessage());
            LOGGER.info("Email sent to {}", customer.getEmail());
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email: {}", e.getMessage(), e);
            throw new MessagingException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendEmail(String email, String subject, EmailTemplate emailTemplate) throws Exception {
        try {
            emailService.sendHtmlMessage(email, subject, emailTemplate.getMessage());
            LOGGER.info("Email sent to {}", email);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email: {}", e.getMessage(), e);
            throw new MessagingException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendPasswordResetEmail(Customer customer) throws Exception {
        PasswordResetTemplate passwordResetTemplate = new PasswordResetTemplate(customer.getName(),imageURL);
        sendEmail(customer, SUBJECT, passwordResetTemplate);
    }
    public void sendForgotPasswordEmail(Customer customer) throws Exception {
        ForgotPasswordTemplate forgotPasswordTemplate = new ForgotPasswordTemplate(customer.getName(),imageURL);
        sendEmail(customer, SUBJECT, forgotPasswordTemplate);
    }
    public void sendOrderCancelEmail(Orders orders) throws Exception {
        OrderCancelTemplate orderCancelTemplate = new OrderCancelTemplate(orders,imageURL);
        sendEmail(orders.getCustomer(), SUBJECT, orderCancelTemplate);
        sendEmail(salesEmail, SUBJECT, orderCancelTemplate);
    }

    public void sendAddOrderEmail(Orders orders) throws Exception {
        OrderAddTemplate orderAddTemplate = new OrderAddTemplate(orders,imageURL);
        sendEmail(orders.getCustomer(), SUBJECT, orderAddTemplate);
        sendEmail(salesEmail, SUBJECT, orderAddTemplate);
    }
    public void sendFeedBackSubmitEmail(String email, String name) throws Exception {
        FeedbackSubmissionTemplate feedbackSubmissionTemplate = new FeedbackSubmissionTemplate(name, imageURL);
        sendEmail(email, SUBJECT, feedbackSubmissionTemplate);
    }
    public void sendOTPEmail(String email, String customerName, String otp) throws Exception {
        OTPTemplate otpTemplate = new OTPTemplate(customerName,imageURL, otp);
        sendEmail(email, SUBJECT, otpTemplate);
    }
}
