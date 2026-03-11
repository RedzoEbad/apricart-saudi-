package com.apricart.consumer.security.mapper.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Customer.CustomerBuilder;
import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.security.dto.request.RegistrationRequest;
import com.apricart.consumer.security.mapper.CustomerMapper;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-01T15:08:35+0500",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 16.0.2 (Oracle Corporation)"
)
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer convertToUser(RegistrationRequest registrationRequest) {
        if ( registrationRequest == null ) {
            return null;
        }

        CustomerBuilder user = Customer.builder();

        user.name( registrationRequest.getName() );
        user.username( registrationRequest.getUserName() );
        user.password( registrationRequest.getPassword() );
        user.email( registrationRequest.getEmail() );
        user.phoneNumber( registrationRequest.getPhoneNumber() );

        return user.build();
    }

    @Override
    public AuthenticatedUserDto convertToAuthenticatedUserDto(Customer user) {
        if ( user == null ) {
            return null;
        }

        AuthenticatedUserDto authenticatedUserDto = AuthenticatedUserDto
                .builder()
                .username(user.getUsername())
                .name(user.getName())
                .userRole(user.getUserRole())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();

        return authenticatedUserDto;
    }

    @Override
    public Customer convertToUser(AuthenticatedUserDto authenticatedUserDto) {
        if ( authenticatedUserDto == null ) {
            return null;
        }

        CustomerBuilder user = Customer.builder();

        user.name( authenticatedUserDto.getName() );
        user.username( authenticatedUserDto.getUsername() );
        user.email(authenticatedUserDto.getEmail());
        user.password( authenticatedUserDto.getPassword() );
        user.userRole( authenticatedUserDto.getUserRole() );

        return user.build();
    }

}
