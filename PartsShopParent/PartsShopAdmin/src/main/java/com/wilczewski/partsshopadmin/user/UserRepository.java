package com.wilczewski.partsshopadmin.user;

import com.wilczewski.partsshopcommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.firstName, ' ', u.lastName, ' ', u.email) LIKE %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);

    @Query("UPDATE User u SET u.enabled = ?2 where u.id = ?1")
    @Modifying
    public void updateUserEnabledStatus(Integer id, boolean enabled);
}
