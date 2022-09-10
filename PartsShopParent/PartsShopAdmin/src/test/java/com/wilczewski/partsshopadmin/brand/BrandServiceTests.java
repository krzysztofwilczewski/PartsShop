package com.wilczewski.partsshopadmin.brand;

import com.wilczewski.partsshopcommon.entity.Brand;
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
public class BrandServiceTests {

    @MockBean
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImp brandServiceImp;

    @Test
    public void testCheckUniqueInNewReturnDuplicate(){
        Integer id = null;
        String name = "Bosch";
        Brand brand = new Brand();

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandServiceImp.checkUnique(id, name);
        assertThat(result).isEqualTo("DUPLIKACJA");

    }

    @Test
    public void testCheckUniqueInNewReturnOk(){
        Integer id = null;
        String name = "Pdf";

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result = brandServiceImp.checkUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditReturnDuplicate(){
        Integer id = 1;
        String name = "Bosch";
        Brand brand = new Brand();

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandServiceImp.checkUnique(1, "Bosch");
        assertThat(result).isEqualTo("DUPLIKACJA");
    }

    @Test
    public void testCheckUniqueInEditReturnOk(){
        Integer id = 1;
        String name = "Bosch";
        Brand brand = new Brand();

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result = brandServiceImp.checkUnique(1, "Bosch");
        assertThat(result).isEqualTo("OK");
    }

}
