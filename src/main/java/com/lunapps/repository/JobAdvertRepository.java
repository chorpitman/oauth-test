package com.lunapps.repository;

import com.lunapps.models.AdvertStatus;
import com.lunapps.models.JobAdvert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAdvertRepository extends JpaRepository<JobAdvert, Long>, JpaSpecificationExecutor<JobAdvert> {
    @Query(value = "SELECT\n" +
            "  *,\n" +
            "  (6371000 * acos(cos(radians(:lat)) * cos(radians(job_adverts.latitude))\n" +
            "               * cos(radians(job_adverts.longitude) - radians(:lng))\n" +
            "               + sin(radians(:lat))\n" +
            "                 * sin(radians(job_adverts.latitude))))\n" +
            "\n" +
            "    AS distance\n" +
            "FROM job_adverts\n" +
            "HAVING distance < :dist\n" +
            "ORDER BY distance ASC", nativeQuery = true)
    List<JobAdvert> findAdvertsByDistance(@Param("dist") final Long distance, @Param("lat") final Double latitude, @Param("lng") final Double longitude);

    @Query(value = "SELECT\n" +
            "  ja.id,\n" +
            "  ja.address,\n" +
            "  ja.created_date,\n" +
            "  ja.description,\n" +
            "  ja.job_category_id,\n" +
            "  ja.job_sub_category_id,\n" +
            "  ja.latitude,\n" +
            "  ja.longitude,\n" +
            "  ja.mustDoneWorkTo,\n" +
            "  ja.requirement,\n" +
            "  ja.subject,\n" +
            "  ja.updated_date,\n" +
            "  ja.user_rate,\n" +
            "  ja.workDuration,\n" +
            "  ja.workReward,\n" +
            "  ja.fk_job_profile_id\n" +
            "FROM betojob_profile AS bj\n" +
            "  INNER JOIN job_adverts AS ja ON bj.id = ja.fk_job_profile_id\n" +
            "  LEFT JOIN users AS u ON u.betojob_profile_id = bj.id\n" +
            "  LEFT JOIN subcategory AS sub ON sub.id = ja.job_sub_category_id\n" +
            "  LEFT JOIN сategories AS c ON c.id = ja.job_category_id\n" +
            "WHERE u.first_name LIKE CONCAT('%', :phrase, '%') OR u.last_name LIKE CONCAT('%', :phrase, '%') OR\n" +
            "      ja.address LIKE CONCAT('%', :phrase, '%')\n" +
            "      OR c.category_name LIKE CONCAT('%', :phrase, '%') OR sub.subcategory_name LIKE CONCAT('%', :phrase, '%')\n" +
            "GROUP BY ja.created_date DESC", nativeQuery = true)
    List<JobAdvert> findAdvertBySearchPhrase(@Param("phrase") final String phrase);

    @Query(value = "SELECT * FROM job_adverts AS ja LEFT JOIN betojob_profile AS bp ON ja.fk_job_profile_id = bp.id " +
            "LEFT JOIN users AS u ON bp.id = u.betojob_profile_id WHERE u.id = :userId", nativeQuery = true)
    List<JobAdvert> findAllAdvertByUserId(@Param("userId") final Long userId);

    @Query(value = "SELECT * FROM job_adverts AS ja LEFT JOIN deals AS d ON ja.id = d.service_id " +
            "WHERE ja.advert_status = :jaStatus " +
            "AND d.homey_id = :userId " +
            "AND d.status IN :dealStatus " +
            "AND d.type = :dealType", nativeQuery = true)
    List<JobAdvert> findAllOwnerAdvertByDealStatus(@Param("jaStatus") final String advertStatus,
                                                   @Param("userId") final Long userId,
                                                   @Param("dealStatus") final List<String> dealStatus,
                                                   @Param("dealType") final String dealType);

    List<JobAdvert> findAllByAdvertStatus(final AdvertStatus advertStatus);
}
