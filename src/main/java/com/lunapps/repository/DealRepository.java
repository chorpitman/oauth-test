package com.lunapps.repository;

import com.lunapps.models.deal.Deal;
import com.lunapps.models.deal.DealStatus;
import com.lunapps.models.deal.DealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    @Query("SELECT d FROM Deal as d where d.homeyId = :id AND d.serviceId = :serviceId AND d.dealStatus = :dealStatus")
    List<Deal> getDealByHomeyIdServiceIdDealStatus(@Param("id") Long homeyId, @Param("serviceId") Long serviceId, @Param("dealStatus") DealStatus dealStatus);

    @Query("SELECT d FROM Deal as d where d.laborerId = :id AND d.serviceId = :serviceId")
    List<Deal> getDealByLaborerIdAndServiceId(@Param("id") Long laborerId, @Param("serviceId") Long serviceId);

    @Query("SELECT d FROM Deal AS d WHERE d.dealStatus = :dealStatus AND d.laborerId = :id AND d.dealType = :dealType")
    Deal getDealByClientIdAndStatus(@Param("id") Long userId, @Param("dealStatus") DealStatus dealStatus,
                                    @Param("dealType") DealType dealType);

    @Query("SELECT d FROM Deal AS d WHERE d.serviceId = :id AND d.homeyId = :advertOwnerId")
    List<Deal> findDealByAdvertId(@Param("id") Long advertId, @Param("advertOwnerId") Long advertOwnerId);

    @Query("SELECT DISTINCT d FROM Deal AS d LEFT JOIN FETCH d.auctionList WHERE d.homeyId =:authorId " +
            "AND d.serviceId = :serviceId AND d.dealStatus =:dealStatus AND d.dealType = :dealType")
        //todo rename
    List<Deal> findAllDealsByAdvertIdDealStatusAndType(@Param("authorId") Long authorId, @Param("serviceId") Long serviceId, @Param("dealStatus") DealStatus dealStatus, @Param("dealType") DealType dealType);

    @Query(value = "SELECT DISTINCT d FROM Deal AS d LEFT JOIN FETCH d.auctionList " +
            "WHERE d.dealType = :dealType AND d.serviceId = :serviceId AND d.dealStatus IN :dealStatus")
    List<Deal> findAllInProgressDealsByAdvertId(@Param("serviceId") Long serviceId,
                                                @Param("dealStatus") List<DealStatus> dealStatus,
                                                @Param("dealType") DealType dealType);

    @Query(value = "SELECT DISTINCT d FROM Deal AS d JOIN FETCH d.auctionList WHERE d.homeyId = :authorId " +
            "AND d.dealStatus IN :dealStatus AND d.serviceId = :serviceId AND d.dealType =:dealType ORDER BY d.created")
    List<Deal> findAllByAuthorIdDealsStatusDealTypeAndServiceId(@Param("authorId") final Long authorId,
                                                                @Param("dealStatus") final List<DealStatus> dealStatus,
                                                                @Param("dealType") final DealType dealType,
                                                                @Param("serviceId") final Long serviceId);

    @Query(value = "SELECT DISTINCT d FROM Deal AS d JOIN FETCH d.auctionList WHERE d.laborerId = :clientId " +
            "AND d.dealStatus IN :dealStatus AND d.dealType =:dealType ORDER BY d.created")
    List<Deal> findAllDealByClientIdDealStatusAndType(@Param("clientId") final Long clientId,
                                                      @Param("dealStatus") final List<DealStatus> dealStatus,
                                                      @Param("dealType") final DealType dealType);

    @Query(value = "SELECT * FROM deals AS d JOIN job_adverts as ja ON d.service_id = ja.id " +
            "WHERE d.laborer_id = :userId AND d.status IN :dealStatus " +
            "AND d.type = :dealType AND ja.advert_status = :jaStatus", nativeQuery = true)
    List<Deal> findByClientIdAndDealStatusAndDealTypeAndAdvertType(@Param("jaStatus") final String advertStatus,
                                                                   @Param("userId") final Long userId,
                                                                   @Param("dealStatus") final List<String> dealStatus,
                                                                   @Param("dealType") final String dealType);

    @Query(value = "SELECT DISTINCT d FROM Deal AS d JOIN FETCH d.auctionList WHERE d.homeyId = :authorId " +
            "AND d.dealStatus IN :dealStatus AND d.dealType =:dealType ORDER BY d.created")
    List<Deal> findAllDealByAuthorIdDealStatusAndType(@Param("authorId") final Long authorId,
                                                      @Param("dealStatus") final List<DealStatus> dealStatus,
                                                      @Param("dealType") final DealType dealType);

    //todo test
    @Query(value = "SELECT DISTINCT d FROM Deal AS d JOIN FETCH d.auctionList WHERE d.homeyId = :authorId " +
            "AND d.dealStatus IN :dealStatus AND d.dealType =:dealType ORDER BY d.created")
    List<Deal> findAllByAuthorIdDealStatusAndDealType(@Param("authorId") final Long authorId,
                                                      @Param("dealStatus") final List<DealStatus> dealStatus,
                                                      @Param("dealType") final DealType dealType);

    //todo test

    @Query(value = "SELECT d FROM Deal AS d LEFT JOIN FETCH d.auctionList WHERE d.dealType = 'BETOJOB' AND d.homeyId = :homeyId AND d.dealStatus = :dealStatus")
    List<Deal> findAllJobDealByAuthorIdAndDealStatus(@Param("homeyId") Long homeyId, @Param("dealStatus") DealStatus dealStatus);

    @Query("SELECT d FROM Deal d LEFT JOIN FETCH d.auctionList WHERE d.id = :id")
    Deal getFetchedDeal(@Param("id") final Long id);

    //admin
    @Query(value = "SELECT d FROM Deal as d LEFT JOIN FETCH d.auctionList")
    List<Deal> getFetchedAllDeals();
}
