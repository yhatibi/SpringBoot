package com.example.restservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    UserDAO userRepository;

    @Autowired
    public UserController(UserDAO userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> user =  userRepository.findById(id);
        if (user.isPresent()) return user.get();
        else return null;
    }

    public UserDto addUser(UserDto userDto) {
        User u = new User(userDto);
        userRepository.save(u);
        return userDto;
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }


    public UserDto updateOrNew(UserDto userDto, int id) {
        User user = new User(userDto);
        return userRepository.findById(id).map(o -> {
            o.setEmail(user.getEmail());
            o.setPassword(user.getPassword());
            o.setFullName(user.getFullName());
            userRepository.save(o);
            return new UserDto(o);
        }).orElseGet(() -> {
            UserDto userDto1 = new UserDto(userRepository.save(user));
            return userDto1;
        });
    }

    public UserDto applyPatchToCustomer(JsonPatch patch, UserDto userDto) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(userDto, JsonNode.class));
        return objectMapper.treeToValue(patched, UserDto.class);
    }

}
