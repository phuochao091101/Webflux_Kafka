package com.example.profileservice;


import com.example.commonservice.utils.Constant;
import com.example.profileservice.controller.ProfileController;
import com.example.profileservice.model.ProfileDTO;
import com.example.profileservice.service.ProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
public class ProfileControllerTest {
    @Mock
    private ProfileService profileServiceMock;
    @InjectMocks
    private ProfileController profileController;
    private ProfileDTO profileDTO;

    @BeforeEach
    public void setUp(){
        profileDTO = new ProfileDTO(1,"test@gmail.com", Constant.STATUS_PROFILE_ACTIVE,200,"name","CUSTOMER");
        ReflectionTestUtils.setField(profileController,"profileService",profileServiceMock); //runner, object data, object data mock
    }
    @Test
    void getAllProfileSuccess(){
        when(profileServiceMock.getAllProfile()).thenReturn(Flux.just(profileDTO));
        profileController.getAllProfile().getBody().doOnNext(profileDTO1 -> Assertions.assertEquals(profileDTO,profileDTO1)).subscribe();
    }
}
