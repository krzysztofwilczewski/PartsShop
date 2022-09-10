package com.wilczewski.partsshopadmin.manufacturer;

import com.wilczewski.partsshopcommon.entity.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

    @Query("SELECT m FROM Manufacturer m WHERE m.parent.id is NULL")
    public List<Manufacturer> findRootManufacturers(Sort sort);

    @Query("SELECT m FROM Manufacturer m WHERE m.parent.id is NULL")
    public Page<Manufacturer> findRootManufacturers(Pageable pageable);

    @Query("SELECT m FROM Manufacturer m WHERE m.name LIKE %?1%")
    public Page<Manufacturer> search(String keyword, Pageable pageable);

    public Manufacturer findByName(String name);

    public Manufacturer findByAlias(String alias);

    public Long countById(Integer id);

}
