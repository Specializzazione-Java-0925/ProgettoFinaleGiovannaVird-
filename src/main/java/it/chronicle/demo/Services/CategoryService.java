package it.chronicle.demo.Services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import it.chronicle.demo.Models.Category;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.multipart.MultipartFile;

import it.chronicle.demo.Dtos.CategoryDto;
import it.chronicle.demo.Repositories.CategoryRepository;

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
    public CategoryDto create(Category model, Principal principal, MultipartFile multipartFile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public CategoryDto update(Long key, Category model, MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
