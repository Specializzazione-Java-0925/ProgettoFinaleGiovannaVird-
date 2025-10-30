package it.chronicle.demo.Dtos;

import java.time.LocalDate;

import it.chronicle.demo.Models.Category;
import it.chronicle.demo.Models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDate publishDate;
    private User user;
    private Category category;
    
}
