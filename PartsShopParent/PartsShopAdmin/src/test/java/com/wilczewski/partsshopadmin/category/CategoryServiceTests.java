package com.wilczewski.partsshopadmin.category;

import com.wilczewski.partsshopcommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImp categoryServiceImp;

    @Test
    public void testUniqueName(){
        Integer id = null;
        String name = "Elektryka";
        String alias = "abcde";

        Category category = new Category(id, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryServiceImp.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplikacjaNazwy");
    }

    @Test
    public void testUniqueAlias(){
        Integer id = null;
        String name = "takietam";
        String alias = "żarówki";

        Category category = new Category(id, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryServiceImp.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplikacjaAliasu");
    }

    @Test
    public void testUniqueReturnOK(){
        Integer id = null;
        String name = "Elektryka";
        String alias = "abcde";

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryServiceImp.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testUniqueReturnDuplicateName(){
        Integer id = 1;
        String name = "Elektryka";
        String alias = "żarówki";

        Category category = new Category(2, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryServiceImp.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplikacjaNazwy");
    }

    @Test
    public void testUniqueReturnDuplicateAlias(){
        Integer id = 1;
        String name = "takietam";
        String alias = "żarówki";

        Category category = new Category(2, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryServiceImp.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplikacjaAliasu");
    }

    @Test
    public void testUniqueOkReturnOK() {
        Integer id = 1;
        String name = "Elektryka";
        String alias = "abcde";

        Category category = new Category(id, name, alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryServiceImp.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");

    }

}
