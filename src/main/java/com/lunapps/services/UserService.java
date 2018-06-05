package com.lunapps.services;

import com.lunapps.controllers.dto.RatingCommentDto;
import com.lunapps.controllers.dto.UserAdminDto;
import com.lunapps.controllers.dto.UserAdminPageDto;
import com.lunapps.controllers.dto.UserLoginDto;
import com.lunapps.controllers.dto.UserPasswordUpdateDto;
import com.lunapps.controllers.dto.UserProfileDto;
import com.lunapps.controllers.dto.adverts.park.car.CarDto;
import com.lunapps.controllers.dto.adverts.park.car.CarUpdateDto;
import com.lunapps.models.User;
import com.lunapps.models.filter.UserAdminFilter;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by olegchorpita on 7/11/17.
 */
public interface UserService {
    UserProfileDto getUserById(final Long userId);

    List<UserAdminDto> getUsers();

    User isUserExist(final String email);

    User findByEmail(final String email);

    void login(final UserLoginDto userLoginDto);

    void registration(final UserLoginDto userDto) throws MessagingException;

    User fbRegistration(final String fbAccessToken);

    User forgotPassword(final String email) throws MessagingException;

    User confirmRegistration(final String token);

    void updateUser(final UserProfileDto userProfileDto);

    void updatePassword(final UserPasswordUpdateDto dto);

    void updateUDID(final String deviceUDID, final String userEmail);

    void generateAndSendPassword(final User userEmail) throws MessagingException;

    List<RatingCommentDto> getUserRate(final String email);

    // about users cars
    UserProfileDto createCar(final CarDto car);

    UserProfileDto updateCar(final CarUpdateDto carDto);

    void deleteCar(final Long carId);

    void makeCarDefault(final Long carId);

    CarUpdateDto getById(final Long carId);

    List<CarUpdateDto> getAllUserCars();

    BigDecimal getUserBalanceById(final Long userId);

    BigDecimal getUserBalance(final String email);

    //filter for sysadmin
    UserAdminPageDto filter(final UserAdminFilter filter, final Integer pageSize, final Integer pageNumber);

//    void sendNotification() throws CommunicationException, KeystoreException, JSONException;
}
