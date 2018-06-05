package com.lunapps.repository;

import com.lunapps.models.BetoJobProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetoJobProfileRepository extends JpaRepository<BetoJobProfile, Long> {
}
