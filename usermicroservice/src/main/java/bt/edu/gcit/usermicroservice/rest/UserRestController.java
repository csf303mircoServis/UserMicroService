package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;
import io.micrometer.common.lang.NonNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = "multipart/form-data")
    public User save(
            @RequestPart("firstName") @Valid @NotNull(message = "First name is required.Currently it is empty") String firstName,
            @RequestPart("lastName") @Valid @NotNull String lastName,
            @RequestPart("email") @Valid @NotNull String email,
            @RequestPart("password") @Valid @NotNull String password,
            @RequestPart("photo") @Valid @NotNull MultipartFile photo,
            @RequestPart("roles") @Valid @NotNull String rolesJson) {
        try {
            // Create a new User object
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            // user.setRoles(roles);
            // Parse the roles JSON string into a Set<Role>
            ObjectMapper objectMapper = new ObjectMapper();
            Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>() {
            });
            user.setRoles(roles);
            System.out.println("Uploading photo");

            // Save the user and get the ID
            User savedUser = userService.save(user);

            // Upload the user photo
            System.out.println("Uploading photo" + savedUser.getId().intValue());
            userService.uploadUserPhoto(savedUser.getId().intValue(), photo);

            // Return the saved user
            return savedUser;
        } catch (IOException e) {
            // Handle the exception
            throw new RuntimeException("Error while uploading photo",
                    e);
        }
    }

    // @PostMapping("/users")
    // public User save(@RequestBody User user) {
    // return userService.save(user);
    // }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User findByID(@PathVariable int id) {
        return userService.findByID(id);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteByID(@PathVariable int id) {
        userService.deleteByID(id);
        return ResponseEntity.ok("Deleted user with ID: " + id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @GetMapping("/users/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }

    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(
            @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean enabled = requestBody.get("enabled");
        userService.updateUserEnabledStatus(id, enabled);
        System.out.println("User enabled status updated successfully");
        return ResponseEntity.ok().build();
    }
}