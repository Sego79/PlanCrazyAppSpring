package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.exception.UserAlreadyExistException;
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

    //CRUD : find all app users
    @GetMapping("/app-user")
    public ResponseEntity<List<AppUserDto>> getAllAppUser() {
        List<AppUserDto> appUserDtoList = appUserService.fetchAppUser();
        return ResponseEntity.status(HttpStatus.OK).body(appUserDtoList);
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
    @PutMapping("/app-user")
    public ResponseEntity<AppUserDto> appUserToEdit(@RequestBody AppUserDto appUserDto) throws Exception {

        AppUserDto returnedDto = appUserService.updateAppUser(appUserDto);
        return new ResponseEntity<>(returnedDto, HttpStatus.OK);
    }


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
