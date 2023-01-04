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
        String email = jwtUtils.getEmailFromToken(jwtUtils.parseStringHeaderAuthorization(headerAuth));
        AppUserDto connectedUser = new AppUserDto(appUserService.fetchByEmail(email));
        return new ResponseEntity<>(connectedUser, HttpStatus.OK);
    }

    @PutMapping("/app-user")
    public ResponseEntity<AppUserDto> appUserEdit(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody AppUserDto appUserDto) throws Exception {
        String email = jwtUtils.getEmailFromToken(jwtUtils.parseStringHeaderAuthorization(headerAuth));
        appUserDto.setEmail(email);
        AppUserDto returnedDto = appUserService.updateAppUser(appUserDto); // TODO : disable password modification !
        // en l'occurrence ici le changement de mot de passe ne change pas le token de connexion, changer de mdp devrait
        // être une autre requête !
        return new ResponseEntity<>(returnedDto, HttpStatus.OK);
    }


    //CRUD : create
    @PostMapping("/signup")
    public ResponseEntity<AppUserDto> createAppUser(@RequestBody AppUserDto dto) {
        try {
            AppUserDto newAppUserDto = appUserService.addAppUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAppUserDto);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    //CRUD : update
//    @PutMapping("/app-user")
//    public ResponseEntity<AppUserDto> appUserToEdit(@RequestBody AppUserDto appUserDto) throws Exception {
//
//        AppUserDto returnedDto = appUserService.updateAppUser(appUserDto);
//        return new ResponseEntity<>(returnedDto, HttpStatus.OK);
//    }


    //CRUD : delete
    @DeleteMapping("/app-user/{id}")
    public ResponseEntity<HttpStatus> deleteAppUSer(@PathVariable("id") long idAppUserToDelete) {
        try {
            appUserService.delete(idAppUserToDelete);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
