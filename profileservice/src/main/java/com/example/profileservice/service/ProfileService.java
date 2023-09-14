package com.example.profileservice.service;

import com.example.profileservice.model.ProfileDTO;
import com.example.profileservice.model.mapper.ProfileMapper;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.utils.ProfileStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    public Flux<ProfileDTO> getAllProfile(){
        return profileRepository.findAll()
                .map(ProfileMapper::entityToDto)
                .switchIfEmpty(Mono.error(new Exception("Not Found")));
    }
    public Mono<Boolean> checkDuplicate(String email){
        return profileRepository.findByEmail(email)
                .map(profile -> true)
                .switchIfEmpty(Mono.just(false));
    }
    public Mono<ProfileDTO> createNewProfile(ProfileDTO profileDTO){
        return checkDuplicate(profileDTO.getEmail()).flatMap(result->{
            if(result){
                return Mono.error(new Exception("Duplicate Email"));
            }else {
                profileDTO.setStatus(ProfileStatus.STATUS_PROFILE_PENDING);
                return createProfile(profileDTO);
            }
        });
    }

    private Mono<ProfileDTO> createProfile(ProfileDTO profileDTO) {
        return Mono.just(profileDTO)
                .flatMap(profile-> profileRepository.save(ProfileMapper.dtoToEntity(profile)))
                .map(ProfileMapper::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .doOnSuccess(dto->{
                    System.out.println("Success: "+dto.getEmail());
                });
    }
}
