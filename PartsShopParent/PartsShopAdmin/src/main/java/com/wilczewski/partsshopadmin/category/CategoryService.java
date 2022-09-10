package com.wilczewski.partsshopadmin.category;

import com.wilczewski.partsshopcommon.entity.Category;
import com.wilczewski.partsshopcommon.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

    // List<Category> listAll(String sortDir);

    List<Category> listByPage(CategoryPageInfo pageInfo, int pageNumber, String sortDir, String keyword);

    List<Category> listOfCategories();

    public Category save(Category category);

    public  Category get(Integer id) throws CategoryNotFoundException;

    public String checkUnique(Integer id, String name, String alias);

    public void updateCategoryEnabledStatus(Integer id, boolean enabled);

    public void delete(Integer id) throws CategoryNotFoundException;

 //   public List<Category> listNoChildrenCategories();

 //   public Category getCategory(String alias) throws CategoryNotFoundException;

 //   public List<Category> getCategoryParents(Category child);

}
