package com.wilczewski.partsshopadmin.manufacturer;

import com.wilczewski.partsshopcommon.entity.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManufacturerServiceImp implements ManufacturerService{

    public static final int ROOT_MANUFACTURERS_PER_PAGE = 4;

    private ManufacturerRepository manufacturerRepository;

    @Autowired
    public ManufacturerServiceImp(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public List<Manufacturer> listByPage(ManufacturerPageInfo pageInfo, int pageNumber, String sortDir, String keyword) {
        Sort sort = Sort.by("name");


        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNumber-1, ROOT_MANUFACTURERS_PER_PAGE, sort);

        Page<Manufacturer> pageManufacturers = null;

        if (keyword != null && !keyword.isEmpty()){
            pageManufacturers = manufacturerRepository.search(keyword, pageable);
        } else {

            pageManufacturers = manufacturerRepository.findRootManufacturers(pageable);
        }

        List<Manufacturer> rootManufacturers = pageManufacturers.getContent();

        pageInfo.setTotalElements(pageManufacturers.getTotalElements());
        pageInfo.setTotalpages(pageManufacturers.getTotalPages());

        if (keyword != null && !keyword.isEmpty()){
            List<Manufacturer> searchResult = pageManufacturers.getContent();
            for (Manufacturer manufacturer : searchResult){
                manufacturer.setHasChildren(manufacturer.getChildren().size() > 0);
            }
            return searchResult;

        } else {
            return listHierarchicalManufacturers(rootManufacturers, sortDir);
        }
    }

    private List<Manufacturer> listHierarchicalManufacturers(List<Manufacturer> rootManufacturers, String sortDir) {
        List<Manufacturer> hierarchicalManufacturers = new ArrayList<>();

        for (Manufacturer rootManufacturer: rootManufacturers){
            hierarchicalManufacturers.add(Manufacturer.copyFull(rootManufacturer));

            Set<Manufacturer> children = sortSubManufacturers(rootManufacturer.getChildren(), sortDir);

            for (Manufacturer subManufacturer: children){
                String name = "--" + subManufacturer.getName();
                hierarchicalManufacturers.add(Manufacturer.copyFull(subManufacturer, name));

                listSubHierarchicalManufacturers(hierarchicalManufacturers, subManufacturer, 1, sortDir);
            }
        }

        return hierarchicalManufacturers;
    }


    private void listSubHierarchicalManufacturers(List<Manufacturer> hierarchicalManufactures ,Manufacturer parent, int subLevel, String sortDir){

        Set<Manufacturer> children = sortSubManufacturers(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Manufacturer subManufacturer : children){
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name += subManufacturer.getName();

            hierarchicalManufactures.add(Manufacturer.copyFull(subManufacturer, name));

            listSubHierarchicalManufacturers(hierarchicalManufactures, subManufacturer, newSubLevel, sortDir);
        }
    }

    public List<Manufacturer> listOfManufacturers() {

        List<Manufacturer> manufacturersUsedInList = new ArrayList<>();
        Iterable<Manufacturer> manufacturersInDb = manufacturerRepository.findRootManufacturers(Sort.by("name").ascending());

        for (Manufacturer manufacturer : manufacturersInDb){
            if (manufacturer.getParent() == null){
                manufacturersUsedInList.add(Manufacturer.copyIdAndName(manufacturer));

                Set<Manufacturer> children = sortSubManufacturers(manufacturer.getChildren());

                for (Manufacturer subManufacturer : children){
                    String name = "--" + subManufacturer.getName();
                    manufacturersUsedInList.add(Manufacturer.copyIdAndName(subManufacturer.getId(), name));

                    listChildren(manufacturersUsedInList, subManufacturer,1);
                }
            }
        }

        return manufacturersUsedInList;
    }

    private void listChildren(List<Manufacturer> manufacturersUsedInList ,Manufacturer parent, int level){
        int newLevel = level + 1;
        Set<Manufacturer> children = sortSubManufacturers(parent.getChildren());

        for (Manufacturer subManufacturer : children){
            String name = "";
            for (int i=0; i<newLevel; i++){
                name += "--";
            }
            name += subManufacturer.getName();
            manufacturersUsedInList.add(Manufacturer.copyIdAndName(subManufacturer.getId(), name));

            listChildren(manufacturersUsedInList ,subManufacturer, newLevel);
        }
    }

    private SortedSet<Manufacturer> sortSubManufacturers(Set<Manufacturer> children){
        return sortSubManufacturers(children, "asc");
    }

    private SortedSet<Manufacturer> sortSubManufacturers(Set<Manufacturer> children, String sortDir) {
        SortedSet<Manufacturer> sortedChildren = new TreeSet<>(new Comparator<Manufacturer>() {
            @Override
            public int compare(Manufacturer o1, Manufacturer o2) {
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

    public String checkUnique(Integer id, String name, String alias){

        boolean isCreatingNew = (id == null || id == 0);

        Manufacturer manufacturerByName = manufacturerRepository.findByName(name);

        if (isCreatingNew){
            if (manufacturerByName != null){
                return "DuplikacjaNazwy";
            } else {
                Manufacturer manufacturerByAlias = manufacturerRepository.findByAlias(alias);
                if(manufacturerByAlias != null) {
                    return "DuplikacjaAliasu";
                }
            }
        } else {
            if (manufacturerByName != null && manufacturerByName.getId() != id) {
                return "DuplikacjaNazwy";
            }
            Manufacturer manufacturerByAlias = manufacturerRepository.findByAlias(alias);
            if (manufacturerByAlias != null && manufacturerByAlias.getId() != id){
                return "DuplikacjaAliasu";
            }
        }

        return "OK";
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Override
    public Manufacturer get(Integer id) throws ManufacturerNotFoundException {
        try{
            return manufacturerRepository.findById(id).get();
        } catch(NoSuchElementException exception) {
            throw new ManufacturerNotFoundException("Nie można znaleźć auta o ID " + id);
        }
    }

    @Override
    public void delete(Integer id) throws ManufacturerNotFoundException {
        Long countById = manufacturerRepository.countById(id);

        if (countById == null || countById == 0){
            throw new ManufacturerNotFoundException("Nie można znaleźć auta o ID " + id);
        }
        manufacturerRepository.deleteById(id);
    }

}
