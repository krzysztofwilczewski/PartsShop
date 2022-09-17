package com.wilczewski.partsshopmain.customer;

import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.exception.CustomerNotFoundException;
import com.wilczewski.partsshopmain.Utility;
import com.wilczewski.partsshopmain.setting.EmailSettingBag;
import com.wilczewski.partsshopmain.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    private CustomerService customerService;

    private SettingService settingService;

    @Autowired
    public ForgotPasswordController(CustomerService customerService, SettingService settingService) {
        this.customerService = customerService;
        this.settingService = settingService;
    }

    @GetMapping("/forgot_password")
    public String show(){
        return "forgot_password";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) {

        String email = request.getParameter("email");
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(link, email);

            model.addAttribute("message", "Link resetujacy hasło został wysłany na Twój email.");
        } catch (CustomerNotFoundException exception) {
            model.addAttribute("error", exception.getMessage());
        } catch (MessagingException | UnsupportedEncodingException exception) {
            model.addAttribute("error", "Nie można wysłać wiadomości");
        }


        return "forgot_password";
    }

    private void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = email;
        String subject = "Link do zresetowania hasła";

        String content = "<p>Witaj,</p>"
                + "<p>Wysłano prośbę o zresetowanie hasła.</p>"
                + "<p>Kliknij na poniższy link aby zresetować hasło:</p>"
                + "<p><a href=\"" + link + "\">Zmień moje hasło</a></p>"
                + "<br>"
                + "<p>Zignoruj tą wiadomość jeśli pamiętasz hasło lub"
                + " prośba o zmianę hasła nie była wysyłana</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model){

        Customer customer = customerService.getByResetPasswordToken(token);

        if (customer != null) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("pageTitle", "Nieprawidłowy Token");
            model.addAttribute("message", "Nieprawidłowy Token");
            return "message";
        }

        return "reset_password";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            customerService.updatePassword(token, password);

            model.addAttribute("pageTitle", "Zresetowane hasło");
            model.addAttribute("title", "Reset Twojego hasła");
            model.addAttribute("message", "Hasło zostało zmienione.");
            return "message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("pageTitle", "Nieprawidłowy Token");
            model.addAttribute("message", e.getMessage());
            return "message";
        }
    }

}
