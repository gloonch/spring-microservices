package com.example.accounts.service;

import com.example.accounts.dto.CustomerDTO;
import com.example.accounts.entity.Account;

public interface AccountService {

    /**
     *
     * @param customerDTO - CustomerDTO Object
     */
    void createAccount(CustomerDTO customerDTO);

    /**
     *
     * @param mobileNumber - Input mobile number
     * @return Account Details based on a given mobileNumber
     */
    CustomerDTO fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDTO - CustomerDTO Object
     * @return true if update is successful
     */
    boolean updateAccount(CustomerDTO customerDTO);

    /**
     *
     * @param mobileNumber
     * @return true if deletion is successful
     */
    boolean deleteAccount(String mobileNumber);
}
