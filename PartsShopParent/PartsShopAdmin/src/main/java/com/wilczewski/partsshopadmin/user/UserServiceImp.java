package com.wilczewski.partsshopadmin.user;


import com.wilczewski.partsshopcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImp implements UserService {

    public static final int USERS_PER_PAGE = 7;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }


    @Override
    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    //   public List<User> listAllUsers() {
    //      return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    //  }

    public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);

        if (keyword != null) {
            return userRepository.findAll(keyword, pageable);
        }

        return userRepository.findAll(pageable);
    }

    public void save(User user) {

        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }

        } else {
            encodePassword(user);
        }
        userRepository.save(user);
    }

    @Override
    public User updateAccount(User userUpdated) {
        User userSaved = userRepository.findById(userUpdated.getId()).get();

        if (!userUpdated.getPassword().isEmpty()) {
            userSaved.setPassword(userUpdated.getPassword());
            encodePassword(userSaved);
        }

        userSaved.setFirstName(userUpdated.getFirstName());
        userSaved.setLastName(userUpdated.getLastName());

        return userRepository.save(userSaved);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) return true;
        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            if (userByEmail != null) return false;
        } else {
            if (userByEmail.getId() != id) {
                return false;
            }
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new UserNotFoundException("Nie znaleziono użytkownika z ID" + id);
        }
    }


    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Nie znaleziono użytkownika z ID" + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateUserEnabledStatus(id, enabled);
    }

}
