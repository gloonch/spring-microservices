package com.example.accounts.mapper;

import com.example.accounts.dto.AccountDTO;
import com.example.accounts.entity.Account;

public class AccountMapper {
    public static AccountDTO mapToAccountDTO(Account account, AccountDTO accountDto) {
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBranchAddress(account.getBranchAddress());
        return accountDto;
    }

    public static Account mapToAccounts(AccountDTO accountDto, Account account) {
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountType(accountDto.getAccountType());
        account.setBranchAddress(accountDto.getBranchAddress());
        return account;
    }
}