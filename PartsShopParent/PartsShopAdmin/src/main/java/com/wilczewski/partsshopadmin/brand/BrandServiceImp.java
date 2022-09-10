package com.wilczewski.partsshopadmin.brand;

import com.wilczewski.partsshopcommon.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandServiceImp implements BrandService{

    public static final int BRANDS_PER_PAGE = 4;

    private BrandRepository brandRepository;

    @Autowired
    public BrandServiceImp(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    @Override
    public Page<Brand> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber-1, BRANDS_PER_PAGE, sort);

        if (keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }

        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand get(Integer id) throws BrandNotFoundException {
        try{
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new BrandNotFoundException("Nie można znaleźć producenta o ID " + id);
        }
    }

    @Override
    public void delete(Integer id) throws BrandNotFoundException {

        Long countById = brandRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Nie można znaleźć producenta o ID " + id);
        }
        brandRepository.deleteById(id);
    }

    @Override
    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id ==0);
        Brand brandByName = brandRepository.findByName(name);

        if (isCreatingNew){
            if (brandByName != null)
                return "DUPLIKACJA";
        } else {
            if (brandByName != null && brandByName.getId() != id) {
                return "DUPLIKACJA";
            }
        }
        return "OK";
    }

}
