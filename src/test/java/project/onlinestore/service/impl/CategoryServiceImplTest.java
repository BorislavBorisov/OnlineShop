package project.onlinestore.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.repository.CategoryRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private CategoryServiceImpl serviceToTest;

    @Mock
    private CategoryRepository mockCategoryRepo;

    private final ModelMapper modelMapper = new ModelMapper();

    CategoryEntity categoryEntity;

    @BeforeEach
    void init() {
        serviceToTest = new CategoryServiceImpl(modelMapper, mockCategoryRepo);

        categoryEntity = new CategoryEntity();
        categoryEntity.setName("TestCategory")
                .setImgUrl("some address")
                .setPosition(1)
                .setId(1L);
    }

    @Test
    void test_FindCategoryByName_Correct() {

        when(mockCategoryRepo.findByName(categoryEntity.getName()))
                .thenReturn(Optional.of(categoryEntity));

        var actual = serviceToTest.findCategoryByName(categoryEntity.getName());

        assertEquals(categoryEntity.getName(), actual.getName());

    }

    @Test
    void test_getCategoryById_Ok() {
        when(mockCategoryRepo.findById(categoryEntity.getId()))
                .thenReturn(Optional.of(categoryEntity));

        var actual = serviceToTest.findCategoryById(categoryEntity.getId());
        assertEquals(actual.getName(), categoryEntity.getName());
        assertEquals(actual.getPosition(), categoryEntity.getPosition());
        assertEquals(actual.getImgUrl(), categoryEntity.getImgUrl());
        assertEquals(actual.getId(), categoryEntity.getId());
        System.out.println();
    }

}