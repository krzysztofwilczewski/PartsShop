package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.State;
import com.wilczewski.partsshopcommon.entity.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class StateRestController {

    private StateRepository stateRepository;

    @Autowired
    public StateRestController(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @GetMapping("/states/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<State> listStates = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> result = new ArrayList<>();

        for (State state : listStates){
            result.add(new StateDTO(state.getId(), state.getName()));
        }

        return result;
    }


    @PostMapping("/states/save")
    public String save(@RequestBody State state){
        State savedState = stateRepository.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("/states/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        stateRepository.deleteById(id);
    }

}
