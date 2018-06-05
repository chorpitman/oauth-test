package com.lunapps.services.impl;

import com.lunapps.controllers.dto.RatingCommentDto;
import com.lunapps.controllers.dto.UserAdminDto;
import com.lunapps.controllers.dto.UserAdminPageDto;
import com.lunapps.controllers.dto.UserFbDto;
import com.lunapps.controllers.dto.UserLoginDto;
import com.lunapps.controllers.dto.UserPasswordUpdateDto;
import com.lunapps.controllers.dto.UserProfileDto;
import com.lunapps.controllers.dto.adverts.park.car.CarDto;
import com.lunapps.controllers.dto.adverts.park.car.CarUpdateDto;
import com.lunapps.convertor.CarDtoConverter;
import com.lunapps.convertor.UserDtoConverter;
import com.lunapps.exception.user.UserException;
import com.lunapps.exception.user.UserRegistrationException;
import com.lunapps.models.Authority;
import com.lunapps.models.BetoJobProfile;
import com.lunapps.models.BetoParkProfile;
import com.lunapps.models.Car;
import com.lunapps.models.Gender;
import com.lunapps.models.PaymentSystem;
import com.lunapps.models.Rating;
import com.lunapps.models.User;
import com.lunapps.models.UserRole;
import com.lunapps.models.filter.UserAdminFilter;
import com.lunapps.repository.CarRepository;
import com.lunapps.repository.UserRepository;
import com.lunapps.repository.searchSpecification.SearchCriteria;
import com.lunapps.repository.searchSpecification.UserSpecification;
import com.lunapps.services.EmailService;
import com.lunapps.services.FacebookService;
import com.lunapps.services.FilterUtils;
import com.lunapps.services.TokenService;
import com.lunapps.services.UserService;
import com.lunapps.services.UserUtilService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static java.text.MessageFormat.format;

