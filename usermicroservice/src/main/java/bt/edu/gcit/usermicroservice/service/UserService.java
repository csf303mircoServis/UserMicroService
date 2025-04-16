package bt.edu.gcit.usermicroservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import bt.edu.gcit.usermicroservice.entity.User;

public interface UserService {
    User save(User user);

    List<User> getAllUsers();

    User findByID(int id);

    void deleteByID(int id);

    boolean isEmailDuplicate(String email);

    User updateUser(int id, User user);

    void updateUserEnabledStatus(int id, boolean enabled);

    void uploadUserPhoto(int id, MultipartFile photo) throws IOException;
}
