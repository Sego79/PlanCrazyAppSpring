package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.exception.UserAlreadyExistException;
import com.plancrazyappfrontofficespring.security.jwt.JwtUtils;
import com.plancrazyappfrontofficespring.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private JwtUtils jwtUtils;


    // CRUD : find the connected user
    @GetMapping("/app-user")
    public ResponseEntity<AppUserDto> getLoggedAppUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        return new ResponseEntity<>(connectedUser, HttpStatus.OK);
    }

    //CRUD : modify the connected user, excluding the unique fields
    // todo : check if all unique fields are well protected
    @PutMapping("/app-user")
    public ResponseEntity<AppUserDto> appUserEdit(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody AppUserDto appUserDto) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        appUserDto.setEmail(connectedUser.getEmail());
        appUserDto.setAppUserId(connectedUser.getAppUserId());
        appUserDto.setNickname(connectedUser.getNickname());
        appUserDto.setPassword(connectedUser.getPassword());
        appUserDto.setPhoneNumber(connectedUser.getPhoneNumber());
        AppUserDto returnedDto = appUserService.updateAppUser(appUserDto);
        return new ResponseEntity<>(returnedDto, HttpStatus.OK);
    }


    //CRUD : delete
    // todo : vérifier qu'on supprime bien le token dans le back (genre ?)
    @DeleteMapping("/app-user")
    public ResponseEntity<HttpStatus> deleteAppUSer(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth) {
        String email = jwtUtils.getEmailFromToken(jwtUtils.parseStringHeaderAuthorization(headerAuth));
        try {
            appUserService.delete(email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