/**
 * Created by olegchorpita on 7/11/17.
 */

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    //todo move to properties file
    private static final String ALREADY_EXIST = "40400";
    private static final String DOES_NOT_EXIST = "40401";
    private static final String HAS_TO_CONFIRM_REG = "40402";
    private static final String WRONG_PASSWORD = "40403";
    private static final String PASSWORDS_ARE_NOT_EQUALS = "40404";
    private static final String NULL_OR_EMPTY = "40405";
    public static final int DEFAULT_PAGE_SIZE = 10;

    private final String USER_EMAIL_TOKEN = "userEmailToken";
    private final String USER_DB_TOKEN = "userDbToken";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FacebookService facebookService;
    @Autowired
    private UserUtilService userUtilService;
    @Autowired
    private UserDtoConverter converter;
    @Autowired
    private CarDtoConverter carDtoConverter;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private FilterUtils filterUtils;

    @Override
    @Transactional
    public void login(final UserLoginDto userLoginDto) {
        LOGGER.debug(format("About login process --> User with email: {0} and password: {1} try to login", userLoginDto.getEmail(), userLoginDto.getPassword()));
        Objects.nonNull(userLoginDto);
        User foundUser = userRepository.findByEmail(userLoginDto.getEmail());
        if (Objects.isNull(foundUser)) {
            throw new UserRegistrationException(format("user with email: {0} does not exist", userLoginDto.getEmail()), DOES_NOT_EXIST);
        }

        if (!encoder.matches(userLoginDto.getPassword(), foundUser.getPassword())) {
            throw new UserRegistrationException(format("user with email: {0} wrong password", userLoginDto.getEmail()), WRONG_PASSWORD);
        }

        if (Objects.equals(foundUser.getIsUserAccountActive(), Boolean.FALSE)) {
            throw new UserRegistrationException(format("user with email: {0} has to confirm registration", userLoginDto.getEmail()), HAS_TO_CONFIRM_REG);
        }
        foundUser.setLoginDate(ZonedDateTime.now());
        userRepository.save(foundUser);
    }

    @Override
    public UserProfileDto getUserById(Long userId) {
        LOGGER.debug("About process --> getUserById: {0}", userId);
        User user = userRepository.findById(userId);
        if (Objects.isNull(user)) {
            throw new UserException(format("user with id: {0} does not exist", userId));
        }
        return converter.convert(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User isUserExist(String email) {
        LOGGER.debug("About process --> isUserExist by email: {0}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void registration(final UserLoginDto userLoginDto) throws MessagingException {
        LOGGER.debug(format("About process --> registration: {0}", userLoginDto));
        User foundUser = userRepository.findByEmail(userLoginDto.getEmail());
        if (Objects.nonNull(foundUser)) {
            throw new UserRegistrationException(format("user with email: {0} already exist", foundUser.getEmail()), ALREADY_EXIST);
        } else {
            LOGGER.debug("Generate tokens for email");
            HashMap<String, String> mailToken = tokenService.generateMailToken(USER_EMAIL_TOKEN, USER_DB_TOKEN);
            User user = User.builder()
                    .email(userLoginDto.getEmail())
                    .password(encoder.encode(userLoginDto.getPassword()))
                    .isUserAccountActive(Boolean.FALSE)
                    .authorities(Collections.singleton(Authority.builder().userRole(UserRole.ROLE_USER).build()))
                    .emailToken(mailToken.get(USER_DB_TOKEN))
                    .gender(Gender.NOT_SELECTED)
                    .createdDate(ZonedDateTime.now())
                    .betoJobProfile(BetoJobProfile.builder().enable(Boolean.FALSE).jobAdverts(Collections.EMPTY_SET).build())
                    .betoParkProfile(BetoParkProfile.builder().enable(Boolean.FALSE).parkAdvert(Collections.EMPTY_SET).build())
                    .paymentSystem(PaymentSystem.NOT_SELECTED)
                    .balance(BigDecimal.ZERO)
                    .build();
            LOGGER.debug(format("Create and save new user with email tokens: {0}", user));
            userRepository.save(user);
            LOGGER.debug(format("Sent token for confirm registration: {0}", mailToken.get(USER_DB_TOKEN)));
            emailService.sendRegistrationEmail(user.getEmail(), user.getEmail(), mailToken.get(USER_EMAIL_TOKEN), Boolean.TRUE, Locale.ENGLISH);
        }
    }

    @Override
    @Transactional
    public User fbRegistration(final String fbAccessToken) {
        LOGGER.debug(format("About process --> user fbLogin: {0}", fbAccessToken));
        UserFbDto userFbDto = facebookService.getFbUser(fbAccessToken);
        User userByFbToken = userRepository.findByFbUserId(String.valueOf(userFbDto.getId()));
        if (Objects.isNull(userByFbToken)) {
            User user = converter.convertFbDtoToUser(userFbDto);
            return userRepository.save(user);
        } else {
            return userByFbToken;
        }
    }

    @Override
    @Transactional
    public User forgotPassword(final String email) throws MessagingException {
        LOGGER.debug(format("About process --> user forgotPassword: {0}", email));
        User foundUser = userRepository.findByEmail(email);
        if (Objects.isNull(foundUser)) {
            throw new UserRegistrationException(format("user with email: {0} does not exist", email), DOES_NOT_EXIST);
        }
        HashMap<String, String> mailToken = tokenService.generateMailToken(USER_EMAIL_TOKEN, USER_DB_TOKEN);
        foundUser.setEmailToken(mailToken.get(USER_DB_TOKEN));
        userRepository.save(foundUser);
        LOGGER.debug(format("sent confirm for reset password with email {0}", email));
        emailService.sendConfirmForResetPassword(foundUser.getEmail(), foundUser.getEmail(), mailToken.get(USER_EMAIL_TOKEN), Locale.ENGLISH);

        return foundUser;
    }

    @Override
    @Transactional
    public User confirmRegistration(final String token) {
        LOGGER.debug(format("About process --> user confirmRegistration with token: {0}", token));
        String encodedToken = new String(Base64.encodeBase64(DigestUtils.sha512(token)));
        User foundUser = userRepository.findByEmailToken(encodedToken);
        if (Objects.nonNull(foundUser)) {
            foundUser.setIsUserAccountActive(Boolean.TRUE);
            foundUser.setEmailToken(null);
            return userRepository.save(foundUser);
        }
        return null;
    }

    @Override
    @Transactional
    public void updateUser(final UserProfileDto userProfileDto) {
        LOGGER.debug(format("About process --> updateUser: {0}", userProfileDto));
        if (Objects.isNull(userProfileDto)) {
            throw new UserException(format("user profile can not be null: {0}", userProfileDto));
        }
        User foundUser = userRepository.findByEmail(userProfileDto.getEmail());
        if (Objects.isNull(foundUser)) {
            throw new UserException(format("user profile does not exist for user with email: {0}", userProfileDto.getEmail()));
        }
        foundUser.setFirstname(userProfileDto.getFirstname());
        foundUser.setLastname(userProfileDto.getLastname());
        foundUser.setNickname(userProfileDto.getNickname());
//        foundUser.setEmail(userProfileDto.getEmail());
//        foundUser.setIsUserAccountActive(userProfileDto.getIsUserAccountActive());
//        foundUser.setBalance(userProfileDto.getBalance());
        //todo профайл сетить
        foundUser.getBetoJobProfile().setEnable(userProfileDto.getBetoJobProfile().getEnable());
        foundUser.getBetoParkProfile().setEnable(userProfileDto.getBetoParkProfile().getEnable());

        foundUser.setGender(userProfileDto.getGender());
        foundUser.setAge(userProfileDto.getAge());
        foundUser.setUpdatedDate(ZonedDateTime.now());
        foundUser.setAddress(userProfileDto.getAddress());
        foundUser.setAvatarUrl(userProfileDto.getAvatarUrl());


        userRepository.save(foundUser);
    }

    @Override
    public List<UserAdminDto> getUsers() {
        List<User> allUsers = userRepository.findAll();

        List<UserAdminDto> adminDtos = new ArrayList<>();
        for (User user : allUsers) {
            adminDtos.add(converter.convertToAdminDto(user));
        }

        return adminDtos;
    }

    @Override
    @Transactional
    public void updatePassword(final UserPasswordUpdateDto updatePasswordDto) {
        LOGGER.debug(format("About process --> update password", updatePasswordDto));
        if (Objects.isNull(updatePasswordDto)) {
            throw new UserRegistrationException("User password can not be empty or null");
        }
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.isNull(currentUserEmail)) {
            throw new UserRegistrationException("Problem with user authentication");
        }
        User foundUser = userRepository.findByEmail(currentUserEmail);
        if (Objects.isNull(foundUser)) {
            throw new UserRegistrationException(format("user with email: {0} does not exist", currentUserEmail), DOES_NOT_EXIST);
        }
        if (!encoder.matches(updatePasswordDto.getPassword(), foundUser.getPassword())) {
            throw new UserRegistrationException(format("wrong passwords for email: {0}", currentUserEmail), WRONG_PASSWORD);
        } else {
            foundUser.setPassword(encoder.encode(updatePasswordDto.getNewPassword()));
            userRepository.save(foundUser);
        }
    }

    @Override
    @Transactional
    public void updateUDID(final String deviceUDID, final String userEmail) {
        LOGGER.debug("About process of updating user udid: '{}'" + deviceUDID);
        User foundUser = userRepository.findByEmail(userEmail);
        userUtilService.nullCheck(foundUser, userEmail);
        foundUser.setDeviceUDID(deviceUDID);
        userRepository.save(foundUser);
    }

    @Override
    @Transactional
    public void generateAndSendPassword(final User user) throws MessagingException {
        LOGGER.debug(format("Start generate password for user: {0}" + user));
        if (Objects.isNull(user)) {
            throw new UserRegistrationException(format("user can not be null or empty: {0}", user), NULL_OR_EMPTY);
        }
        String generatedPassword = new String(Base64.encodeBase64URLSafe(RandomStringUtils.randomAlphanumeric(8).getBytes()));
        user.setPassword(encoder.encode(generatedPassword));
        user.setEmailToken(null);
        userRepository.save(user);
        LOGGER.debug(format("Sent confirm for reset password with email: {0}", user.getEmail()));
        emailService.sendGeneratedPassword(user.getEmail(), user.getEmail(), generatedPassword, Locale.ENGLISH);
    }

    @Override
    public List<RatingCommentDto> getUserRate(final String email) {
        LOGGER.debug(format("About process --> get user tate by email: {0}", email));

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByEmail(currentUserEmail);
        userUtilService.nullCheck(foundUser, email);
        //get rate
        Set<Rating> ratings = foundUser.getUserRating();
        //get all users
        List<User> users = userRepository.findAll();

        List<RatingCommentDto> commentDtoList = new ArrayList<>();

        for (Rating rating : ratings) {
            Long feedbackAuthor = rating.getFeedbackAuthor();
            //найти автора
            User ratingAuthor = getUserById(users, feedbackAuthor);
            if (Objects.nonNull(ratingAuthor)) {
                RatingCommentDto dto = RatingCommentDto.builder()
                        .id(rating.getId())
                        .comment(rating.getComment())
                        .rating(rating.getRating())
                        .dealId(rating.getDealId())
                        .ratingType(rating.getRatingType())
                        .feedbackAuthor(rating.getFeedbackAuthor())
                        .feedbackAuthorFirstName(ratingAuthor.getFirstname())
                        .feedbackAuthorLastName(ratingAuthor.getLastname())
                        .feedbackAuthorAvatar(ratingAuthor.getAvatarUrl())
                        .created(rating.getCreated())
                        .build();
                commentDtoList.add(dto);
            }
        }

        return commentDtoList;
    }

    // ABOUT CARS
    //create car
    @Override
    public UserProfileDto createCar(final CarDto userNewCar) {
        LOGGER.debug(format("About process --> createCar: {0}", userNewCar));
        Objects.isNull(userNewCar);
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByEmail(currentUserEmail);
        if (Objects.isNull(foundUser))
            throw new UserException(format("user with email: {0} does not exist", currentUserEmail));
        BetoParkProfile betoParkProfile = foundUser.getBetoParkProfile();
        Set<Car> profileCar = betoParkProfile.getCar();
        if (profileCar.size() == 0) {
            userNewCar.setIsCarDefault(Boolean.TRUE);
            profileCar.add(carDtoConverter.convert(userNewCar));
            betoParkProfile.setCar(profileCar);
            foundUser.setBetoParkProfile(betoParkProfile);
            userRepository.save(foundUser);
        } else {
            if (userNewCar.getIsCarDefault() == Boolean.TRUE) {
                for (Car car : profileCar) {
                    car.setIsCarDefault(Boolean.FALSE);
                }
                profileCar.add(carDtoConverter.convert(userNewCar));
                betoParkProfile.setCar(profileCar);
                foundUser.setBetoParkProfile(betoParkProfile);
                userRepository.save(foundUser);
            } else {
                profileCar.add(carDtoConverter.convert(userNewCar));
                betoParkProfile.setCar(profileCar);
                foundUser.setBetoParkProfile(betoParkProfile);
                userRepository.save(foundUser);
            }
        }
        return converter.convert(userRepository.findByEmail(currentUserEmail));
    }

    @Override
    public void makeCarDefault(Long carId) {
        LOGGER.debug(format("About process --> makeCarDefault by id: {0}", carId));
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByEmail(currentUserEmail);
        if (Objects.isNull(foundUser))
            throw new UserException(format("user with email: {0} does not exist", currentUserEmail));
        BetoParkProfile betoParkProfile = foundUser.getBetoParkProfile();
        Set<Car> profileCar = betoParkProfile.getCar();
        if (profileCar.size() == 0) {
            throw new UserException(format("user with email: {0} does not have any car", currentUserEmail));
        }

        if (profileCar.size() == 1) {
            for (Car car : profileCar) {
                if (Objects.equals(car.getId(), carId)) {
                    car.setIsCarDefault(Boolean.TRUE);
                    userRepository.save(foundUser);
                } else
                    throw new UserException(format("car with id: {0} does not exist", carId));
            }
        }

        if (profileCar.size() > 1) {
            Set<Car> otherCars = new HashSet<>();
            for (Car car : profileCar) {
                if (Objects.equals(car.getId(), carId)) {
                    car.setIsCarDefault(Boolean.TRUE);
                } else {
                    otherCars.add(car);
                }
            }
            if (profileCar.size() == otherCars.size()) {
                throw new UserException(format("car with id: {0} does not exist", carId));
            } else {
                for (Car car : profileCar) {
                    Long id = car.getId();
                    for (Car otherCar : otherCars) {
                        if (Objects.equals(id, otherCar.getId())) {
                            car.setIsCarDefault(Boolean.FALSE);
                        }
                    }
                }
            }
            userRepository.save(foundUser);
        }
    }

    @Override
    public void deleteCar(final Long carId) {
        LOGGER.debug(format("About process --> deleteCar by id: {0}", carId));
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByEmail(currentUserEmail);
        if (Objects.isNull(foundUser))
            throw new UserException(format("user with email: {0} does not exist", currentUserEmail));
        BetoParkProfile betoParkProfile = foundUser.getBetoParkProfile();
        Set<Car> profileCar = betoParkProfile.getCar();
        if (profileCar.size() == 0) {
            throw new UserException(format("user with email: {0} does not have any car", currentUserEmail));
        }
        Car carForRemoving = null;
        for (Car car : profileCar) {
            if (Objects.equals(car.getId(), carId)) {
                carForRemoving = car;
            }
        }
        if (Objects.isNull(carForRemoving)) {
            throw new UserException(format("user does not have car with id: {0}", carId));
        }
        profileCar.remove(carForRemoving);
        carRepository.delete(carForRemoving);
        userRepository.save(foundUser);
    }

    @Override
    public UserProfileDto updateCar(CarUpdateDto carDto) {
        LOGGER.debug(format("About process --> updateCar with id: {0}", carDto.getId()));
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByEmail(currentUserEmail);
        if (Objects.isNull(foundUser))
            throw new UserException(format("user with email: {0} does not exist", currentUserEmail));
        BetoParkProfile betoParkProfile = foundUser.getBetoParkProfile();
        Set<Car> profileCar = betoParkProfile.getCar();
        if (profileCar.size() == 0) {
            throw new UserException(format("user with email: {0} does not have any car", currentUserEmail));
        }
        for (Car car : profileCar) {
            if (Objects.equals(car.getId(), carDto.getId())) {
                car.setCarBrand(carDto.getCarBrand());
                car.setModel(carDto.getModel());
                car.setVehicleType(carDto.getVehicleType());
                car.setYearOfProduction(carDto.getYearOfProduction());
                car.setCarLength(carDto.getCarLength());
                car.setCarColor(carDto.getCarColor());
                car.setCarNumber(carDto.getCarNumber());
                car.setIsCarDefault(carDto.getIsCarDefault());
                car.setCarPhoto(carDto.getCarPhoto());
            }
        }
        userRepository.save(foundUser);
        return converter.convert(foundUser);
    }

    @Override
    public CarUpdateDto getById(Long carId) {
        LOGGER.debug(format("About process --> get Car by id: {0}", carId));
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByEmail(currentUserEmail);
        if (Objects.isNull(foundUser))
            throw new UserException(format("user with email: {0} does not exist", currentUserEmail));
        BetoParkProfile betoParkProfile = foundUser.getBetoParkProfile();
        Set<Car> profileCar = betoParkProfile.getCar();
        if (profileCar.size() == 0) {
            throw new UserException(format("user with email: {0} does not have any car", currentUserEmail));
        }
        LOGGER.debug("About process update user car by id: {0}", carId);
        Car foundCar = null;
        for (Car car : profileCar) {
            if (Objects.equals(car.getId(), carId)) {
                foundCar = car;
            }
        }

        if (Objects.isNull(foundCar)) {
            throw new UserException(format("user does not have car with id: {0}", carId));
        }

        return carDtoConverter.convert(foundCar);
    }

    @Override
    public List<CarUpdateDto> getAllUserCars() {
        LOGGER.debug("About process --> getAllUserCars");
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByEmail(currentUserEmail);
        if (Objects.isNull(foundUser))
            throw new UserException(format("user with email: {0} does not exist", currentUserEmail));
        BetoParkProfile betoParkProfile = foundUser.getBetoParkProfile();
        Set<Car> profileCar = betoParkProfile.getCar();
        List<CarUpdateDto> dtoList = new ArrayList<>();
        for (Car car : profileCar) {
            dtoList.add(carDtoConverter.convert(car));
        }
        return dtoList;
    }

    @Override
    public BigDecimal getUserBalanceById(Long userId) {
        User foundUser = userRepository.findById(userId);
        userUtilService.nullCheck(foundUser, userId);
        return foundUser.getBalance();
    }

    @Override
    public BigDecimal getUserBalance(final String userEmail) {
        User foundUser = userRepository.findByEmail(userEmail);
        userUtilService.nullCheck(foundUser, userEmail);
        return foundUser.getBalance();
    }

    @Override
    public UserAdminPageDto filter(final UserAdminFilter filter, final Integer pageSize, final Integer pageNumber) {
        List<SearchCriteria> searchCriteria = filterUtils.getSearchCriteriaForUserFilter(filter);
        List<UserSpecification> specification = filterUtils.getUserSpecification(searchCriteria);
        Specifications<User> createdSpecs = filterUtils.buildSpecForUserRequest(specification);

        Pageable pageable = new PageRequest(checkNumber(pageNumber), checkPageSize(pageSize), new Sort(Sort.Direction.ASC, "id"));
        Page<User> users = userRepository.findAll(createdSpecs, pageable);
        UserAdminPageDto userAdminDto = buildUserAdminDto(users);

        return userAdminDto;
    }

    private BetoParkProfile getDefaultCar(final User foundUser, final UserProfileDto userProfileDto) {
        //getting cars from dto profile (car which user want add to profile)
        BetoParkProfile betoParkProfile = userProfileDto.getBetoParkProfile();
        Set<Car> cars = betoParkProfile.getCar();
        if (Objects.isNull(cars)) {
            return betoParkProfile;
        }
        if (foundUser.getBetoParkProfile().getCar().size() == 0 && cars.size() == 1) {
            for (Car car : cars) {
                car.setIsCarDefault(Boolean.TRUE);
            }
        } else {
            cars.addAll(foundUser.getBetoParkProfile().getCar());
        }
        betoParkProfile.setCar(cars);
        return betoParkProfile;
    }

    private UserAdminPageDto buildUserAdminDto(Page<User> users) {
        if (Objects.isNull(users) || users.getSize() == 0) {
            return null;
        }
        List<UserAdminDto> converted = new ArrayList<>();

        for (User user : users) {
            UserAdminDto dto = converter.convertToAdminDto(user);
            converted.add(dto);
        }

        if (converted.size() != 0) {
            return UserAdminPageDto
                    .builder()
                    .userAdminDto(converted)
                    .totalElements(users.getTotalElements())
                    .totalPages(users.getTotalPages())
                    .build();
        }
        return null;
    }

    private Integer checkNumber(final Integer pageNumber) {
        if (Objects.isNull(pageNumber) || pageNumber <= 0) {
            return 0;
        }
        return pageNumber;
    }

    private Integer checkPageSize(final Integer pageSize) {
        if (Objects.isNull(pageSize) || pageSize <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    private User getUserById(final List<User> userList, final Long id) {
        if (userList.size() == 0) {
            return null;
        }

        for (User user : userList) {
            if (Objects.equals(id, user.getId())) {
                return user;
            }
        }

        return null;
    }
}
