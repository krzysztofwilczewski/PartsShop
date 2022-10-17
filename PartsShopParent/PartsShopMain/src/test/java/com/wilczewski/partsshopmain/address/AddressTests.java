package com.wilczewski.partsshopmain.address;

import com.wilczewski.partsshopcommon.entity.Address;
import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AddressTests {

    private AddressRepository addressRepository;

    @Autowired
    public AddressTests(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Test
    public void testAddNew() {
        Integer customerId = 8;
        Integer countryId = 1;

        Address newAddress = new Address();
        newAddress.setCustomer(new Customer(customerId));
        newAddress.setCountry(new Country(countryId));
        newAddress.setFirstName("Henryk");
        newAddress.setLastName("Wilczewski");
        newAddress.setPhoneNumber("19094644165");
        newAddress.setAddressLine1("Prosta 3");
        newAddress.setAddressLine2("");
        newAddress.setCity("Wasilków");
        newAddress.setState("Podlaskie");
        newAddress.setPostalCode("16-010");

        Address savedAddress = addressRepository.save(newAddress);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 8;
        List<Address> listAddresses = addressRepository.findByCustomer(new Customer(customerId));
        assertThat(listAddresses.size()).isGreaterThan(0);

        listAddresses.forEach(System.out::println);
    }

    @Test
    public void testFindByIdAndCustomer() {
        Integer addressId = 1;
        Integer customerId = 8;

        Address address = addressRepository.findByIdAndCustomer(addressId, customerId);

        assertThat(address).isNotNull();
        System.out.println(address);
    }

    @Test
    public void testUpdate() {
        Integer addressId = 1;
        String phoneNumber = "646-666-3932";

        Address address = addressRepository.findById(addressId).get();
        address.setPhoneNumber(phoneNumber);

        Address updatedAddress = addressRepository.save(address);
        assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    public void testSetDefault(){
        Integer addressId = 1;
        addressRepository.setDefaultAddress(addressId);

        Address address = addressRepository.findById(addressId).get();
        assertThat(address.isDefaultForShipping()).isTrue();
    }

    @Test
    public void testSetNoneDefaultAddress(){
        Integer addressId = 1;
        Integer customerId = 8;
        addressRepository.setNonDefaultForOthers(addressId, customerId);
    }

    @Test
    public void testGetDefault(){
        Integer customerId = 8;
        Address address = addressRepository.findDefaultByCustomer(customerId);
        assertThat(address).isNotNull();
        System.out.println(address);
    }
}
