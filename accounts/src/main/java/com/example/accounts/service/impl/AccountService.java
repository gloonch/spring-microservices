package com.example.accounts.service.impl;


import com.example.accounts.constants.AccountConstants;
import com.example.accounts.dto.AccountDTO;
import com.example.accounts.dto.CustomerDTO;
import com.example.accounts.entity.Account;
import com.example.accounts.entity.Customer;
import com.example.accounts.exception.CustomerAlreadyExistsException;
import com.example.accounts.exception.ResourceNotFoundException;
import com.example.accounts.mapper.AccountMapper;
import com.example.accounts.mapper.CustomerMapper;
import com.example.accounts.repository.AccountRepository;
import com.example.accounts.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountService implements com.example.accounts.service.AccountService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;


    /**
     *
     * @param customerDTO - CustomerDTO Object
     */
    @Override
    public void createAccount(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(String.valueOf(customerDTO.getMobileNumber()));
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer with mobile number " + customerDTO.getMobileNumber() + " already exists");
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createAccount(savedCustomer));
    }

    /**
     *
     * @param mobileNumber - Input mobile number
     * @return Account details based on a given mobileNumber
     */
    @Override
    public CustomerDTO fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()-> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDTO(customer, new CustomerDTO());
        customerDTO.setAccountDTO(AccountMapper.mapToAccountDTO(account, new AccountDTO()));

        return customerDTO;
    }

    /**
     *
     * @param customerDTO - CustomerDTO Object
     * @return boolean indicating if update was successful
     */
    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {

        // Find the existing customer using the mobile number
        Customer customer = customerRepository
                .findByMobileNumber(customerDTO.getMobileNumber())
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Customer", "mobileNumber", customerDTO.getMobileNumber())
                );

        // Update customer information using the mapper
        CustomerMapper.mapToCustomer(customerDTO, customer);

        if (customerDTO.getAccountDTO() != null) {

            // Save updated customer
            customerRepository.save(customer);

            // Find the account connected to this customer
            Account account = accountRepository
                    .findByCustomerId(customer.getCustomerId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
                    );

            // Update account information if accountDTO exists
            AccountMapper.mapToAccounts(customerDTO.getAccountDTO(), account);

            // Save updated account
            accountRepository.save(account);
        }

        return true;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {

        // Find customer using mobile number
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
                );

        // Find account belonging to the customer
        Account account = accountRepository
                .findByCustomerId(customer.getCustomerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
                );

        // Delete the account first because it depends on the customer
        accountRepository.deleteById(account.getAccountNumber());

        // Delete the customer
        customerRepository.deleteById(account.getCustomerId());

        return true;
    }

    /**
     *
     * @param customer - Customer Object
     * @return the new account details
     */
    private Account createAccount(Customer customer) {
        Account account = new Account();
        account.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 10000L + new Random().nextInt(900000);

        account.setAccountNumber(randomAccNumber);
        account.setAccountType(AccountConstants.SAVINGS);
        account.setBranchAddress(AccountConstants.ADDRESS);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        return account;
    }
}
