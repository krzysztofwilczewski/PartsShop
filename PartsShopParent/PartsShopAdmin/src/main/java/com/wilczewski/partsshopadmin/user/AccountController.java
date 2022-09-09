package com.wilczewski.partsshopadmin.user;

import com.wilczewski.partsshopadmin.configuration.ShopUserDetails;
import com.wilczewski.partsshopcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {

    private UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }




    @GetMapping("/account")
    public String userDetails(@AuthenticationPrincipal ShopUserDetails loggedUser, Model model){

        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);

        model.addAttribute("user", user);

        return "account";
    }

    @PostMapping("/account/update")
    public String saveUpdatedUser(User user, RedirectAttributes redirectAttributes, @AuthenticationPrincipal ShopUserDetails loggedUser) {
        userService.updateAccount(user);

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "Dane zostały zmodyfikowane");

        return "redirect:/account";

    }

}
