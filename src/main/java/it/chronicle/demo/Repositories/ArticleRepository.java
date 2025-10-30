package it.chronicle.demo.Repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Models.Category;
import it.chronicle.demo.Models.User;

public interface ArticleRepository extends ListCrudRepository<Article,Long> {

    List<Article>findByCategory(Category category);
    List<User>findByUser(User user);
}
