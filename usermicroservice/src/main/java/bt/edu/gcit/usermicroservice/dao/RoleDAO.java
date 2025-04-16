package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import bt.edu.gcit.usermicroservice.entity.Role;

public interface RoleDAO {
    void addRole(Role role);
    List<Role> getAllRoles();
    Role findByID(int id);
    void deleteByID(int id);
}
