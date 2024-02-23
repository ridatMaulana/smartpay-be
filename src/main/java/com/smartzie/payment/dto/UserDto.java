package com.smartzie.payment.dto;

import com.smartzie.payment.component.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private String name;
    private String department;
    private String number;
    public UserDto(Long id, String username, String email, Role role, String name, String department){
        this.name = name;
        this.id = id;
        this.username = username;
        this.role = role;
        this.email = email;
        this.department = department;
    }
    // getters, setters, dan konstruktor
}
