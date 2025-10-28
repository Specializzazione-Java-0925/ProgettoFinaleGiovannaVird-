package it.chronicle.demo.Models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false ,length = 100)
    private String username;
    @Column(nullable = false , unique = true , length = 100)
    private String email;
    @Column(nullable = false , length = 100)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name= "users_roles",
        joinColumns = {@JoinColumn(name="USER_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name="ROLE_ID",referencedColumnName = "ID")})
        private List<Role> roles = new ArrayList<>();;

}

