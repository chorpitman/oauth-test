package com.lunapps.controllers;

import com.lunapps.controllers.dto.RatingCommentDto;
import com.lunapps.controllers.dto.UserAdminDto;
import com.lunapps.controllers.dto.UserForgotPasswordDto;
import com.lunapps.controllers.dto.UserPasswordUpdateDto;
import com.lunapps.controllers.dto.UserProfileDto;
import com.lunapps.models.User;
import com.lunapps.security.Authentication;
import com.lunapps.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Created by olegchorpita on 9/8/17.
 */
@Api(description = "Operations about User")
@Controller
@RequestMapping("/rest")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private Authentication authenticationService;

    @ApiOperation(value = "get user profile by user id", notes = "Controller provide process of getting user profile by user id")
    @RequestMapping(value = "/user/get/{userId}", method = RequestMethod.GET)
    @PreAuthorize(value = "hasAnyRole('ADMIN, USER')")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable(name = "userId") final Long userId) {
        LOGGER.debug("About process get user profile by id '{}'", userId);
        UserProfileDto userProfile = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }

    @ApiOperation(value = "update profile", notes = "Controller provide process of update user")
    @RequestMapping(value = "/user/profile-update", method = RequestMethod.PUT)
    @PreAuthorize(value = "hasAnyRole('ADMIN, USER')")
    public ResponseEntity<Void> updateUserProfile(@RequestBody @Valid final UserProfileDto dtoLogin) {
        LOGGER.debug("About process update user profile '{}'", dtoLogin);
        userService.updateUser(dtoLogin);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "user update password", notes = "Controller provide process of update user password")
    @RequestMapping(value = "/user/update-password", method = RequestMethod.POST)
    @PreAuthorize(value = "hasAnyRole('ADMIN, USER')")
    public ResponseEntity<Void> userUpdatePassword(@RequestBody @Valid final UserPasswordUpdateDto dto) throws MessagingException {
        LOGGER.debug("About process user update password '{}'", dto);
        userService.updatePassword(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "user forget password", notes = "Controller provide process of forgot user password")
    @RequestMapping(value = "/user/forgot-password", method = RequestMethod.POST)
    public ResponseEntity<Void> userForgotPassword(@RequestBody @Valid final UserForgotPasswordDto forgotPasswordDto) throws MessagingException {
        LOGGER.debug("About process user forget password '{}'", forgotPasswordDto.getEmail());
        User user = userService.forgotPassword(forgotPasswordDto.getEmail());
        if (Objects.nonNull(user)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ApiOperation(value = "return all users for admin", notes = "Controller provide process of return all users for admin")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserAdminDto>> getAllUsers() {
        LOGGER.debug("About process return all");
        List<UserAdminDto> users = userService.getUsers();
        if (users.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @ApiOperation(value = "get user balance by id", notes = "Controller provide process of getting user balance by user id")
    @RequestMapping(value = "/user/balance/{userId}", method = RequestMethod.GET)
    @PreAuthorize(value = "hasAnyRole('ADMIN, USER')")
    public ResponseEntity<BigDecimal> getUserBalanceById(@PathVariable(name = "userId") final Long userId) {
        LOGGER.debug("About process get user balance by id '{}'", userId);
        BigDecimal balance = userService.getUserBalanceById(userId);
        if (Objects.isNull(balance)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    @ApiOperation(value = "get user balance by oauth token", notes = "Controller provide process of getting user balance by oauth token")
    @RequestMapping(value = "/user/balance", method = RequestMethod.GET)
    @PreAuthorize(value = "hasAnyRole('ADMIN, USER')")
    public ResponseEntity<BigDecimal> getUserBalance() {
        LOGGER.debug("About process get user balance by email '{}'", authenticationService.getCurrentAuthUser());
        BigDecimal balance = userService.getUserBalance(authenticationService.getCurrentAuthUser());
        if (Objects.isNull(balance)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    @ApiOperation(value = "save user udid", notes = "Controller provide process of saving user udid")
    @RequestMapping(value = "/user/udid", method = RequestMethod.PUT)
    @PreAuthorize(value = "hasAnyRole('ADMIN, USER')")
    public ResponseEntity<Void> saveUserUDID(@RequestParam(name = "deviceUDID") final String deviceUDID) {
        LOGGER.debug("About process of saving user udid'{}'", authenticationService.getCurrentAuthUser());
        userService.updateUDID(deviceUDID, authenticationService.getCurrentAuthUser());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "get user rate with comments", notes = "Controller provide process of getting user rate with comments")
    @RequestMapping(value = "/user/rate", method = RequestMethod.POST)
    @PreAuthorize(value = "hasAnyRole('ADMIN, USER')")
    public ResponseEntity<List<RatingCommentDto>> getUserRate() {
        LOGGER.debug("About process get user rate and comment by email '{}'", authenticationService.getCurrentAuthUser());
        List<RatingCommentDto> userRate = userService.getUserRate(authenticationService.getCurrentAuthUser());
        if (userRate.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(userRate);
    }
}