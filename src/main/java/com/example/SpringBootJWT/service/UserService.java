package com.example.SpringBootJWT.service;

import com.example.SpringBootJWT.dto.CreateUserDto;
import com.example.SpringBootJWT.dto.UserDto;
import com.example.SpringBootJWT.entities.User;
import com.example.SpringBootJWT.exception.UserNotFoundException;
import com.example.SpringBootJWT.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto saveUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        User savedUser = userRepository.save(user);
        //      till here one row with automatically generated id and name and email is created in the "user" table
//        Hibernate sends an SQL query like:INSERT INTO user(name, email)
//VALUES ('Gaurav', 'gaurav@gmail.com');
        return new UserDto(savedUser.getId(),savedUser.getName(),savedUser.getEmail());
//        why to create new object we can also send the user directly isn't it?
        /*save() does not return the same object just for convenience—it returns the managed entity
        after Hibernate has synchronized it with the database. Sometimes that returned object contains
         new information.
         also the return type is userDto we cannot return user as it is a entity */
    }

    public List<UserDto> getUsers() {
//        return userRepository.findAll();//this will return User but we want UserDto
//        List<User> users = new ArrayList<>();initializing with arraylist is not required we can directly write lilke
        List<User> users = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user: users){
            UserDto userDto = new UserDto(user.getId(),user.getName(),user.getEmail());
//            user is a different object than userDto so we need to create object of userDto with
//            the constructor
            userDtoList.add(userDto);
        }
        return userDtoList;
    }


    public UserDto getUserById(Long id) {
        //        if(userRepository.findById(id).isEmpty()){
//            throw new UserNotFoundException("User not found with id: "+ id);
//        naive way to throw the exception
//        }
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User not found with id: "+ id));;//what if data is not present it should through some exception
//        we will be studying letter on
        return new UserDto(user.getId(),user.getName(),user.getEmail());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDto patchUser(Long id, CreateUserDto patchUserDto) {
        User user = userRepository.findById(id).orElseThrow();
        if(patchUserDto.getName() != null){
            user.setName(patchUserDto.getName());
        }
        if(patchUserDto.getEmail() != null){
            user.setEmail(patchUserDto.getEmail());
        }
        //no save due to dirty checking

        return new UserDto(user.getId(),user.getName(),user.getEmail());
    }

    @Transactional
//    The reason @Transactional is used here is because you're modifying an entity without explicitly calling save().
//    There is no
//
//userRepository.save(user);
//
//Yet the database gets updated.
/*
* Without a transaction, there is no guarantee that Hibernate will synchronize those changes back to the database automatically.
* During the commit, Hibernate checks:

"Did any managed entity change?"

It sees:

Old Name : Gaurav
New Name : Rahul

So it automatically generates

UPDATE user
SET name='Rahul',
    email='...'
WHERE id=1;
* */
    public UserDto updateUser(Long id, CreateUserDto updateUserDto) {

        User user = userRepository.findById(id).orElseThrow();
        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());
        return new UserDto(user.getId(),user.getName(),user.getEmail());
    }

    public List<UserDto> getUsersPaginated(int page, int pageSize, String direction, String sortBy) {
        Sort sort;
        sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,pageSize,sort);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserDto> userDtoList = new ArrayList<>();

        usersPage.forEach(user -> userDtoList.add(new UserDto(user.getId(),user.getName(),user.getEmail())));
        return userDtoList;
    }
//    public ResponseEntity<UserDto>
}
