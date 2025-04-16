package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import bt.edu.gcit.usermicroservice.entity.User;

public interface UserDAO {
    User save(User user);
    User JustSave(User user);
    List<User> getAllUsers();
    User findByID(int id);
    void deleteByID(int id);
    boolean isEmailDuplicate(String email);
    // User updateUser(int id,User user);
    void updateUserEnabledStatus(int id, boolean enabled);
    User findByEmail(String email);
}
