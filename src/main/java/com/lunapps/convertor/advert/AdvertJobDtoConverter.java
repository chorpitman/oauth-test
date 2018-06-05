package com.lunapps.convertor.advert;

import com.lunapps.controllers.dto.adverts.AdvertJobDetailDto;
import com.lunapps.controllers.dto.adverts.job.AdvertJobCreateDto;
import com.lunapps.controllers.dto.adverts.job.AdvertJobDto;
import com.lunapps.controllers.dto.deals.DealDetailDto;
import com.lunapps.exception.user.AdvertException;
import com.lunapps.models.AdvertStatus;
import com.lunapps.models.Category;
import com.lunapps.models.JobAdvert;
import com.lunapps.models.Rating;
import com.lunapps.models.RatingType;
import com.lunapps.models.SubCategory;
import com.lunapps.models.User;
import com.lunapps.models.deal.Deal;
import com.lunapps.repository.CategoryRepository;
import com.lunapps.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AdvertJobDtoConverter {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public JobAdvert convert(final User author, final AdvertJobCreateDto createDto) {
        return JobAdvert.builder()
                .subject(createDto.getSubject())
                .address(createDto.getAddress())
                .latitude(createDto.getLatitude())
                .longitude(createDto.getLongitude())
                .description(createDto.getDescription())
                .workDuration(createDto.getWorkDuration())
                .workReward(createDto.getWorkReward())
                .mustDoneWorkTo(createDto.getMustDoneWorkTo())
                .requirement(createDto.getRequirement())
                .jobCategoryId(createDto.getJobCategoryId())
                .jobSubCategoryId(createDto.getJobSubCategoryId())
                .photoList(createDto.getPhotoList())
                .isPhotoRequestConfirmationNeed(createDto.getIsPhotoRequestConfirmationNeed())
                .isJobDonePhotoDownloaded(Boolean.FALSE)
                .advertStatus(AdvertStatus.ENABLE)
                .createdDate(ZonedDateTime.now())
                .userRate(countUserRating(author, RatingType.JOB_HOMEY))
                .build();
    }

    public AdvertJobDto convert(final JobAdvert advert, final User advertAuthor, final User currentUser) {
        if (Objects.equals(currentUser.getId(), advertAuthor.getId())) {
            return AdvertJobDto.builder()
                    .id(advert.getId())
                    .subject(advert.getSubject())
                    .address(advert.getAddress())
                    .latitude(advert.getLatitude())
                    .longitude(advert.getLongitude())
                    .description(advert.getDescription())
                    .workDuration(advert.getWorkDuration())
                    .workReward(advert.getWorkReward())
                    .mustDoneWorkTo(advert.getMustDoneWorkTo())
                    .requirement(advert.getRequirement())
                    .category(getCategory(advert.getJobCategoryId(), advert.getJobSubCategoryId()))
                    .photoList(advert.getPhotoList())
                    //todo add rating current user as
                    .userRate(countUserRating(currentUser, RatingType.JOB_HOMEY))
                    .createdDate(advert.getCreatedDate())
                    .updatedDate(advert.getUpdatedDate())
                    .firstname(advertAuthor.getFirstname())
                    .lastname(advertAuthor.getLastname())
                    .avatarUrl(advertAuthor.getAvatarUrl())
                    .isCurrentUserAdvert(Boolean.TRUE)
                    .isPhotoRequestConfirmationNeed(advert.getIsPhotoRequestConfirmationNeed())
                    .isJobDonePhotoDownloaded(advert.getIsJobDonePhotoDownloaded())
                    .build();
        }

        return AdvertJobDto.builder()
                .id(advert.getId())
                .subject(advert.getSubject())
                .address(advert.getAddress())
                .latitude(advert.getLatitude())
                .longitude(advert.getLongitude())
                .description(advert.getDescription())
                .workDuration(advert.getWorkDuration())
                .workReward(advert.getWorkReward())
                .mustDoneWorkTo(advert.getMustDoneWorkTo())
                .requirement(advert.getRequirement())
                .category(getCategory(advert.getJobCategoryId(), advert.getJobSubCategoryId()))
                .photoList(advert.getPhotoList())
                //todo rating
                .userRate(countUserRating(currentUser, RatingType.JOB_LABORER))
                .createdDate(advert.getCreatedDate())
                .updatedDate(advert.getUpdatedDate())
                .firstname(advertAuthor.getFirstname())
                .lastname(advertAuthor.getLastname())
                .avatarUrl(advertAuthor.getAvatarUrl())
                .isCurrentUserAdvert(Boolean.FALSE)
                .isPhotoRequestConfirmationNeed(advert.getIsPhotoRequestConfirmationNeed())
                .isJobDonePhotoDownloaded(advert.getIsJobDonePhotoDownloaded())
                .build();
    }

    public List<AdvertJobDto> convertToDtoWithAdditionalInfoAboutAdvertOwner(final List<JobAdvert> jobAdvertList) {
        List<AdvertJobDto> convertedJobs = new ArrayList<>();
        for (JobAdvert jobAdvert : jobAdvertList) {
            User user = userRepository.findUserByJobAdvertId(jobAdvert.getId());

            AdvertJobDto jobDto = AdvertJobDto.builder()
                    .id(jobAdvert.getId())
                    .subject(jobAdvert.getSubject())
                    .address(jobAdvert.getAddress())
                    .latitude(jobAdvert.getLatitude())
                    .longitude(jobAdvert.getLongitude())
                    .description(jobAdvert.getDescription())
                    .workDuration(jobAdvert.getWorkDuration())
                    .workReward(jobAdvert.getWorkReward())
                    .mustDoneWorkTo(jobAdvert.getMustDoneWorkTo())
                    .requirement(jobAdvert.getRequirement())
                    .category(getCategory(jobAdvert.getJobCategoryId(), jobAdvert.getJobSubCategoryId()))
                    .photoList(jobAdvert.getPhotoList())
                    .userRate(countUserRating(user, RatingType.JOB_HOMEY))
                    .createdDate(jobAdvert.getCreatedDate())
                    .updatedDate(jobAdvert.getUpdatedDate())
                    .isPhotoRequestConfirmationNeed(jobAdvert.getIsPhotoRequestConfirmationNeed())
                    .isJobDonePhotoDownloaded(jobAdvert.getIsJobDonePhotoDownloaded())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .avatarUrl(user.getAvatarUrl())
                    .build();
            convertedJobs.add(jobDto);
        }
        return convertedJobs;
    }

    public AdvertJobDetailDto convertJobAdvertToAdvertJobDtoDetail(final User author, final JobAdvert jobAdvert, final List<Deal> deals) {

        return AdvertJobDetailDto.builder()
                .id(jobAdvert.getId())
                .subject(jobAdvert.getSubject())
                .address(jobAdvert.getAddress())
                .description(jobAdvert.getDescription())
                .workDuration(jobAdvert.getWorkDuration())
                .workReward(jobAdvert.getWorkReward())
                .requirement(jobAdvert.getRequirement())
                .userRate(countUserRating(author, RatingType.JOB_HOMEY))
                .photoList(jobAdvert.getPhotoList())
                .createdDate(jobAdvert.getCreatedDate())
                .updatedDate(jobAdvert.getUpdatedDate())
                .isPhotoRequestConfirmationNeed(jobAdvert.getIsPhotoRequestConfirmationNeed())
                .isJobDonePhotoDownloaded(jobAdvert.getIsJobDonePhotoDownloaded())
                .dealList(convertDeals(deals, author))
                .build();
    }

    private List<DealDetailDto> convertDeals(final List<Deal> dealList, final User author) {
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

    private Category getCategory(final Long jobCategoryId, final Long jobSubCategoryId) {
        Category category = categoryRepository.findOne(jobCategoryId);
        if (Objects.isNull(category)) {
            throw new AdvertException(MessageFormat.format("advert with id: {0} does not exist", jobCategoryId));
        }

        List<SubCategory> subCategoryList = category.getSubCategories();
        if (subCategoryList.size() != 0) {
            if (Objects.isNull(jobSubCategoryId)) {
                throw new AdvertException(MessageFormat.format("advert with id: {0} can not contain null subCategory", jobCategoryId));
            }
            for (SubCategory subCategory : subCategoryList) {
                if (Objects.equals(subCategory.getId(), jobSubCategoryId)) {
                    category.setSubCategories(Arrays.asList(SubCategory.builder()
                            .id(subCategory.getId())
                            .name(subCategory.getName())
                            .build()));

                    return category;
                }
            }
        }
        category.setSubCategories(Collections.EMPTY_LIST);

        return category;
    }

    private double countUserRating(final User user, final RatingType ratingType) {
        Set<Rating> userRating = user.getUserRating();
        List<Long> ratingList = userRating
                .stream()
                .filter(rating -> Objects.equals(rating.getRatingType(), ratingType))
                .map(Rating::getRating)
                .collect(Collectors.toList());

        if (ratingList.size() == 0) {

            return 0.0;
        }

        return ratingList
                .stream()
                .mapToLong(x -> x)
                .average().getAsDouble();
    }
}