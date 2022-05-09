package com.viktorsuetnov.caloriecounting.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    public static final int SEQUENCE_START = 100_000;

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = SEQUENCE_START)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    protected Long id;

    @NotBlank
    @Size(min = 5, max = 100)
    @Column(name = "username", nullable = false)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enable", nullable = false)
    private boolean active;

    @NotNull
    @Column(name = "registered", nullable = false, updatable = false, columnDefinition = "timestamp default now()")
    private Date registered;

    @Column(name = "caloriesPerDay", nullable = false, columnDefinition = "int default 2000")
    @Range(min = 10, max = 10_000)
    private int caloriesPerDay;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @OrderBy("dateTime DESC")
    private List<Meal> meals = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String email, String password, int caloriesPerDay, Role role, Role ... roles) {
        this(id, username, email, password, caloriesPerDay, false, new Date(), EnumSet.of(role, roles));
    }

    public User(Long id, String username, String email, String password, int caloriesPerDay,
                boolean active, Date registered, Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.active = active;
        this.registered = registered;
        setRoles(roles);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    public Date getRegistered() {
        return registered;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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
        return isActive();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", username=" + username +
                ", active=" + active +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }
}
