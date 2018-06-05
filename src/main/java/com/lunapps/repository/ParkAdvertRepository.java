package com.lunapps.repository;

import com.lunapps.models.AdvertStatus;
import com.lunapps.models.ParkAdvert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkAdvertRepository extends JpaRepository<ParkAdvert, Long>, JpaSpecificationExecutor<ParkAdvert> {

    @Query(value = "SELECT\n" +
            "  *,\n" +
            "  (6371000 * acos(cos(radians(:lat)) * cos(radians(park_adverts.latitude))\n" +
            "               * cos(radians(park_adverts.longitude) - radians(:lng))\n" +
            "               + sin(radians(:lat))\n" +
            "                 * sin(radians(park_adverts.latitude)))) " +
            "AS distance\n" +
            "FROM park_adverts\n" +
            "HAVING distance < :dist\n" +
            "ORDER BY distance ASC", nativeQuery = true)
    List<ParkAdvert> findByDistance(@Param("dist") final Long distance, @Param("lat") final Double latitude, @Param("lng") final Double longitude);

    @Query(value = "SELECT pa FROM ParkAdvert AS pa WHERE pa.advertStatus=:advertStatus")
    List<ParkAdvert> findByAdvertStatus(@Param("advertStatus") final AdvertStatus advertStatus);

    @Query(value = "SELECT * FROM park_adverts AS pa JOIN betopark_profile AS p ON pa.fk_park_profile_id = p.id " +
            "LEFT JOIN users AS u ON p.id = u.betopark_profile_id WHERE u.id = :userId", nativeQuery = true)
    List<ParkAdvert> findAllAdvertUserAsOwnerById(@Param("userId") final Long userId);

    @Query(value = "SELECT * FROM park_adverts AS pa JOIN betopark_profile AS p ON pa.fk_park_profile_id = p.id " +
            "LEFT JOIN users AS u ON p.id = u.betopark_profile_id WHERE u.id = :userId AND pa.advert_status = :advertStatus ", nativeQuery = true)
    List<ParkAdvert> findAllAdvertUserAsOwnerByIdAndAdvertStatus(@Param("userId") final Long userId,
                                                                 @Param("advertStatus") String advertStatus);

    @Query(value = "SELECT * FROM park_adverts AS pa JOIN betopark_profile AS p ON pa.fk_park_profile_id = p.id " +
            "LEFT JOIN users AS u ON p.id = u.betopark_profile_id WHERE u.id = :userId AND pa.advert_status IN :advertStatuses ", nativeQuery = true)
    List<ParkAdvert> findAllAdvertUserAsOwnerByIdAndAdvertStatuses(@Param("userId") final Long userId,
                                                                   @Param("advertStatuses") List<String> advertStatus);

    @Query(value = "SELECT * FROM park_adverts AS pa JOIN betopark_profile AS p ON pa.fk_park_profile_id = p.id " +
            "LEFT JOIN users AS u ON p.id = u.betopark_profile_id WHERE u.id = :userId AND pa.advert_status <> :advertStatus ", nativeQuery = true)
    List<ParkAdvert> findAllAdvertUserAsOwnerByIdAndAdvertStatusWhichNotEqual(@Param("userId") final Long userId,
                                                                              @Param("advertStatus") String advertStatus);

    @Query(value = "SELECT * FROM park_adverts AS pa LEFT JOIN deals AS d ON pa.id = d.service_id " +
            "WHERE pa.advert_status = :paStatus " +
            "AND d.homey_id = :userId " +
            "AND d.status = :dealStatus", nativeQuery = true)
    List<ParkAdvert> findAllOwnerAdvertByDealStatus(@Param("paStatus") final String advertStatus,
                                                    @Param("userId") final Long userId,
                                                    @Param("dealStatus") final String dealStatus);

    List<ParkAdvert> findAllByAdvertStatus(final AdvertStatus advertStatus);
}
