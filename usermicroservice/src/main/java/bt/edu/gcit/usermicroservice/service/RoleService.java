package bt.edu.gcit.usermicroservice.service;

import java.util.List;

import bt.edu.gcit.usermicroservice.entity.Role;

public interface RoleService {
    void addRole(Role role);
    List<Role> getAllRoles();
    Role findByID(int id);
    void deleteByID(int id);
}