package com.viktorsuetnov.caloriecounting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Schema(description = "Сущность пользователя")
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank
    @Size(min = 5, max = 100)
    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;


    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(min = 8, max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "active", nullable = false)
    private boolean active;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "activation_code")
    private String activationCode;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "registered", updatable = false, columnDefinition = "timestamp default now()")
    private LocalDate registered;

    @Column(name = "calories_per_day")
    private Integer caloriesPerDay;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @OrderBy("dateTime DESC")
    private List<Meal> meals = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.registered = LocalDate.now();
    }

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User() {
    }

    public User(Long id, String username, String email, String password, List<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", username=" + username +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }
}
