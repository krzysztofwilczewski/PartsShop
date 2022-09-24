package com.wilczewski.partsshopmain.address;

import com.wilczewski.partsshopcommon.entity.Address;
import com.wilczewski.partsshopcommon.entity.Customer;

import java.util.List;

public interface AddressService {

    public List<Address> listAddressBook(Customer customer);

    public void save(Address address);

    public Address get(Integer addressId, Integer customerId);

    public void delete(Integer addressId, Integer customerId);

    public void setDefaultAddress(Integer defaultAddressId, Integer customerId);
}
