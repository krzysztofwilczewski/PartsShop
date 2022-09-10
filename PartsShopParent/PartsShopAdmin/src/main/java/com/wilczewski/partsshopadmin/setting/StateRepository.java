package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    public List<State> findByCountryOrderByNameAsc(Country country);

}
