package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private final String uploadDir = "src/main/resources/static/images";
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User findByID(int id) {
        return userDAO.findByID(id);
    }

    @Override
    public void deleteByID(int id) {
        userDAO.deleteByID(id);
    }

    @Override
    @Transactional
    public User updateUser(int id, User updatedUser) {
        // First, find the user by ID
        User existingUser = userDAO.findByID(id);

        // If the user doesn't exist, throw UserNotFoundException
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        // Update the existing user with the data from updatedUser
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());

        // Check if the password has changed. If it has, encode the new password before
        // saving.
        if (!existingUser.getPassword().equals(updatedUser.getPassword())) {

            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        existingUser.setRoles(updatedUser.getRoles());

        // Save the updated user and return it
        return userDAO.save(existingUser);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        User user = userDAO.findByEmail(email);
        return user != null;
    }

    @Transactional
    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        userDAO.updateUserEnabledStatus(id, enabled);
    }

    @Transactional
    @Override
    public void uploadUserPhoto(int id, MultipartFile photo) throws IOException {
        User user = userDAO.findByID(id);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size exceeds the limit of 1MB");
        }

        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        System.out.println("Original filename: " + originalFilename);

        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        System.out.println("Filename extension: " + filenameExtension);

        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        System.out.println("Filename without extension: " + filenameWithoutExtension);

        String timestamp = String.valueOf(System.currentTimeMillis());
        System.out.println("Timestamp: " + timestamp);

        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        System.out.println("Generated filename: " + filename);

        Path uploadPath = Paths.get(uploadDir, filename);
        System.out.println("Upload path: " + uploadPath);

        photo.transferTo(uploadPath);

        user.setPhoto(filename);
        // save(user);
        userDAO.justSave(user);
    }
}
