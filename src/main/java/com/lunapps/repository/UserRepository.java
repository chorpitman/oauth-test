package com.lunapps.repository;

import com.lunapps.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(final String email);

    User findByEmailToken(final String emailToken);

    User findByFbUserId(final String fbUserId);

    @Query(value = "SELECT DISTINCT u FROM User AS u LEFT JOIN FETCH u.userRating WHERE u.id = :userId")
    User findById(@Param("userId") final Long userId);

    @Query(value = "SELECT * FROM users WHERE betopark_profile_id = :parkProfileId", nativeQuery = true)
    User findUserByParkProfileId(@Param("parkProfileId") final Long parkProfileId);

    @Query(value = "SELECT * FROM users AS u JOIN job_adverts AS ja ON ja.fk_job_profile_id = u.betojob_profile_id WHERE ja.id = :advertId", nativeQuery = true)
    User findUserByJobAdvertId(@Param("advertId") final Long advertId);

    @Query(value = "SELECT * FROM users AS u JOIN park_adverts AS pa ON pa.fk_park_profile_id = u.betopark_profile_id WHERE pa.id = :advertId", nativeQuery = true)
    User findUserByParkAdvertId(@Param("advertId") final Long advertId);

    @Modifying(clearAutomatically = true)
    @Query("SELECT u FROM User u JOIN u.betoParkProfile where u.betoParkProfile.enable = TRUE AND u.email = :email")
    User isUserBetoParkProfileEnable(@Param("email") final String email);

    @Query(value = "SELECT * FROM users AS u JOIN job_adverts AS ja ON u.betojob_profile_id = ja.fk_job_profile_id WHERE ja.id = :advertId", nativeQuery = true)
    User findUserByAdvertId(@Param("advertId") final Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRating WHERE u.id = :id")
    User getFetchUser(@Param("id") final Long id);

    @Query(value = "SELECT * FROM users AS u JOIN photos AS p on u.id = p.fk_user_id WHERE p.id IN :photoListIds", nativeQuery = true)
    List<User> getUserByPhotoId(@Param("photoListIds") final List<Long> photoListIds);

    @Query(value = "SELECT * FROM users AS u JOIN betopark_profile AS bp ON u.betopark_profile_id = bp.id " +
            "JOIN cars AS c on bp.id = c.fk_park_profile_id WHERE c.id = :carId", nativeQuery = true)
    User findUserByCarId(@Param("carId") final Long carId);

//    @Query(value = "SELECT *\n" +
//            "from users as u\n" +
//            "  JOIN betojob_profile bp on u.betojob_profile_id = bp.id\n" +
//            "  JOIN betopark_profile p on u.betopark_profile_id = p.id join deals as d on u.id =d.service_id where d.id = :id", nativeQuery = true)
//    User findUserAmongParkAndJobByDealId(@Param("id") final Long dealId);
}