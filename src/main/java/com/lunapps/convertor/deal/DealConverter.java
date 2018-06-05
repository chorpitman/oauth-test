package com.lunapps.convertor.deal;

import com.lunapps.controllers.dto.auction.AuctionUpdateDto;
import com.lunapps.controllers.dto.deals.DealOpenDto;
import com.lunapps.controllers.dto.deals.DealParkOpenDto;
import com.lunapps.models.Category;
import com.lunapps.models.JobAdvert;
import com.lunapps.models.ParkAdvert;
import com.lunapps.models.Photo;
import com.lunapps.models.SubCategory;
import com.lunapps.models.User;
import com.lunapps.models.deal.Deal;
import com.lunapps.models.deal.DealType;
import com.lunapps.repository.CarRepository;
import com.lunapps.repository.CategoryRepository;
import com.lunapps.repository.JobAdvertRepository;
import com.lunapps.repository.ParkAdvertRepository;
import com.lunapps.repository.SubCategoryRepository;
import com.lunapps.repository.UserRepository;
import com.lunapps.services.AdvertUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class DealConverter {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobAdvertRepository jobAdvertRepository;
    @Autowired
    private AdvertUtilService advertUtilService;
    @Autowired
    private ParkAdvertRepository parkAdvertRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CarRepository carRepository;


    public DealOpenDto convertDealToDealUpdDto(final Deal deal) {
        if (Objects.equals(DealType.BETOJOB, deal.getDealType())) {
            JobAdvert ja = jobAdvertRepository.findOne(deal.getServiceId());
            advertUtilService.nullCheckJA(ja, deal.getServiceId());

            return builder(deal, ja.getDescription(), getCategoryName(ja.getJobCategoryId()),
                    getSubCategoryName(ja.getJobSubCategoryId()), ja.getPhotoList(),
                    ja.getIsPhotoRequestConfirmationNeed(), ja.getIsJobDonePhotoDownloaded());
        }
        //betopark
        return null;
    }

    public DealParkOpenDto convertDealToDealParkUpdDto(final Deal deal) {
        ParkAdvert pa = parkAdvertRepository.findOne(deal.getServiceId());
        advertUtilService.nullCheckJA(pa, deal.getServiceId());
        DealParkOpenDto openDto = builder(deal, pa);
        return openDto;
    }

    private DealOpenDto builder(final Deal deal, final String description, final String jobCategoryId,
                                final String subCategory, final Set<Photo> photoList,
                                final Boolean isPhotoRequestConfirmationNeed, final Boolean isJobDonePhotoDownloaded) {
        return DealOpenDto.builder()
                .id(deal.getId())
                .homeyId(deal.getHomeyId())
                .homeyFirstName(userRepository.findById(deal.getHomeyId()).getFirstname())
                .homeyLastName(userRepository.findById(deal.getHomeyId()).getLastname())
                .homeyAvatar(userRepository.findOne(deal.getHomeyId()).getAvatarUrl())
                .laborerId(deal.getLaborerId())
                .laborerAvatar(userRepository.findById(deal.getLaborerId()).getAvatarUrl())
                .serviceId(deal.getServiceId())
                .serviceDescription(description)
                .serviceCategory(jobCategoryId)
                .subCategory(subCategory)
                .advertPhoto(photoList)
                .dealType(deal.getDealType())
                .dealStatus(deal.getDealStatus())
                .dealInfo(deal.getDealInfo())
                .amountDeal(deal.getAmountDeal())
                .laborerCommission(deal.getLaborerCommission())
                .homeyCommission(deal.getHomeyCommission())
                .historyOfNegotiation(deal.getHistoryOfNegotiation())
                .isOwnerAccept(deal.getIsOwnerAccept())
                .isLaborerAccept(deal.getIsLaborerAccept())
                .created(deal.getCreated())
                .updated(deal.getUpdated())
                .auctionList(buildAuctionDto(deal))
                .isPhotoRequestConfirmationNeed(isPhotoRequestConfirmationNeed)
                .isJobDonePhotoDownloaded(isJobDonePhotoDownloaded)
                .claimList(deal.getClaimList())
                .build();
    }

    private DealParkOpenDto builder(final Deal deal, final ParkAdvert parkAdvert) {

        return DealParkOpenDto.builder()
                .dealId(deal.getId())
                .authorAvatar(userRepository.findOne(deal.getHomeyId()).getAvatarUrl())
                .authorId(deal.getHomeyId())
                .authorFirstName(userRepository.findOne(deal.getHomeyId()).getFirstname())
                .authorLastName(userRepository.findOne(deal.getHomeyId()).getLastname())
                .clientId(deal.getLaborerId())
                .clientAvatar(userRepository.findById(deal.getLaborerId()).getAvatarUrl())
                .clientFirstName(userRepository.findById(deal.getLaborerId()).getFirstname())
                .clientLastName(userRepository.findById(deal.getLaborerId()).getLastname())
                .serviceId(deal.getServiceId())
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
                .car(carRepository.findOne(parkAdvert.getBetoParkProfileCarId()))
                .dealType(deal.getDealType())
                .dealStatus(deal.getDealStatus())
                .dealInfo(deal.getDealInfo())
                .amountDeal(deal.getAmountDeal())
                .authorCommission(deal.getHomeyCommission())
                .clientCommission(deal.getLaborerCommission())
                .historyOfNegotiation(deal.getHistoryOfNegotiation())
                .isOwnerAccept(deal.getIsOwnerAccept())
                .isClientAccept(deal.getIsLaborerAccept())
                .created(deal.getCreated())
                .updated(deal.getUpdated())
                .auctionList(buildAuctionDto(deal))
                .claimList(deal.getClaimList())
                .build();
    }

    private List<AuctionUpdateDto> buildAuctionDto(Deal deal) {
        int size = deal.getAuctionList().size();
        List<AuctionUpdateDto> updateAuction = new ArrayList<>();

        if (size == 0) {
            return updateAuction;
        }

        for (int i = 0; i < deal.getAuctionList().size(); i++) {
            AuctionUpdateDto updateDto = AuctionUpdateDto.builder()
                    .id(deal.getId())
                    .jobSeekerId(deal.getLaborerId())
                    .jobSeekerAvatar(getUserWithoutLazy(deal.getLaborerId()).getAvatarUrl())
                    .jobSeekerRating(getUserWithoutLazy(deal.getLaborerId()).getUserRating())
                    .advertOwnerId(deal.getHomeyId())
                    .jobOwnerAvatar(getUserWithoutLazy(deal.getHomeyId()).getAvatarUrl())
                    .jobOwnerRating(getUserWithoutLazy(deal.getHomeyId()).getUserRating())
                    .auctionStatus(deal.getAuctionList().get(i).getAuctionStatus())
                    .startTime(deal.getAuctionList().get(i).getStartTime())
                    .finishTime(deal.getAuctionList().get(i).getFinishTime())
                    .price(deal.getAuctionList().get(i).getPrice())
                    .transactions(null)
                    .build();

            updateAuction.add(updateDto);
        }

        return updateAuction;
    }

    //todo fix this shit
    private User getUserWithoutLazy(long id) {
        User foundUser = userRepository.getFetchUser(id);
        return foundUser;
    }

    private String getCategoryName(final Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        return category.getName();
    }

    private String getSubCategoryName(final Long subCategoryId) {
        if (Objects.isNull(subCategoryId)) {
            return null;
        }
        SubCategory subCategory = subCategoryRepository.findOne(subCategoryId);
        return subCategory.getName();
    }
}
