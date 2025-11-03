package it.chronicle.demo.Services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Models.Category;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.chronicle.demo.Dtos.CategoryDto;
import it.chronicle.demo.Repositories.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryService implements CrudService<CategoryDto, Category, Long> {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDto> readAll() {
       List<CategoryDto> dtos = new ArrayList<CategoryDto>();
       for(Category category: categoryRepository.findAll()){
        dtos.add(modelMapper.map(category, CategoryDto.class));
       }
       return dtos;
    }

    @Override
    public CategoryDto read(Long key) {
       return modelMapper.map(categoryRepository.findById(key), CategoryDto.class);
    }

    @Override
    public CategoryDto create(Category model, Principal principal, MultipartFile File) {
       return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
    }

    @Override
    public CategoryDto update(Long key, Category model, MultipartFile file) {
       if(categoryRepository.existsById(key)){
        model.setId(key);
        return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
       }else{
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
       }
    }

    @Override
    @Transactional
    public void delete(Long key) {
        if(categoryRepository.existsById(key)){
           Category category = categoryRepository.findById(key).get();

           if(category.getArticles() != null){
            Iterable<Article> articles = category.getArticles();
            for(Article article : articles){
               article.setCategory(null);
            }
           }

           categoryRepository.deleteById(key);
        }else{
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    
}
