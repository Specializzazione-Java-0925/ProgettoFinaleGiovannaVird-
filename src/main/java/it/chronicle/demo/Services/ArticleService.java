package it.chronicle.demo.Services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.chronicle.demo.Dtos.ArticleDto;
import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Models.User;
import it.chronicle.demo.Models.Category;
import it.chronicle.demo.Repositories.ArticleRepository;
import it.chronicle.demo.Repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private UserRepository userRepository;
    @Autowired 
    private ModelMapper modelMapper;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ImageService imageService;

    @Override
    public List<ArticleDto> readAll() {
       List<ArticleDto> dtos = new ArrayList<ArticleDto>();
       for(Article article : articleRepository.findAll()){
        dtos.add(modelMapper.map(article, ArticleDto.class));
       }
       return dtos;
    }

    @Override
    public ArticleDto read(Long key) {
       Optional<Article> optArticle = articleRepository.findById(key);
       if(optArticle.isPresent()){
        return modelMapper.map(optArticle.get(), ArticleDto.class);
       }else{
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autore id = " + key + " non trovato" );
        
       }
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {

        String url = "";

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if(authentication != null){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (userRepository.findById(userDetails.getId())).get();
        article.setUser(user);
      }

      if(!file.isEmpty()){
        try{
            CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
            url = futureUrl.get();
        }catch(Exception e){
          System.out.println("il path non c'è");
            e.printStackTrace();
        }
      }

      article.setIsAccepted(null);

      ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);
      if(!file.isEmpty()){
        imageService.saveImageOnDB(url, article);
      }
      return dto;
    }


@Override
public ArticleDto update(Long key, Article updatedArticle, MultipartFile file) {

    Article existingArticle = articleRepository.findById(key)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Articolo id = " + key + " non trovato"));

    existingArticle.setTitle(updatedArticle.getTitle());
    existingArticle.setSubtitle(updatedArticle.getSubtitle());
    existingArticle.setBody(updatedArticle.getBody());
    existingArticle.setPublishDate(updatedArticle.getPublishDate());
    existingArticle.setCategory(updatedArticle.getCategory()); 
    


    if (file != null && !file.isEmpty()) {
        String url = "";
        try {
            if (existingArticle.getImage() != null) {
                imageService.deleteImage(existingArticle.getImage().getPath());
            }

            CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
            url = futureUrl.get();
            
            imageService.saveImageOnDB(url, existingArticle);
            
            existingArticle.setIsAccepted(null); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    

    if (file == null || file.isEmpty()) {
        if (existingArticle.equals(updatedArticle) == false) {
            existingArticle.setIsAccepted(null);
        }
    }

    return modelMapper.map(articleRepository.save(existingArticle), ArticleDto.class);
}
    
    @Override
    public void delete(Long key) {
       if(articleRepository.existsById(key)){
        Article article = articleRepository.findById(key).get();

        try{
          String path = article.getImage().getPath();
          article.getImage().setArticle(null);
          imageService.deleteImage(path);
        } catch(Exception e){
          e.printStackTrace();
        }
        articleRepository.deleteById(key);
       }else{
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
       }
    }
    
    public List<ArticleDto> searchByCategory(Category category){
      List<ArticleDto> dtos = new ArrayList<ArticleDto>();
      for(Article article : articleRepository.findByCategory(category)){
        dtos.add(modelMapper.map(article, ArticleDto.class));
      }
      return dtos;
    }

    public List<ArticleDto>searchByAuthor(User user){
      List<ArticleDto> dtos = new ArrayList<ArticleDto>();
      for(Article article : articleRepository.findByUser(user)){
        dtos.add(modelMapper.map(article, ArticleDto.class));
      }
      return dtos;
    }

    public void setIsAccepted(Boolean result, Long id){
      Article article = articleRepository.findById(id).get();
      article.setIsAccepted(result);
      articleRepository.save(article);
    }

    public List<ArticleDto> search(String keyword){
      List<ArticleDto> dtos = new ArrayList<ArticleDto>();
      for(Article article : articleRepository.search(keyword)){
        dtos.add(modelMapper.map(article, ArticleDto.class));
      }
      return dtos;
    }
}
