package com.wilczewski.partsshopmain.customer;

import com.wilczewski.partsshopcommon.entity.AuthenticationType;
import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerTests {

    private CustomerRepository customerRepository;
    private TestEntityManager entityManager;

    @Autowired
    public CustomerTests(CustomerRepository customerRepository, TestEntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
    }
    @Test
    public void testCreateCustomer(){

        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Krzysztof");
        customer.setLastName("Wilczewski");
        customer.setPassword("qwertyuiop");
        customer.setEmail("twornik@poczta.onet.pl");
        customer.setPhoneNumber("666333444");
        customer.setAddressLine1("Bojarska 7");
        customer.setCity("Białystok");
        customer.setState("Podlaskie");
        customer.setPostalCode("15-543");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateCustomer2(){

        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Agnieszka");
        customer.setLastName("Wilczewska");
        customer.setPassword("qwertyuiop");
        customer.setEmail("agnus@interia.pl");
        customer.setPhoneNumber("224567890");
        customer.setAddressLine1("Bojarska 7");
        customer.setCity("Białystok");
        customer.setState("Podlaskie");
        customer.setPostalCode("15-543");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }
    @Test
    public void testListCustomers(){
        Iterable<Customer> customers = customerRepository.findAll();
        customers.forEach(System.out::println);

        assertThat(customers).hasSizeGreaterThan(1);
    }

    @Test
    public void testUpdateCustomer(){
        Integer customerId = 2;
        String lastName = "Osmólska";

        Customer customer = customerRepository.findById(customerId).get();
        customer.setLastName(lastName);
        customer.setEnabled(true);

        Customer updatedCustomer = customerRepository.save(customer);
        assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testFindByEmail(){
        String email = "twornik@poczta.onet.pl";
        Customer customer = customerRepository.findByEmail(email);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode(){
        String code = "code123";
        Customer customer = customerRepository.findByVerificationCode(code);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer(){
        Integer customerId = 1;
        customerRepository.enable(customerId);

        Customer customer = customerRepository.findById(customerId).get();
        assertThat(customer.isEnabled()).isTrue();
    }

    @Test
    public void testUpdateAuthenticationType(){

        Integer id = 2;

        customerRepository.updateAuthenticationType(id, AuthenticationType.DATABASE);

        Customer customer = customerRepository.findById(id).get();

        assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
    }


}
