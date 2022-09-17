package com.wilczewski.partsshopmain.category;

import com.wilczewski.partsshopcommon.entity.Category;
import com.wilczewski.partsshopcommon.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

      public List<Category> listNoChildrenCategories();

      public Category getCategory(String alias) throws CategoryNotFoundException;

      public List<Category> getCategoryParents(Category child);

}
