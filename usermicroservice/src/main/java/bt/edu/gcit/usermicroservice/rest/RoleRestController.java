package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.RoleService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class RoleRestController {
    private RoleService roleService;

    @Autowired
    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public void addRole(@RequestBody Role role) {
        System.out.println(role);
        roleService.addRole(role);
    }

    @GetMapping("/roles")
    public List<Role> getAllroles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/roles/{id}")
    public Role findByID(@PathVariable int id) {
        return roleService.findByID(id);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<String> deleteByID(@PathVariable int id) {
        return ResponseEntity.ok("Deleted user with ID: " + id);
    }
}
