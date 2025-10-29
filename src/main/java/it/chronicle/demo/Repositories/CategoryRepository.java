package it.chronicle.demo.Repositories;

import it.chronicle.demo.Models.Category; 
import org.springframework.data.repository.ListCrudRepository;

public interface CategoryRepository extends ListCrudRepository<Category , Long> {

}




