package project.services;

import project.model.Role;

public interface RoleService {

    public Role getRoleById(int id);
    public Role getRoleByName(String name);
}
