package com.wilczewski.partsshopadmin.brand;

import com.wilczewski.partsshopcommon.entity.Brand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {

    public List<Brand> listAll();

    public Brand save(Brand brand);

    public Brand get(Integer id) throws BrandNotFoundException;

    public void delete(Integer id) throws BrandNotFoundException;

    public String checkUnique(Integer id, String name);

    public Page<Brand> listByPage(int pageNumber, String sortField, String sortDir, String keyword);

}
