package com.wilczewski.partsshopadmin.category;

import com.wilczewski.partsshopcommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryTests {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryTests(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Test
    public void testCreateMainCategory(){
        Category category = new Category("Klimatyzacja");
        Category saved = categoryRepository.save(category);
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(16);
        Category subCategory = new Category("Wycieraczki", parent);
        Category subCategory1 = new Category("Silnik wycieraczek", parent);
        Category subCategory2 = new Category("Pompka spryskiwacza", parent);
        Category subCategory3 = new Category("Ramię wycieraczki", parent);
        categoryRepository.saveAll(List.of(subCategory, subCategory1,subCategory2, subCategory3));

    }

    @Test
    public void testGetCategory(){
        Category category = categoryRepository.findById(1).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for (Category subCategory : children){
            System.out.println(subCategory.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintAllCategories(){
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories){
            if (category.getParent() == null){
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subCategory : children){
                    System.out.println("--"+ subCategory.getName());
                    printChildren(subCategory,1);
                }
            }
        }
    }

    private void printChildren(Category parent, int level){
        int newLevel = level + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children){
            for (int i=0; i<newLevel; i++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory, newLevel);
        }
    }

    @Test
    public void testListRootCategories(){
        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(category -> System.out.println(category.getName()));
    }
/*
    @Test
    public void testListEnabledCategories(){
        List<Category> categories = categoryRepository.findAllEnabled();
        categories.forEach(category -> {
            System.out.println(category.getName() + " (" + category.isEnabled() + ") ");
        });
    }

    @Test
    public void testFindCategoryByAlias(){
        String alias = "filtry";
        Category category = categoryRepository.findByAliasEnabled(alias);

        assertThat(category).isNotNull();

    }
    */
}
