package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class CountryRestController {

    private CountryRepository countryRepository;

  //  private CustomerService customerService;

    @Autowired
    public CountryRestController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping("/countries/list")
    public List<Country> listAll(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    @PostMapping("/countries/save")
    public String save(@RequestBody Country country){
        Country savedCountry = countryRepository.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @DeleteMapping("/countries/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        countryRepository.deleteById(id);
    }

  /*  @PostMapping("/customers/check_email")
    public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email){
        if (customerService.checkIsEmailUnique(id, email)) {
            return "OK";
        } else {
            return "DUPLIKACJA";
        }
    } */

}
