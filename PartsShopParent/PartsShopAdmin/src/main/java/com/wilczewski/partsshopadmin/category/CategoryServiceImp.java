package com.wilczewski.partsshopadmin.category;

import com.wilczewski.partsshopcommon.entity.Category;
import com.wilczewski.partsshopcommon.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryServiceImp implements CategoryService{

    public static final int ROOT_CATEGORIES_PER_PAGE = 4;

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNumber, String sortDir, String keyword) {
        Sort sort = Sort.by("name");


        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNumber-1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> pageCategories = null;

        if (keyword != null && !keyword.isEmpty()){
            pageCategories = categoryRepository.search(keyword, pageable);
        } else {

            pageCategories = categoryRepository.findRootCategories(pageable);
        }

        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalpages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()){
            List<Category> searchResult = pageCategories.getContent();
            for (Category category : searchResult){
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return searchResult;

        } else {
            return listHierarchicalCategories(rootCategories, sortDir);
        }
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory: rootCategories){
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory: children){
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }


    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories ,Category parent, int subLevel, String sortDir){

        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children){
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }
    }

    @Override
    public List<Category> listOfCategories() {

        List<Category> categoriesUsedInList = new ArrayList<>();
        Iterable<Category> categoriesInDb = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDb){
            if (category.getParent() == null){
                categoriesUsedInList.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());

                for (Category subCategory : children){
                    String name = "--" + subCategory.getName();
                    categoriesUsedInList.add(Category.copyIdAndName(subCategory.getId(), name));

                    listChildren(categoriesUsedInList, subCategory,1);
                }
            }
        }

        return categoriesUsedInList;
    }

    private void listChildren(List<Category> categoriesUsedInList ,Category parent, int level){
        int newLevel = level + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category subCategory : children){
            String name = "";
            for (int i=0; i<newLevel; i++){
                name += "--";
            }
            name += subCategory.getName();
            categoriesUsedInList.add(Category.copyIdAndName(subCategory.getId(), name));

            listChildren(categoriesUsedInList ,subCategory, newLevel);
        }
    }

    @Override
    public Category save(Category category) {

        Category parent = category.getParent();

        if (parent != null) {
            String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += String.valueOf(parent.getId()) + "-";
            category.setAllParentIDs(allParentIds);
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        }catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Nie można znaleźć kategorii o ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias){

        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = categoryRepository.findByName(name);

        if (isCreatingNew){
            if (categoryByName != null){
                return "DuplikacjaNazwy";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if(categoryByAlias != null) {
                    return "DuplikacjaAliasu";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplikacjaNazwy";
            }
            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id){
                return "DuplikacjaAliasu";
            }
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children){
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                if (sortDir.equals("asc")) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }

    @Override
    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Nie można znaleźć kategorii o ID " + id);
        }
        categoryRepository.deleteById(id);
    }
/*
    @Override
    public List<Category> listNoChildrenCategories() {
        List<Category> listNoChildrenCategories = new ArrayList<>();

        List<Category> listEnabledCategories = categoryRepository.findAllEnabled();

        listEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.size() == 0) {
                listNoChildrenCategories.add(category);
            }
        });

        return listNoChildrenCategories;
    }

    @Override
    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findByAliasEnabled(alias);
        if (category == null) {
            throw new CategoryNotFoundException("Nie znaleziono kategorii " + alias);
        }
        return category;
    }

    @Override
    public List<Category> getCategoryParents(Category child) {

        List<Category> listParents = new ArrayList<>();

        Category parent = child.getParent();

        while (parent != null) {
            listParents.add(0, parent);
            parent = parent.getParent();
        }

        listParents.add(child);

        return listParents;
    }
*/
}
