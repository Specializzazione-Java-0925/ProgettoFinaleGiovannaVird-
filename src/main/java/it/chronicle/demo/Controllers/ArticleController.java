package it.chronicle.demo.Controllers;

import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.chronicle.demo.Dtos.CategoryDto;
import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Services.CrudService;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto, Category , Long> categoryService;
    
    @GetMapping("create")
    public String articleCreate(Model viewModel) {
        viewModel.addAttribute("title", "Crea un articolo");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "article/create";
    }
    
}
