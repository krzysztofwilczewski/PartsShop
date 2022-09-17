package com.wilczewski.partsshopmain.configuration;

import com.wilczewski.partsshopcommon.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomerUserDetails implements UserDetails {

    private Customer customer;

    public CustomerUserDetails(Customer customer) {
        this.customer = customer;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return null;
    }

    public String getPassword(){
        return customer.getPassword();
    }

    public String getUsername(){
        return customer.getEmail();
    }

    public boolean isAccountNonExpired(){
        return true;
    }

    public boolean isAccountNonLocked(){
        return true;
    }

    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customer.isEnabled();
    }

    public String getFullName(){
        return customer.getFirstName() + " " + customer.getLastName();
    }

    public Customer getCustomer(){
        return this.customer;
    }

}
