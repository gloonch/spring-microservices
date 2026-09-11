package com.example.accounts.service;

import com.example.accounts.dto.CustomerDTO;
import com.example.accounts.entity.Account;

public interface AccountService {

    /**
     *
     * @param customerDTO - CustomerDTO Object
     */
    void createAccount(CustomerDTO customerDTO);
}
