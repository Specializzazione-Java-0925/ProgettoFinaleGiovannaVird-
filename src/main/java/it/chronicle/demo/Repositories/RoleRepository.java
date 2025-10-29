package it.chronicle.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.chronicle.demo.Models.Role;

public interface RoleRepository extends JpaRepository<Role , Long> {
     Role findByName(String name); 
}
