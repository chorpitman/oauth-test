package com.lunapps.convertor.advert;

import com.lunapps.controllers.dto.adverts.park.AdvertParkCreateDto;
import com.lunapps.controllers.dto.adverts.park.AdvertParkDto;
import com.lunapps.controllers.dto.adverts.park.AdvertParkUpdateDto;
import com.lunapps.controllers.dto.deals.DealDetailDto;
import com.lunapps.controllers.dto.deals.DealParkAuthorDto;
import com.lunapps.exception.user.UserException;
import com.lunapps.models.AdvertStatus;
import com.lunapps.models.Car;
import com.lunapps.models.ParkAdvert;
import com.lunapps.models.User;
import com.lunapps.models.deal.Deal;
import com.lunapps.repository.CarRepository;
import com.lunapps.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class AdvertParkDtoConverter {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepository carRepository;


    public ParkAdvert convert(final AdvertParkDto dto) {
        ParkAdvert parkAdvert = ParkAdvert.builder()
                .betoParkProfileId(dto.getBetoParkProfileId())
                .betoParkProfileCarId(dto.getBetoParkProfileCarId())
                .carLength(dto.getCarLength())
//                .carWidth(dto.getCarWidth())
                .availabilityTimeFrom(dto.getAvailabilityTimeFrom())
                .availabilityTimeTo(dto.getAvailabilityTimeFrom().plusMinutes(dto.getWaitingTime()))
                .parkingLocation(dto.getParkingLocation())
                .parkingCharge(dto.getParkingCharge())
                .userRate(dto.getUserRate())
                .parkingType(dto.getParkingType())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .updateTime(dto.getUpdateTime())
                .createTime(ZonedDateTime.now())
                .build();
        return parkAdvert;
    }

    public ParkAdvert convert(final AdvertParkCreateDto dto) {
        ParkAdvert parkAdvert = ParkAdvert.builder()
                .betoParkProfileId(dto.getBetoParkProfileId())
                .betoParkProfileCarId(dto.getBetoParkProfileCarId())
                .carLength(dto.getCarLength())
//                .carWidth(dto.getCarWidth())
                .availabilityTimeFrom(dto.getAvailabilityTimeFrom())
                .advertStatus(AdvertStatus.ENABLE)
                .availabilityTimeTo(dto.getAvailabilityTimeTo())
                .waitingTime(dto.getWaitingTime())
                .parkingLocation(dto.getParkingLocation())
                .parkingCharge(dto.getParkingCharge())
                .userRate(dto.getUserRate())
                .parkingType(dto.getParkingType())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .updateTime(null)
                .createTime(ZonedDateTime.now())
                .build();
        return parkAdvert;
    }

    public ParkAdvert convert(final AdvertParkUpdateDto dto) {
        ParkAdvert parkAdvert = ParkAdvert.builder()
                .id(dto.getId())
                .betoParkProfileId(dto.getBetoParkProfileId())
                .betoParkProfileCarId(dto.getBetoParkProfileCarId())
                .carLength(dto.getCarLength())
//                .carWidth(dto.getCarWidth())
                .availabilityTimeFrom(dto.getAvailabilityTimeFrom())
                .availabilityTimeTo(dto.getAvailabilityTimeTo())
                .waitingTime(dto.getWaitingTime())
                .parkingLocation(dto.getParkingLocation())
                .parkingCharge(dto.getParkingCharge())
                .userRate(dto.getUserRate())
                .parkingType(dto.getParkingType())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .updateTime(ZonedDateTime.now())
                .build();
        return parkAdvert;
    }

    public AdvertParkDto convert(final ParkAdvert parkAdvert, final User user) {
        Car car = carRepository.findOne(parkAdvert.getBetoParkProfileCarId());
        User advertOwner = userRepository.findUserByParkAdvertId(parkAdvert.getId());

        AdvertParkDto advertForDb = AdvertParkDto.builder()
                .id(parkAdvert.getId())
                .betoParkProfileId(parkAdvert.getBetoParkProfileId())
                .betoParkProfileCarId(parkAdvert.getBetoParkProfileCarId())
                .carLength(parkAdvert.getCarLength())
//                .carWidth(parkAdvert.getCarWidth())
                .availabilityTimeFrom(parkAdvert.getAvailabilityTimeFrom())
                .availabilityTimeTo(parkAdvert.getAvailabilityTimeTo())
                .waitingTime(parkAdvert.getWaitingTime())
                .parkingLocation(parkAdvert.getParkingLocation())
                .parkingCharge(parkAdvert.getParkingCharge())
                .userRate(parkAdvert.getUserRate())
                .waitingTime(parkAdvert.getWaitingTime())
                .parkingType(parkAdvert.getParkingType())
                .latitude(parkAdvert.getLatitude())
                .longitude(parkAdvert.getLongitude())
                .updateTime(parkAdvert.getUpdateTime())
                .createTime(parkAdvert.getCreateTime())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .avatarUrl(user.getAvatarUrl())
                .car(car)
                .isCurrentUserAdvert(isUserOwnerParkAdvert(user.getId(), advertOwner.getId()))
                .build();
        return advertForDb;
    }

    public AdvertParkDto convert(final ParkAdvert parkAdvert, final Car car, final User foundUser) {
        AdvertParkDto advertForDb = AdvertParkDto.builder()
                .id(parkAdvert.getId())
                .betoParkProfileId(parkAdvert.getBetoParkProfileId())
                .betoParkProfileCarId(parkAdvert.getBetoParkProfileCarId())
                .carLength(parkAdvert.getCarLength())
//                .carWidth(parkAdvert.getCarWidth())
                .availabilityTimeFrom(parkAdvert.getAvailabilityTimeFrom())
                .availabilityTimeTo(parkAdvert.getAvailabilityTimeTo())
                .waitingTime(parkAdvert.getWaitingTime())
                .parkingLocation(parkAdvert.getParkingLocation())
                .parkingCharge(parkAdvert.getParkingCharge())
                .userRate(parkAdvert.getUserRate())
                .waitingTime(parkAdvert.getWaitingTime())
                .parkingType(parkAdvert.getParkingType())
                .latitude(parkAdvert.getLatitude())
                .longitude(parkAdvert.getLongitude())
                .updateTime(parkAdvert.getUpdateTime())
                .createTime(parkAdvert.getCreateTime())
                .car(car)
                .firstname(foundUser.getFirstname())
                .lastname(foundUser.getLastname())
                .avatarUrl(foundUser.getAvatarUrl())
                //todo added changes
                .isCurrentUserAdvert(Boolean.TRUE)
                .build();

        return advertForDb;
    }

    //for test
//    public List<AdvertParkDto> convertModelsToDtos(final List<ParkAdvert> parkAdverts) {
//        if (parkAdverts.size() == 0) return Collections.EMPTY_LIST;
//        List<AdvertParkDto> parkDtos = new ArrayList<>();
//
//        for (ParkAdvert parkAdvert : parkAdverts) {
//            AdvertParkDto convert = convert(parkAdvert);
//            parkDtos.add(convert);
//        }
//        return parkDtos;
//    }

    //for test
//    public List<ParkAdvert> convertDtosToModel(final Collection<AdvertParkDto> advertsDtos) {
//        if (advertsDtos.size() == 0) return Collections.EMPTY_LIST;
//        List<ParkAdvert> advertsList = new ArrayList<>();
//        for (AdvertParkDto dto : advertsDtos) {
//            advertsList.add(convert(dto));
//        }
//        return advertsList;
//    }

    public List<AdvertParkDto> convertToDtoWithAddInfoAboutParkingOwner(final Collection<ParkAdvert> parkAdvertList, final User currentUser) {
        if (parkAdvertList.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        ArrayList<AdvertParkDto> parkAdvertDtoList = new ArrayList<>();
        for (ParkAdvert advert : parkAdvertList) {
            User user = userRepository.findUserByParkProfileId(advert.getBetoParkProfileId());
            Car car = carRepository.findOne(advert.getBetoParkProfileCarId());

            if (Objects.equals(user.getId(), currentUser.getId())) {
                AdvertParkDto dto = AdvertParkDto.builder()
                        .id(advert.getId())
                        .betoParkProfileId(advert.getBetoParkProfileId())
                        .betoParkProfileCarId(advert.getBetoParkProfileCarId())
                        .availabilityTimeFrom(advert.getAvailabilityTimeFrom())
                        .availabilityTimeTo(advert.getAvailabilityTimeTo())
                        .parkingLocation(advert.getParkingLocation())
                        .parkingCharge(advert.getParkingCharge())
                        .userRate(advert.getUserRate())
                        .waitingTime(advert.getWaitingTime())
                        .parkingType(advert.getParkingType())
                        .latitude(advert.getLatitude())
                        .longitude(advert.getLongitude())
                        .carLength(advert.getCarLength())
                        .updateTime(advert.getUpdateTime())
                        .createTime(advert.getCreateTime())
                        //additional fields
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .avatarUrl(user.getAvatarUrl())
                        .car(car)
                        .isCurrentUserAdvert(Boolean.TRUE)
                        .build();

                parkAdvertDtoList.add(dto);
                continue;
            }
            AdvertParkDto dto = AdvertParkDto.builder()
                    .id(advert.getId())
                    .betoParkProfileId(advert.getBetoParkProfileId())
                    .betoParkProfileCarId(advert.getBetoParkProfileCarId())
                    .availabilityTimeFrom(advert.getAvailabilityTimeFrom())
                    .availabilityTimeTo(advert.getAvailabilityTimeTo())
                    .parkingLocation(advert.getParkingLocation())
                    .parkingCharge(advert.getParkingCharge())
                    .userRate(advert.getUserRate())
                    .waitingTime(advert.getWaitingTime())
                    .parkingType(advert.getParkingType())
                    .latitude(advert.getLatitude())
                    .longitude(advert.getLongitude())
                    .carLength(advert.getCarLength())
                    .updateTime(advert.getUpdateTime())
                    .createTime(advert.getCreateTime())
                    //additional fields
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .avatarUrl(user.getAvatarUrl())
                    .car(car)
                    .isCurrentUserAdvert(Boolean.FALSE)
                    .build();

            parkAdvertDtoList.add(dto);
        }
        return parkAdvertDtoList;
    }

    public DealParkAuthorDto convertAuthorParkAdvertToCamperDetail(final ParkAdvert parkAdvert, final User author) {
        Car car = carRepository.findOne(parkAdvert.getBetoParkProfileCarId());

        return DealParkAuthorDto.builder()
                .advertId(parkAdvert.getId())
                .betoParkProfileId(parkAdvert.getBetoParkProfileId())
                .betoParkProfileCarId(parkAdvert.getBetoParkProfileCarId())
                .availabilityTimeFrom(parkAdvert.getAvailabilityTimeFrom())
                .availabilityTimeTo(parkAdvert.getAvailabilityTimeTo())
                .parkingLocation(parkAdvert.getParkingLocation())
                .parkingCharge(parkAdvert.getParkingCharge())
                .userRate(parkAdvert.getUserRate())
                .waitingTime(parkAdvert.getWaitingTime())
                .parkingType(parkAdvert.getParkingType())
                .latitude(parkAdvert.getLatitude())
                .longitude(parkAdvert.getLongitude())
                .carLength(parkAdvert.getCarLength())
                .updated(parkAdvert.getUpdateTime())
                .created(parkAdvert.getCreateTime())
                .advertStatus(parkAdvert.getAdvertStatus())
                .car(car)
                .firstname(author.getFirstname())
                .lastname(author.getLastname())
                .avatarUrl(author.getAvatarUrl())
                .build();
    }

    public DealParkAuthorDto convertParkAdvertToCamperDetail(final ParkAdvert parkAdvert, final Collection<Deal> deals) {
        User author = userRepository.findUserByParkAdvertId(parkAdvert.getId());
        Car car = carRepository.findOne(parkAdvert.getBetoParkProfileCarId());

        if (Objects.isNull(author)) {
            throw new UserException("user does not exist");
        }

        return DealParkAuthorDto.builder()
                .advertId(parkAdvert.getId())
                .betoParkProfileId(parkAdvert.getBetoParkProfileId())
                .betoParkProfileCarId(parkAdvert.getBetoParkProfileCarId())
                .availabilityTimeFrom(parkAdvert.getAvailabilityTimeFrom())
                .availabilityTimeTo(parkAdvert.getAvailabilityTimeTo())
                .parkingLocation(parkAdvert.getParkingLocation())
                .parkingCharge(parkAdvert.getParkingCharge())
                .userRate(parkAdvert.getUserRate())
                .waitingTime(parkAdvert.getWaitingTime())
                .parkingType(parkAdvert.getParkingType())
                .latitude(parkAdvert.getLatitude())
                .longitude(parkAdvert.getLongitude())
                .carLength(parkAdvert.getCarLength())
                .updated(parkAdvert.getUpdateTime())
                .created(parkAdvert.getCreateTime())
                .advertStatus(parkAdvert.getAdvertStatus())
                .firstname(author.getFirstname())
                .lastname(author.getLastname())
                .avatarUrl(author.getAvatarUrl())
                .car(car)
                //deal
                .deals(convertDeals(deals, author))
                .build();
    }

    private List<DealDetailDto> convertDeals(final Collection<Deal> dealList, final User author) {
        if (dealList.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<DealDetailDto> dtos = new ArrayList<>();
        for (Deal deal : dealList) {
            User client = userRepository.getFetchUser(deal.getLaborerId());

            DealDetailDto dto = DealDetailDto.builder()
                    .id(deal.getId())
                    .authorFirstName(author.getFirstname())
                    .authorLastName(author.getLastname())
                    .authorAvatar(author.getAvatarUrl())
                    .clientFirstName(client.getFirstname())
                    .clientLastName(client.getLastname())
                    .clientAvatar(client.getAvatarUrl())
                    .clientRatings(client.getUserRating())
                    .dealType(deal.getDealType())
                    .dealStatus(deal.getDealStatus())
                    .dealInfo(deal.getDealInfo())
                    .amountDeal(deal.getAmountDeal())
                    .laborerCommission(deal.getLaborerCommission())
                    .homeyCommission(deal.getHomeyCommission())
                    .isOwnerAccept(deal.getIsOwnerAccept())
                    .isLaborerAccept(deal.getIsLaborerAccept())
                    .created(deal.getCreated())
                    .updated(deal.getUpdated())
                    .auctionList(deal.getAuctionList())
                    .claimList(deal.getClaimList())
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }

    private Boolean isUserOwnerParkAdvert(final Long userId, final Long ownerId) {
        if (Objects.equals(userId, ownerId)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}