package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class UserDAOImpl implements UserDAO {

    private EntityManager entityManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserDAOImpl(EntityManager entityManager, BCryptPasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return entityManager.merge(user);
    }
    @Override
    public User JustSave(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User findByID(int id) {
        User user = entityManager.find(User.class, id);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    @Transactional
    public void deleteByID(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return false;
    }

    // @Override
    // @Transactional
    // public User updateUser(int id, User user) {
    //     User existingUser = entityManager.find(User.class, id);
    //     if (existingUser != null) {
    //         existingUser.setEmail(user.getEmail());
    //         existingUser.setPassword(user.getPassword());
    //         return entityManager.merge(existingUser);
    //     }
    //     return null;
    // }
    
    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        User user = entityManager.find(User.class, id);
        System.out.println(user);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        user.setEnabled(enabled);
        entityManager.persist(user);
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("from User where email =:email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        System.out.println(users.size());
        if (users.isEmpty()) {
            return null;
        } else {
            System.out.println(users.get(0) + " " + users.get(0).getEmail());
            return users.get(0);
        }
    }

}
