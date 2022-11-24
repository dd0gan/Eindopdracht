package com.example.les17.repository;

import com.example.les17.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    public List<Application> findByVacancyId(Integer id);
}
