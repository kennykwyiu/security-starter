package com.kenny.uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
@Entity
@Table(name = "mooc_users")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 80, unique = true, nullable = false)
    private String username;
    @JsonIgnore
    @Column(name = "password_hash", length = 100, nullable = false)
    private String password;
    @Column(length = 11, unique = true, nullable = false)
    private String mobile;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(length = 50)
    private String name;
    private boolean enabled;
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "mooc_users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> authorities;

}
