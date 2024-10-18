package com.ccsw.tutorial.category;
//TEST UNITARIO DE CATEGORY

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    // comprobar q el findAll devuelve todas las categorias
    @Test
    public void findAllShouldReturnAllCategories() {

        List<Category> list = new ArrayList<>();
        list.add(mock(Category.class));

        when(categoryRepository.findAll()).thenReturn(list);

        List<Category> categories = categoryService.findAll();

        assertNotNull(categories);
        assertEquals(1, categories.size());
    }

    public static final String CATEGORY_NAME = "CAT1";

    // comprobar el funcionamiento de guardado cuando el id es nulo
    @Test
    public void saveNotExistsCategoryIdShouldInsert() {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CATEGORY_NAME);

        ArgumentCaptor<Category> category = ArgumentCaptor.forClass(Category.class);

        categoryService.save(null, categoryDto);

        verify(categoryRepository).save(category.capture());

        assertEquals(CATEGORY_NAME, category.getValue().getName());
    }

    public static final Long EXISTS_CATEGORY_ID = 1L;       //comprobará el ID 1

    // comprueba modificacion de registro existente
    @Test
    public void saveExistsCategoryIdShouldUpdate() {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CATEGORY_NAME);

        Category category = mock(Category.class);       // objeto simulado
        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(category));

        categoryService.save(EXISTS_CATEGORY_ID, categoryDto);      // sustituye registro con id 1 por el nuevo registro dto

        verify(categoryRepository).save(category);
    }

    // se invoca delete y se comprueba que es invocado con el atributo correcto
    @Test
    public void deleteExistsCategoryIdShouldDelete() throws Exception {

        Category category = mock(Category.class);   // objeto simulado
        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(category));    // comprueba que existe en la BBDD

        categoryService.delete(EXISTS_CATEGORY_ID);     // el servicio intenta eliminar la categoria existente

        verify(categoryRepository).deleteById(EXISTS_CATEGORY_ID);  // verifica q la operacion del repositorio ha sido llamada por ese id
    }

    public static final Long NOT_EXISTS_CATEGORY_ID = 0L;

    // comprueba q se devuelva la categoria si buscamos por su id
    @Test
    public void getExistsCategoryIdShouldReturnCategory() {

        Category category = mock(Category.class);
        when(category.getId()).thenReturn(EXISTS_CATEGORY_ID);
        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(category));

        Category categoryResponse = categoryService.get(EXISTS_CATEGORY_ID);

        assertNotNull(categoryResponse);
        assertEquals(EXISTS_CATEGORY_ID, category.getId());
    }

    // comprueba que no se devuelva ninguna categoria si se busca por un id inexistente o nulo
    @Test
    public void getNotExistsCategoryIdShouldReturnNull() {

        when(categoryRepository.findById(NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());

        Category category = categoryService.get(NOT_EXISTS_CATEGORY_ID);

        assertNull(category);
    }

}
