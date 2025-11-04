package it.chronicle.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Models.Category;
import it.chronicle.demo.Models.User;

public interface ArticleRepository extends ListCrudRepository<Article,Long> {

    List<Article>findByCategory(Category category);
    List<Article>findByUser(User user);
    List<Article> findByIsAcceptedTrue();
    List<Article> findByIsAcceptedFalse();
    List<Article> findByIsAcceptedIsNull();
    
    // @Query("SELECT a FROM a WHERE " + 
    // "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR" +
    // "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR" + 
    // "LOWER(a.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR" + 
    // "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    // List<Article> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT a FROM Article a WHERE " +
       "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
List<Article> search(@Param("searchTerm") String searchTerm);

}
