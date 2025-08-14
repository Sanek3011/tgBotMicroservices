package org.example.reportservice.repository;

import org.example.reportservice.entity.ReportActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportActivityRepository extends JpaRepository<ReportActivity, Integer> {
    List<ReportActivity> findByType(String type);
}
