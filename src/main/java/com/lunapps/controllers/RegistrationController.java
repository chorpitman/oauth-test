package com.lunapps.controllers;

import com.lunapps.controllers.dto.IsEntityExistDto;
import com.lunapps.controllers.dto.UserFbAccessTokenDto;
import com.lunapps.controllers.dto.UserFbDto;
import com.lunapps.controllers.dto.UserIsEmailExistDto;
import com.lunapps.controllers.dto.UserLoginDto;
import com.lunapps.convertor.UserDtoConverter;
import com.lunapps.models.User;
import com.lunapps.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Objects;

/**
 * Created by olegchorpita on 7/21/17.
 */
@Api(description = "Operations about Registration")
@Controller
@RequestMapping("/rest")
public class RegistrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    //    @Autowired
//    private Authentication authentication;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDtoConverter converter;

    //Should save user in db and send confirmation pass to email
    @ApiOperation(value = "user registration", notes = "Controller provide process of user registration")
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Void> registerUserAccount(@RequestBody @Valid final UserLoginDto dtoLogin) throws MessagingException {
        LOGGER.debug("About process registration '{}'", dtoLogin);
        userService.registration(dtoLogin);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Should retrieve OK and access and refresh tokens if user send valid confirmation password
    @ApiOperation(value = "confirm user email", notes = "Controller provide process of confirm user email")
    @RequestMapping(value = "/registration-confirm/{mailToken}", method = RequestMethod.GET)
    public ResponseEntity<Void> confirmRegistration(@PathVariable(name = "mailToken") final String mailToken) {
        LOGGER.debug("About process confirm registration '{}'", mailToken);
        User user = userService.confirmRegistration(mailToken);
        if (Objects.isNull(user))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Should retrieve OK and access and refresh tokens if user send valid confirmation password
    @ApiOperation(value = "confirm reset user password", notes = "Controller provide process of confirm reset user password")
    @RequestMapping(value = "/resetPassword-confirm/{mailToken}", method = RequestMethod.GET)
    public ResponseEntity<Void> confirmResetPassword(@PathVariable(name = "mailToken") final String mailToken) throws MessagingException {
        LOGGER.debug("About process confirm registration '{}'", mailToken);
        User user = userService.confirmRegistration(mailToken);
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.generateAndSendPassword(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Should retrieve access and refresh tokens if user provide valid credential
    @ApiOperation(value = "login user", notes = "Controller provide process of login user. User should be already registered")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> login(@RequestBody @Valid final UserLoginDto dtoLogin) {
        LOGGER.debug("About process login '{}'", dtoLogin);
        userService.login(dtoLogin);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Should retrieve is user already exist
    @ApiOperation(value = "checking user existing", notes = "Controller check is user already exist")
    @RequestMapping(value = "/isUserExist", method = RequestMethod.POST)
    public ResponseEntity<IsEntityExistDto> isUserExist(@RequestBody @Valid final UserIsEmailExistDto user) {
        LOGGER.debug("About process which check is user exist '{}'", user.getEmail());
        User foundUser = userService.isUserExist(user.getEmail());
        if (Objects.nonNull(foundUser))
            return ResponseEntity.status(HttpStatus.OK).body(IsEntityExistDto.builder().isEntityExist(Boolean.TRUE).build());
        return ResponseEntity.status(HttpStatus.OK).body(IsEntityExistDto.builder().isEntityExist(Boolean.FALSE).build());
    }

    //Should registered fb user
    @ApiOperation(value = "facebook registration", notes = "Controller provide process registration using fb access token")
    @RequestMapping(value = "/fb-login/", method = RequestMethod.POST)
    public ResponseEntity<UserFbDto> fbLogin(final @RequestBody UserFbAccessTokenDto fbAccessToken) {
        User user = userService.fbRegistration(fbAccessToken.getFbAccessToken());
        if (Objects.nonNull(user)) {
            return ResponseEntity.status(HttpStatus.OK).body(converter.convertUserToFbDto(user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}