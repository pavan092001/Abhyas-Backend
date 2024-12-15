package com.example.abhyasa.security;

import com.example.abhyasa.model.DailyQuestion;
import com.example.abhyasa.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {

    private long uid;
    private String name;
    private String email;
    private String password; // Renamed for clarity
    private String role;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean is2faEnabled = false;
    private Date createdDate;
    private Date updatedDate;

    @JsonBackReference
    private List<DailyQuestion> dailyQuestions = new ArrayList<>();

    public UserDetailsImpl() {
    }

    public static UserDetailsImpl build(User user) {
        // Ensure roles have the `ROLE_` prefix
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        return new UserDetailsImpl(
                user.getUid(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                List.of(authority),
                user.isTwoFactorEnabled(),
                user.getCreatedDate(),
                user.getUpdatedDate(),
                user.getDailyQuestions()
        );
    }

    public UserDetailsImpl(long uid, String name, String email, String password, String role,
                           Collection<? extends GrantedAuthority> authorities,
                           boolean is2faEnabled, Date createdDate, Date updatedDate,
                           List<DailyQuestion> dailyQuestions) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
        this.is2faEnabled = is2faEnabled;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.dailyQuestions = dailyQuestions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password; // Ensure you're returning the correct field
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Ensure account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Ensure account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Ensure credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Ensure the account is enabled
    }
}
