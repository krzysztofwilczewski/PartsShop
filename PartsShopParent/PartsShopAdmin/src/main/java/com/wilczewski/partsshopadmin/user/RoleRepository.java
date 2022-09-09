package com.wilczewski.partsshopadmin.user;

import com.wilczewski.partsshopcommon.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
