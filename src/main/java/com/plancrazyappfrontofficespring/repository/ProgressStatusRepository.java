package com.plancrazyappfrontofficespring.repository;

import com.plancrazyappfrontofficespring.model.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressStatusRepository  extends JpaRepository<ProgressStatus, Long> {
}
