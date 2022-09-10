package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    public List<Currency> findAllByOrderByNameAsc();

}
