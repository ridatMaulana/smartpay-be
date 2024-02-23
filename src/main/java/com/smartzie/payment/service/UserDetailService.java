package com.smartzie.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartzie.payment.component.Role;
import com.smartzie.payment.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path("/webservice/rest/server.php")
                .queryParam("wstoken", "e177524bb372f8329d9cd4ce877f8a09")
                .queryParam("wsfunction", "core_user_get_users_by_field")
                .queryParam("moodlewsrestformat", "json")
                .queryParam("values[0]", username)
                .queryParam("field", "username")
                .build();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
             node = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (node.size() == 0) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        JsonNode userNode = node.get(0);
        Role roles = Role.ROLE_SISWA;
        if (userNode.get("username").asText().equals("admin")){
            roles = Role.ROLE_ADMIN;
        }

        UserDto dto = new UserDto(
                userNode.get("id").asLong(),
                userNode.get("username").asText(),
                userNode.get("email").asText(),
                roles,
                userNode.get("fullname").asText(),
                userNode.get("department").asText()
        );
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(dto.getRole().toString()));
        User user = new User(
                userNode.get("id").asText(),
                "",
                true,
                true,
                true,
                true,
                authorities
        );
        return user;
    }
}
