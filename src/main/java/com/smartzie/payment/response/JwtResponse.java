package com.smartzie.payment.response;

import com.smartzie.payment.component.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private String name;
    private String email;
    private Role role;
    private String department;

    public JwtResponse(String token, Long id, String username, String name, String email, Role role, String department){
        this.token = token;
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.department = department;
    }

}
