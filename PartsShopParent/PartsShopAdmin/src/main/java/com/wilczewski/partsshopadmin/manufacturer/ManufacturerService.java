package com.wilczewski.partsshopadmin.manufacturer;

import com.wilczewski.partsshopcommon.entity.Manufacturer;

import java.util.List;

public interface ManufacturerService {

    List<Manufacturer> listByPage(ManufacturerPageInfo pageInfo, int pageNumber, String sortDir, String keyword);

    List<Manufacturer> listOfManufacturers();

    public Manufacturer save(Manufacturer manufacturer);

    public  Manufacturer get(Integer id) throws ManufacturerNotFoundException;

    public String checkUnique(Integer id, String name, String alias);

    public void delete(Integer id) throws ManufacturerNotFoundException;

}
