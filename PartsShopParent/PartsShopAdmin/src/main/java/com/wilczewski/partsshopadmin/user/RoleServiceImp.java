package com.wilczewski.partsshopadmin.user;

import com.wilczewski.partsshopcommon.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleServiceImp implements RoleService{

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }



    public List<Role> allRoles(){
        return  roleRepository.findAll();
    }

}
