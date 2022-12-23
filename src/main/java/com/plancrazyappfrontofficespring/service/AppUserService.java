package com.plancrazyappfrontofficespring.service;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUserDto> fetchAppUser() {
        List<AppUser> appUserList = (List<AppUser>) appUserRepository.findAll();
        return appUserList.stream()
                .map(g ->AppUserDto.transformIntoDto(g))
                .collect(Collectors.toList());
    }

    public AppUser fetchById(Long id) throws Exception {
            Optional<AppUser> appUserOpt = appUserRepository.findById(id);
            return appUserOpt.orElseThrow(() -> new Exception());
        }

    public AppUserDto addAppUser(AppUserDto dto){
        AppUser appUser =  new AppUser(
                dto.getNickname(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAddress(),
                dto.getPostcode(),
                dto.getCity(),
                dto.getPhoneNumber(),
                dto.getEmail(),
                dto.getPassword()
        );

        appUser.setIsActive(true);
        appUser.setIsAdmin(false);
        appUser.setIsSuperAdmin(false);
        appUserRepository.save(appUser);
        return AppUserDto.transformIntoDto(appUser);
    }


    @Transactional
    public void delete(Long id){
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> findById(long id) {
        return appUserRepository.findById(id);
    }

    public void save(AppUser updateAppUSer) {
        appUserRepository.save(updateAppUSer);
    }


}