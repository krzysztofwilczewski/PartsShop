package com.wilczewski.partsshopadmin.user;


import com.wilczewski.partsshopcommon.entity.Role;
import com.wilczewski.partsshopcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String firstPage(Model model) {
        return listByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/users/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, Model model,
                             String sortField, String sortDir, String keyword){

        Page<User> page = userService.listByPage(pageNumber, sortField, sortDir, keyword);
        List<User> allUsers = page.getContent();

        long startCount = (pageNumber-1) * UserServiceImp.USERS_PER_PAGE+1;
        long stopCount = startCount + UserServiceImp.USERS_PER_PAGE-1;
        if (stopCount > page.getTotalElements()) {
            stopCount = page.getTotalElements();
        }

        String reverseSort = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("users", allUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("keyword", keyword);

        return "users";

    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> roles = roleService.allRoles();
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        model.addAttribute("title", "NOWY U??YTKOWNIK");
        return "new_user";
    }

    @PostMapping("/user/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "Zapisano nowego u??ytkownika");

        return redirectURLtoSavedUser(user);

    }

    private String redirectURLtoSavedUser(User user) {
        String partOfEmail = user.getEmail().split("@")[0];
        return "redirect:/admin/users/page/1?sortField=id&sortDir=asc&keyword=" + partOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            User user = userService.get(id);
            List<Role> roles = roleService.allRoles();
            model.addAttribute("user", user);
            model.addAttribute("title", "EDYCJA U??YTKOWNIKA (ID: " + id + ")");
            model.addAttribute("roles", roles);
            return "new_user";
        } catch (UserNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/admin/users";
        }

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "U??ytkownik z ID " + id + " zosta?? pomy??lnie usuni??ty");
        } catch (UserNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());

        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledUserStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? " aktywny" : " nieaktywny";
        String message = "U??ytkownik o ID " + id + " jest" + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/users";
    }

}
