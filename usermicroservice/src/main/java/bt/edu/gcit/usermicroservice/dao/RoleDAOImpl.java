package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Role;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class RoleDAOImpl implements RoleDAO {
    private EntityManager entityManager;

    @Autowired
    public RoleDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    @Override
    public Role findByID(int id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    @Transactional
    public void deleteByID(int id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }
}
