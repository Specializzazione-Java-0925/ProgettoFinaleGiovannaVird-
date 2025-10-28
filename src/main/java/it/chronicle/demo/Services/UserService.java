package it.chronicle.demo.Services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.chronicle.demo.Dtos.UserDto;
import it.chronicle.demo.Models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response);
    User finUserByEmail(String email);
}
