package com.smartzie.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartzie.payment.component.*;
import com.smartzie.payment.component.jwt.JwtUtils;
import com.smartzie.payment.dto.UserDto;
import com.smartzie.payment.request.LoginRequest;
import com.smartzie.payment.response.JwtResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class LoginService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    HttpServletResponse httpServletResponse;

    public ResponseEntity<?> login(LoginRequest userRequest){
        Response<?> data = new Response<>();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost/login/token.php";
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", userRequest.getUsername());
        requestBody.add("password", userRequest.getPassword());
        requestBody.add("service", "moodle_mobile_app");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            data.setResponseCode(ResponseCode.BAD_REQUEST);
            data.setMessage(e);
            return ResponseEntity.badRequest().body(data);
        }
        if (node.size() == 0) {
            data.setResponseCode(ResponseCode.BAD_REQUEST);
            return ResponseEntity.badRequest().body(data);
        }

        if(node.has("error")){
            Response<?> response1 = new Response<>();
            response1.setResponseCode(ResponseCode.BAD_REQUEST);
            data.setMessage("User yang anda masukan tidak valid");
            return ResponseEntity.badRequest().body(response1);
        }
        return ResponseEntity.ok(loginSiswa(userRequest.getUsername()).getBody());
    }

    public ResponseEntity<?> loginSiswa(String username){
        Response<JwtResponse> data = new Response<>();
        data.setResponseCode(ResponseCode.SUCCESS);
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
            data.setResponseCode(ResponseCode.BAD_REQUEST);
            data.setMessage("User yang anda masukan tidak valid");
            return ResponseEntity.badRequest().body(data);
        }
        JsonNode userNode = node.get(0);
        Role roles = Role.ROLE_SISWA;
        if (userNode.get("username").asText().equals("admin")){
            roles = Role.ROLE_ADMIN;
//            data.setResponseCode(ResponseCode.BAD_REQUEST);
//            data.setMessage("User yang anda masukan tidak valid");
        }

        UserDto dto = new UserDto(
                userNode.get("id").asLong(),
                userNode.get("username").asText(),
                userNode.get("email").asText(),
                roles,
                userNode.get("fullname").asText(),
                userNode.get("department").asText()
        );

        JwtResponse jwtResponse = token(dto);
        data.setData(jwtResponse);
        return ResponseEntity.ok(data);
    }

    public JwtResponse token(UserDto user){
        Response<JwtResponse> response = new Response<>();
        String jwtToken = jwtUtils.generateJwtToken(user.getUsername());
       JwtResponse jwtResponse = new JwtResponse(
               jwtToken,
               user.getId(),
               user.getUsername(),
               user.getName(),
               user.getEmail(),
               user.getRole(),
               user.getDepartment()
               );
       return jwtResponse;
    }
}
