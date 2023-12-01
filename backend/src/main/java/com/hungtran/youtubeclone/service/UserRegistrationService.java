package com.hungtran.youtubeclone.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungtran.youtubeclone.dto.UserInfoDTO;
import com.hungtran.youtubeclone.model.User;
import com.hungtran.youtubeclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${keycloak.userinfoEndpoint}")
    private String userInfoEndpoint;
    private final UserRepository userRepository;
    public String registerUser(String tokenValue) {
        //make a call to the userInfo endpoint

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        try {
            HttpResponse<String> responseString = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = responseString.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDTO userInfoDTO = objectMapper.readValue(body, UserInfoDTO.class);

            Optional<User> userBySubject = userRepository.findBySub((userInfoDTO.getSub()));
            if(userBySubject.isPresent()) {
                return userBySubject.get().getId();
            }else {
                User user = new User();
                user.setFirstName("Hạ");
                user.setLastName("An");
                user.setFullName("Hạ An");
                user.setEmailAddress("tt3107@gmail.com");
                user.setSub("b4de143a-c189-4057-87fb-91092f49a503");

                return userRepository.save(user).getId();
            }

            //tạo object cho user

        } catch (Exception exception) {
            throw new RuntimeException("Exception occurred while registering user",exception);
        }
        //fetch user details and save them to the database
    }
}
