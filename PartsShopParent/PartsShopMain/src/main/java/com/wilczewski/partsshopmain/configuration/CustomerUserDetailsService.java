package com.wilczewski.partsshopmain.configuration;

import com.wilczewski.partsshopcommon.entity.Customer;
import com.wilczewski.partsshopmain.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer= customerRepository.findByEmail(email);

        if (customer == null)
            throw new UsernameNotFoundException("Nie znaleziono klienta o emailu "+ email);

        return new CustomerUserDetails(customer);
    }

}
