package com.example.restservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {
    public static final String USERS = "/users";

    UserController userController;

    @Autowired
    public UserResource(UserController userController) {
        this.userController = userController;
    }

    @GetMapping
    private List<User> users() {
        return userController.readAll();
    }

    @GetMapping("/{id}")
    private User user(@PathVariable int id) {

        return userController.getUserById(id);
    }

    @GetMapping("/{id}/email")
    private Map<String,String> email(@PathVariable int id){
        return Collections.singletonMap("email",userController.getUserById(id).getEmail());
    }

    @PostMapping
    private UserDto addUser(@RequestBody UserDto user) {
        return userController.addUser(user);
    }

    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable int id) {
        userController.deleteUser(id);
    }

    @PutMapping("/{id}")
    private UserDto replaceUser(@RequestBody UserDto userDto, @PathVariable int id) {
        return userController.updateOrNew(userDto, id);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UserDto> updateCustomer(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            UserDto userDto = customerService.findCustomer(id).orElseThrow(UserDtoNotFoundException::new);
            UserDto userDtoPatched = userController.applyPatchToCustomer(patch, userDto);
            customerService.updateCustomer(userDtoPatched);
            return ResponseEntity.ok(userDtoPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
