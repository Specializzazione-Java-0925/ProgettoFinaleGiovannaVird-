package it.chronicle.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.chronicle.demo.Dtos.UserDto;
import it.chronicle.demo.Models.User;
import it.chronicle.demo.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserServiceImp implements UserService {


    @Autowired
    private UserRepository userRepository;


    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    public void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
       User user = new User();
       user.setUsername(userDto.getFirstname()+ " " + userDto.getLastname());
       user.setEmail(userDto.getEmail());
       user.setPassword((passwordEncoder().encode(userDto.getPassword())));
       userRepository.save(user);
    }

    @Override
    public User finUserByEmail(String email) {
      return userRepository.findByEmail(email);
    }





}
