package com.wilczewski.partsshopmain.customer;

import com.wilczewski.partsshopcommon.entity.AuthenticationType;
import com.wilczewski.partsshopcommon.entity.Country;
import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopcommon.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {

    public boolean isEmailUnique(String email);

    public void registerCustomer(Customer customer);

    public boolean verify(String verificationCode);

    public List<Country> listAllCountries();

    public void updateAuthentication(Customer customer, AuthenticationType authenticationType);

    public Customer getCustomerByEmail(String email);

    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType authenticationType);

    public void update(Customer customerInForm);

    public String updateResetPasswordToken(String email) throws CustomerNotFoundException;

    public Customer getByResetPasswordToken(String token);

    public void updatePassword(String token, String newPassword) throws CustomerNotFoundException;

}
