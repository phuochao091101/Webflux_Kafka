package com.example.accountservice.model.mapper;

import com.example.accountservice.data.Account;
import com.example.accountservice.model.AccountDTO;

public class AccountMapper {
    public static AccountDTO entityToModel(Account account){
        return AccountDTO.builder()
                .email(account.getEmail())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .reserved(account.getReserved())
                .id(account.getId())
                .build();
    }

    public static Account dtoToEntity(AccountDTO accountDTO){
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setReserved(accountDTO.getReserved());
        account.setBalance(accountDTO.getBalance());
        account.setCurrency(accountDTO.getCurrency());
        account.setEmail(accountDTO.getEmail());
        return account;
    }
}
