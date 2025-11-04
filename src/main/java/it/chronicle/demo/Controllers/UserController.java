package it.chronicle.demo.Controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.chronicle.demo.Dtos.ArticleDto;
import it.chronicle.demo.Dtos.UserDto;
import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Models.User;
import it.chronicle.demo.Repositories.ArticleRepository;
import it.chronicle.demo.Repositories.CareerRequestRepository;
import it.chronicle.demo.Services.ArticleService;
import it.chronicle.demo.Services.CategoryService;
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
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CareerRequestRepository careerRequestRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping("/")
    public String home(Model viewModel) {

        // List<ArticleDto> articles = articleService.readAll();
         List<ArticleDto> articles = new ArrayList<ArticleDto>();
        for(Article article : articleRepository.findByIsAcceptedTrue()){
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        List<ArticleDto> lastThreeArticles = articles.stream().limit(3).collect(Collectors.toList());

        viewModel.addAttribute("articles", lastThreeArticles);

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
    
    @GetMapping("/search/{id}")
    public String userArticleSearch(@PathVariable("id") Long id ,Model viewModel) {
        User user = userService.find(id);
        viewModel.addAttribute("title", "Tutti gli articoli per autore" + user.getUsername());

        List<ArticleDto> articles = articleService.searchByAuthor(user);
         List<ArticleDto> acceptedArticles = articles.stream().filter(article -> Boolean.TRUE.equals(article.getIsAccepted())).collect(Collectors.toList());
        viewModel.addAttribute("articles", acceptedArticles);
        return "article/articles";
    }
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model viewmodel) {
        viewmodel.addAttribute("title", "Richieste ricevute");
        viewmodel.addAttribute("requests", careerRequestRepository.findByIsCheckedFalse());
        viewmodel.addAttribute("categories", categoryService.readAll());
        return "admin/dashboard";
    }

    @GetMapping("/revisor/dashboard")
    public String revisorDashboard(Model viewModel) {
        viewModel.addAttribute("title", "Articoli da revisionare");
        viewModel.addAttribute("articles", articleRepository.findByIsAcceptedIsNull());
        return "revisor/dashboard";
    }
    
    @GetMapping("/writer/dashboard")
    public String writerDashboard(Model viewmodel, Principal principal) {
        viewmodel.addAttribute("title", "I tuoi articoli");

        List<ArticleDto> userArticles = articleService.readAll()
                                                      .stream()
                                                      .filter(article->article.getUser().getEmail().equals(principal.getName()))
                                                      .toList();
        viewmodel.addAttribute("articles", userArticles);
        return "writer/dashboard";

    }
    

    
    
}
