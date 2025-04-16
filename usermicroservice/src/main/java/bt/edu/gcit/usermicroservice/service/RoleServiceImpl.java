package bt.edu.gcit.usermicroservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bt.edu.gcit.usermicroservice.dao.RoleDAO;
import bt.edu.gcit.usermicroservice.entity.Role;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Transactional
    @Override
    public void addRole(Role role) {
        roleDAO.addRole(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    public Role findByID(int id) {
        return roleDAO.findByID(id);
    }

    @Override
    public void deleteByID(int id) {
        roleDAO.deleteByID(id);
    }
}
