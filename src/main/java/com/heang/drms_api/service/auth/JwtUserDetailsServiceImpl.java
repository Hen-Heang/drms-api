package com.heang.drms_api.service.auth;


import com.heang.drms_api.common.api.ExitCode;
import com.heang.drms_api.config.JwtTokenUtil;
import com.heang.drms_api.dto.RegisterRequest;
import com.heang.drms_api.dto.RegisterResponse;
import com.heang.drms_api.exception.BusinessException;
import com.heang.drms_api.mapper.AuthUserMapper;
import com.heang.drms_api.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    private final AuthUserMapper authUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username = email
        AuthUser user = authUserMapper.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // bcrypt hashed
                .roles(user.getRole())        // ex: "DISTRIBUTOR" / "RETAILER"
                .build();
    }


    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
//        1, check duplicate email
        AuthUser authUser = authUserMapper.findByEmail(registerRequest.getEmail());
        if (authUser != null) {
            throw new BusinessException(ExitCode.EMAIL_ALREADY_EXISTS);
        }

//        2, Hash password
        String hashPassword   = passwordEncoder.encode(registerRequest.getPassword());

//        3, Map data into AuthUser entity
        authUser = modelMapper.map(registerRequest, AuthUser.class);
        authUser.setPassword(hashPassword);
        authUser.setStatus("ACTIVE");

//        4, Save into DB
        authUserMapper.insertUser(authUser);

        return RegisterResponse.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .fullName(authUser.getFullName())
                .role(authUser.getRole())
                .build();
    }
}
