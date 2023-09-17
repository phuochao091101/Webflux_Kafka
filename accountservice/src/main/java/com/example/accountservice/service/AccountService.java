package com.example.accountservice.service;

import com.example.accountservice.model.AccountDTO;
import com.example.accountservice.model.mapper.AccountMapper;
import com.example.accountservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    public Mono<AccountDTO> createNewAccount(AccountDTO accountDTO){
        return Mono.just(accountDTO)
                .flatMap(accountDTO1 -> accountRepository.save(AccountMapper.dtoToEntity(accountDTO)))
                .map(AccountMapper::entityToModel);
    }
}