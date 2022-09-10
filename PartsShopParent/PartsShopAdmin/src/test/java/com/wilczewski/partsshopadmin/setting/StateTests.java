package com.wilczewski.partsshopadmin.setting;

import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateTests {

    private StateRepository stateRepository;
    private TestEntityManager entityManager;

    @Autowired
    public StateTests(StateRepository stateRepository, TestEntityManager entityManager) {
        this.stateRepository = stateRepository;
        this.entityManager = entityManager;
    }

    @Test
    public void testCreateStatesInPoland(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

        State state = stateRepository.save(new State("Śląskie", country));

        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testListStatesByCountry(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);
        List<State> listStates = stateRepository.findByCountryOrderByNameAsc(country);

        listStates.forEach(System.out::println);

        assertThat(listStates.size()).isGreaterThan(0);
    }

}
