package it.chronicle.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Models.Category;
import it.chronicle.demo.Dtos.ArticleDto;
import it.chronicle.demo.Dtos.CategoryDto;
import it.chronicle.demo.Services.ArticleService;
import it.chronicle.demo.Services.CrudService;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;






@Controller
@RequestMapping("/articles")
public class ArticleController {
   

    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto, Category , Long> categoryService;

     @Autowired
    private ArticleService articleService;

    @GetMapping
    public String articlesIndex(Model viewModel) {
        viewModel.addAttribute("title", "Tutti gli articoli");

        List<ArticleDto> articles = articleService.readAll();

        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        viewModel.addAttribute("articles", articles);

        return "article/articles";
        
    }
    
    
    @GetMapping("/create")
    public String articleCreate(Model viewModel) {
        viewModel.addAttribute("title", "Crea un articolo");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "article/create";
    }
    
    @PostMapping
    public String articleStore(@Valid @ModelAttribute("article")
        Article article,
        BindingResult result,
        RedirectAttributes redirectAttributes,
        Principal principal,
        MultipartFile file,
        Model viewModel){
            if(result.hasErrors()){
                viewModel.addAttribute("title", "Crea un articolo");
                viewModel.addAttribute("article", article);
                viewModel.addAttribute("categories", categoryService.readAll());
                return "article/create";
            }

            articleService.create(article, principal, file);
            redirectAttributes.addFlashAttribute("successMessage", "Articolo aggiunto con successo!");

            return "redirect:/";
        }

        @GetMapping("details/{id}")
        public String detailArticle(@PathVariable("id") Long id , Model viwModel ) {
            viwModel.addAttribute("title", "Dettaglio articolo");
            viwModel.addAttribute("article", articleService.read(id));
            return "article/details";
        }
        
        
        
    }
    

