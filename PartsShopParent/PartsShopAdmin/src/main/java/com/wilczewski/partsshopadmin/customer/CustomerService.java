package com.wilczewski.partsshopadmin.customer;

import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.exception.CustomerNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    public List<Country> listAllCountries();

 //   public boolean isEmailUnique(String email);

  //  public void registerCustomer(Customer customer);

  //  public boolean verify(String verificationCode);

    public Page<Customer> listByPage(int pageNumber, String sortField, String sortDir, String keyword);

    public void updateCustomerEnabledStatus(Integer id, boolean enabled);

    public Customer get(Integer id) throws CustomerNotFoundException;

    public boolean checkIsEmailUnique(Integer id, String email);

    public void save(Customer customerInForm);

    public  void delete(Integer id) throws CustomerNotFoundException;

}
