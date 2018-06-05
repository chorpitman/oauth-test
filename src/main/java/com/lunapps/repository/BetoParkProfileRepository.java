package com.lunapps.repository;

import com.lunapps.models.BetoParkProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetoParkProfileRepository extends JpaRepository<BetoParkProfile, Long> {
}
