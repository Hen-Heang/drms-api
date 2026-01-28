package com.heang.drms_api.auth.service;


import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.mapper.AuthUserMapper;
import com.heang.drms_api.auth.mapper.OtpMapper;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.JwtChangePasswordRequest;
import com.heang.drms_api.auth.model.Otp;
import com.heang.drms_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@NullMarked
@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService, JwtUserDetailsService {


    private final AuthUserMapper authUserMapper;
    private final OtpMapper otpMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;



    Boolean checkDuplicatePhone(String phone, Integer roleId){
        boolean isExistInUserPhone = false;
        boolean isExistInUserInfo = false;

        if (roleId == 1) {
            isExistInUserPhone = authUserMapper.checkPhoneNumberFromDistributorPhone(phone);
            isExistInUserInfo = authUserMapper.checkPhoneNumberFromDistributorDetail(phone);
        } else {
            isExistInUserPhone = authUserMapper.checkPhoneNumberFromRetailerPhone(phone);
            isExistInUserInfo = authUserMapper.checkPhoneNumberFromRetailerDetail(phone);
        }
        return isExistInUserPhone || isExistInUserInfo;
    }

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validateEmail(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = authUserMapper.findDistributorUserByEmail(email);
        if (user == null){
            user = authUserMapper.findRetailerUserByEmail(email);
        }
        if (user == null){
            throw new BadRequestException("Invalid email address. Please input valid email address.");
        }
        return user;
    }

    @Override
    public AppUserDto insertUser(AppUserRequest appUserRequest) {
        if (!(appUserRequest.getRoleId().equals(1)|| appUserRequest.getRoleId().equals(2))){
            throw new BadRequestException("Invalid roleId.");
        }
        if (appUserRequest.getEmail().isBlank()){
            throw new BadRequestException("Email can not be null");
        }
        if (!(validateEmail(appUserRequest.getEmail()))){
            throw new BadRequestException("Please follow email format.");
        }
        if (appUserRequest.getPassword().isBlank()){
            throw new BadRequestException("Password can not be null");
        }
        appUserRequest.setPassword(passwordEncoder.encode((appUserRequest.getPassword())));
        AppUser appUser = null;
        AppUser checkDuplicate = authUserMapper.findDistributorUserByEmail(appUserRequest.getEmail());
        AppUser checkDuplicateRetailer = authUserMapper.findRetailerUserByEmail(appUserRequest.getEmail());
        if (checkDuplicate != null || checkDuplicateRetailer != null){
            throw new BadRequestException("Email is already in use.");
        }
        if (appUserRequest.getRoleId() == 1) {
            if (appUserRequest.getPassword().equals("string") || appUserRequest.getPassword().isBlank()) {
                throw new BadRequestException("Invalid password");
            }
            appUser = authUserMapper.insertDistributorUser(appUserRequest);
        }else if (appUserRequest.getRoleId() == 2){

            if (appUserRequest.getPassword().equals("string") || appUserRequest.getPassword().isBlank()) {
                throw new BadRequestException("Invalid password");
            }
            appUser = authUserMapper.insertRetailerUser(appUserRequest);
        }
        return modelMapper.map(appUser, AppUserDto.class);
    }

    @Override
    public boolean getVerifyEmail(String email) {
        return authUserMapper.getVerifyDistributorEmail(email);
    }

    @Override
    public AppUserDto changePassword(JwtChangePasswordRequest request) throws NotFoundException {
        boolean isDistributor = true;
        AppUser appUser = authUserMapper.findDistributorUserByEmail(request.getEmail());
        if (appUser == null){
            isDistributor = false;
            appUser = authUserMapper.findRetailerUserByEmail(request.getEmail());
        }
        if (appUser == null){
            throw new NotFoundException("Not found. Invalid email.");
        }
        // verify password with encrypted password
        if (!(passwordEncoder.matches(request.getOldPassword(), appUser.getPassword()))){
            throw new NotFoundException("Old password is incorrect. Please input correct password.");
        }
        // if match encrypt new password and update database password
        request.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
        AppUser newAppUser = new AppUser();
        if (isDistributor){
            newAppUser = authUserMapper.updateDistributorUser(request);
        } else {
            newAppUser = authUserMapper.updateRetailerUser(request);
        }
        return modelMapper.map(newAppUser, AppUserDto.class);
    }

    @Override
    public String forgetPassword(Integer otp, String email, String newPassword) throws NotFoundException {
        // check if user is existing
        boolean isDistributor = true;
        Otp otpObj = null;
        AppUser appUser = authUserMapper.findDistributorUserByEmail(email);
        otpObj = otpMapper.getDistributorOtpByEmail(email);
        if (appUser == null || otpObj == null){
            isDistributor = false;
            appUser = authUserMapper.findRetailerUserByEmail(email);
            otpObj = otpMapper.getRetailerOtpByEmail(email);
        }
        if (appUser == null){
            throw new NotFoundException("Not found. Invalid email.");
        }
        if (otpObj == null){
            throw new BadRequestException("This OTP does not exist");
        }
        // check if request and database of OTP matches
        if (!Objects.equals(appUser.getEmail(), otpObj.getEmail())) {
            throw new BadRequestException("Email not match");
        } else if (!Objects.equals(otpObj.getOtpCode(), otp)) {
            throw new BadRequestException("OTP code not match");
        }
        // check timeout of 3 minutes
        else if (!lessThan3MinutesCheck(otpObj.getCreatedDate())) {
            throw new BadRequestException("OTP Expired");
        }
        // update new password
        String updatedPassword = "null";
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        if (isDistributor) {
            updatedPassword = authUserMapper.updateForgetDistributorUser(email, encodedNewPassword);
        } else {
            updatedPassword = authUserMapper.updateForgetRetailerUser(email, encodedNewPassword);
        }
        if (Objects.equals(updatedPassword, "null")){
            throw new BadRequestException("Failed to update new password");
        }
        return "New password updated. Your new password is: "+ newPassword;
    }


    public Integer getRoleIdByMail(String email) {
        return authUserMapper.getRoleIdByMail(email);
    }
    public Boolean lessThan3MinutesCheck(Date createdDate) {
        Date currentDate = new Date();
        long diffInMillis = Math.abs(currentDate.getTime() - createdDate.getTime());
        long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diffInMinutes < 3;
    }

    public Integer getUserIdByMail(String email) {
        return authUserMapper.getUserIdByMailDistributor(email);
    }
}
