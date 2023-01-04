package com.plancrazyappfrontofficespring.service;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.exception.UserAlreadyExistException;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.model.Role;
import com.plancrazyappfrontofficespring.model.RoleEnum;
import com.plancrazyappfrontofficespring.repository.AppUserRepository;
import com.plancrazyappfrontofficespring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;


    public List<AppUserDto> fetchAppUser() {
        List<AppUser> appUserList = appUserRepository.findAll();
        return appUserList.stream()
                .map(g -> new AppUserDto(g))
                .collect(Collectors.toList());
    }

    public AppUser fetchById(Long id) throws Exception {
        Optional<AppUser> appUserOpt = appUserRepository.findById(id);
        return appUserOpt.orElseThrow(() -> new Exception());
    }

    public AppUser fetchByEmail(String email) throws Exception {
        Optional<AppUser> appUserOpt = appUserRepository.findUserByEmail(email);
        return appUserOpt.orElseThrow(() -> new Exception());
    }

    @Transactional
    public AppUserDto addAppUser(AppUserDto dto) throws UserAlreadyExistException {
        boolean alreadyExist = appUserRepository.existsByEmail(dto.getEmail());
        if (alreadyExist) {
            throw new UserAlreadyExistException(dto.getEmail());
        } else {
            AppUser appUser = new AppUser(
                    dto.getNickname(),
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getAddress(),
                    dto.getPostcode(),
                    dto.getCity(),
                    dto.getPhoneNumber(),
                    dto.getEmail(),
                    encoder.encode(dto.getPassword())
            );

            appUser.setActive(true);
            Role roleUser = roleRepository.findByName(RoleEnum.ROLE_USER);
            appUser.setRoleList(Arrays.asList(roleUser));
            appUserRepository.save(appUser);
            return new AppUserDto(appUser);
        }
    }

    @Transactional
    public AppUserDto updateAppUser(AppUserDto appUserDto) throws Exception {

        Optional<AppUser> appUserToUpdate = appUserRepository.findById(appUserDto.getAppUserId());

        if (appUserToUpdate.isPresent()) {
            AppUser updateAppUserTemp = appUserToUpdate.get();
            updateAppUserTemp.setAppUserId(appUserDto.getAppUserId());
            updateAppUserTemp.setNickname(appUserDto.getNickname());
            updateAppUserTemp.setFirstName(appUserDto.getFirstName());
            updateAppUserTemp.setLastName(appUserDto.getLastName());
            updateAppUserTemp.setAddress(appUserDto.getAddress());
            updateAppUserTemp.setPostcode(appUserDto.getPostcode());
            updateAppUserTemp.setCity(appUserDto.getCity());
            updateAppUserTemp.setPhoneNumber(appUserDto.getPhoneNumber());
            updateAppUserTemp.setEmail(appUserDto.getEmail());
            updateAppUserTemp.setPassword(encoder.encode(appUserDto.getPassword()));

            AppUser appUser = appUserRepository.save(updateAppUserTemp);
            return new AppUserDto(appUser);
        } else {
            throw new Exception();
        }
    }

    @Transactional
    public void delete(String email) {
        appUserRepository.deleteByEmail(email);
    }
}
