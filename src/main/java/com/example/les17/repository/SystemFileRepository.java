package com.example.les17.repository;

import com.example.les17.model.SystemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemFileRepository extends JpaRepository<SystemFile, Integer> {
    public SystemFile findByUuid(String uuid);
}
