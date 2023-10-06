package com.example.profileservice.service;

import com.example.commonservice.common.CommonException;

import com.example.commonservice.utils.Constant;
import com.example.profileservice.event.EventProducer;
import com.example.profileservice.model.ProfileDTO;
import com.example.profileservice.model.mapper.ProfileMapper;
import com.example.profileservice.repository.ProfileRepository;
import com.example.profileservice.utils.ProfileStatus;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    EventProducer eventProducer;
    Gson gson = new Gson();
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
                return Mono.error(new CommonException("PF02","Duplicate profile !", HttpStatus.BAD_REQUEST));
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
                    if(Objects.equals(dto.getStatus(), Constant.STATUS_PROFILE_PENDING)){
                        dto.setInitialBalance(profileDTO.getInitialBalance());
                        eventProducer.send(Constant.PROFILE_ONBOARDING_TOPIC,gson.toJson(dto)).subscribe();
                    }
                });
    }
    public Mono<ProfileDTO> updateStatusProfile(ProfileDTO profileDTO){
        return getDetailProfileByEmail(profileDTO.getEmail())
                .flatMap(profile-> {
                    profile.setStatus(profileDTO.getStatus());
                    return profileRepository.save(ProfileMapper.dtoToEntity(profile));
                })
                .map(ProfileMapper::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
    public Mono<ProfileDTO> getDetailProfileByEmail(String email){
        return profileRepository.findByEmail(email)
                .map(ProfileMapper::entityToDto)
                .switchIfEmpty(Mono.error(new CommonException("PF03", "Profile not found", HttpStatus.NOT_FOUND)));
    }
}
