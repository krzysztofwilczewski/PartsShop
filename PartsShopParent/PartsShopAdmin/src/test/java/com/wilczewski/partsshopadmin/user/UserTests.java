package com.wilczewski.partsshopadmin.user;


import com.wilczewski.partsshopcommon.entity.Role;
import com.wilczewski.partsshopcommon.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTests {

    private UserRepository userRepository;
    private TestEntityManager testEntityManager;

    @Autowired
    public UserTests(UserRepository userRepository, TestEntityManager testEntityManager) {
        this.userRepository = userRepository;
        this.testEntityManager = testEntityManager;
    }





    @Test
    public void createUser(){
        // Role admin = testEntityManager.find(Role.class, 1);
        Role admin = new Role(1);
        User wilk = new User("Krzysztof", "Wilczewski", "wilczek@gmail.com","1234");
        wilk.addRole(admin);
        userRepository.save(wilk);
    }

    @Test
    public void createSecondUser(){
        User aga = new User("Aga", "Wilczewska", "osawilk@gmail.com", "agalamaga");
        Role seller = new Role(2);
        aga.addRole(seller);
        userRepository.save(aga);
    }

    @Test
    public void findAllUsers(){
        List<User> list = (List<User>) userRepository.findAll();
        list.forEach(user -> System.out.println(user));

    }

    @Test
    public void updateUser(){
        User user = userRepository.findById(1).get();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Test
    public void addRole(){
        User user = userRepository.findById(1).get();
        Role seller = new Role(2);
        user.addRole(seller);
        userRepository.save(user);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "user@wp.pl";
        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 10;
        Long countById = userRepository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser(){
        Integer id = 1;
        userRepository.updateUserEnabledStatus(id, false);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers(){
        String keyword = "aga";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));
    }

}
