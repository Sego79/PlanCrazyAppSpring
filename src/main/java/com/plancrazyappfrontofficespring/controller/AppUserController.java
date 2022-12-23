package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AppUserController {


    @Autowired
    private AppUserService appUserService;

    //CRUD : find all
    @GetMapping("/app-user")
    public ResponseEntity<List<AppUserDto>> getAllAppUser() {
        List<AppUserDto> games = appUserService.fetchAppUser();
        return ResponseEntity.status(HttpStatus.OK).body(games); //retourne en JSON dans le corps (body) les games
    }

    //CRUD : find by id
    @GetMapping("/app-user/{id}")
    public ResponseEntity<AppUser> getAppUserById(@PathVariable("id") long id) throws Exception {
        Optional<AppUser> optAppUser = Optional.ofNullable(appUserService.fetchById(id));
        if (optAppUser.isPresent()) {
            return new ResponseEntity<>(optAppUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //CRUD : create
    @PostMapping("/app-user")
    public ResponseEntity<AppUserDto> createGame(@RequestBody AppUserDto dto) {
        AppUserDto newAppUserDto = appUserService.addAppUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAppUserDto);
    }

    //CRUD : update
    @PutMapping("/app-user/{id}")
    public ResponseEntity<AppUserDto> appUserToEdit(@PathVariable("id") long id,  @RequestBody AppUserDto dto) {
        Optional<AppUser> optAppUser = appUserService.findById(id);
        System.out.println(id);
        System.out.println(optAppUser.get());
        if (optAppUser.isPresent()) {
            AppUser updateAppUser = optAppUser.get();
            updateAppUser.setAppUserId(dto.getAppUserId());
            updateAppUser.setNickname(dto.getNickname());
            updateAppUser.setFirstName(dto.getFirstName());
            updateAppUser.setLastName(dto.getLastName());
            updateAppUser.setAddress(dto.getAddress());
            updateAppUser.setPostcode(dto.getPostcode());
            updateAppUser.setCity(dto.getCity());
            updateAppUser.setPhoneNumber(dto.getPhoneNumber());
            updateAppUser.setEmail(dto.getEmail());
            updateAppUser.setPassword(dto.getPassword());

            appUserService.save(updateAppUser);
//TODO : VERIFIER CE QUI EST REELLEMENT RENVOYE
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //CRUD : delete
    @DeleteMapping("/app-user/{id}")
    public ResponseEntity<HttpStatus> deleteAppUSer(@PathVariable("id") long idAppUserToDelete){
        try {
            appUserService.delete(idAppUserToDelete);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
