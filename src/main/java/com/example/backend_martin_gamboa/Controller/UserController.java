package com.example.backend_martin_gamboa.Controller;


import com.example.backend_martin_gamboa.Entity.UserEntity;
import com.example.backend_martin_gamboa.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/")
    public ResponseEntity<List<UserEntity>> listUsers(){ //ResponseEntity, what it is?
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id){
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable("email") String email){
        UserEntity user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("phone/{phone}")
    public ResponseEntity<UserEntity> getUserByPhone(@PathVariable("phone") String phone){
        UserEntity user = userService.getUserByPhone(phone);
        return ResponseEntity.ok(user);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<UserEntity>> getUserByName(@PathVariable("name") String name){
        List<UserEntity> users = userService.getUserByName(name);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user){
        UserEntity newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user){
        UserEntity updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) throws Exception{
        var isDeleted = userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody UserEntity user){
        Long log =  userService.login(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(log);
    }

    @GetMapping("/ageLimit/{userId}")
    public ResponseEntity<Boolean> ageLimit(@PathVariable Long userId){
        Integer age = userService.userAge(userId);
        Boolean limit = userService.ageLimit(age);
        return ResponseEntity.ok(limit);
    }
}
