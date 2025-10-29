package it.chronicle.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.chronicle.demo.Dtos.UserDto;
import it.chronicle.demo.Models.User;
import it.chronicle.demo.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;







@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "home";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes,
        HttpServletRequest request,
        HttpServletResponse response) {
        
            User existingUser = userService.finUserByEmail(userDto.getEmail());
        
            if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
                result.rejectValue("email", null, "esiste già un account con questa email");
            }

            if(result.hasErrors()){
                model.addAttribute("user", userDto);
                return "auth/register";
            }
            userService.saveUser(userDto, redirectAttributes, request, response);
            redirectAttributes.addFlashAttribute("successMessage", "Restrazione avvenuta con successo!");

        return "redirect:/";
    }
    
    
    
}
