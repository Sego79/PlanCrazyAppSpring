package com.plancrazyappfrontofficespring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.controller.dto.SigninRequest;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.security.jwt.JwtUtils;
import com.plancrazyappfrontofficespring.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthRestController.class)
class AuthRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser
    void signingUp__then__returnHttp201_with_created_status() throws Exception {
        //given
        AppUserDto appUserDto = new AppUserDto(new AppUser(
                1L,
                        "nickname",
                        "firstName",
                        "lastName,",
                        "address",
                        46800,
                        "city",
                        "0647817626",
                        "mail@mail.fr",
                        "123",
                        true
                ));
        //then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserDto)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}