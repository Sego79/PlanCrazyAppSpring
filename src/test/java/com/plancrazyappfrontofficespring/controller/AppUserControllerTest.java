package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.security.jwt.JwtUtils;
import com.plancrazyappfrontofficespring.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppUserController.class)
class AppUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    AppUserService appUserService;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser(roles = "USER")
    void getLoggedAppUser() throws Exception { //todo changer nom
        AppUser appUser  = new AppUser(
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
        );

        String headerAuth = "Bearer aaaaaaa";

        when(jwtUtils.getEmailFromToken(jwtUtils.parseStringHeaderAuthorization(headerAuth))).thenReturn(appUser.getEmail());
        doNothing().when(appUserService).delete(appUser.getEmail());

        mockMvc.perform(delete("/app-user").with(csrf())).andExpect(status().isNoContent());
//        String email = jwtUtils.getEmailFromToken(jwtUtils.parseStringHeaderAuthorization(headerAuth));
    }
}