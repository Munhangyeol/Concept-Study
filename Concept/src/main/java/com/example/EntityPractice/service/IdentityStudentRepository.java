package com.example.EntityPractice.service;

import com.example.EntityPractice.domain.IdentityStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityStudentRepository extends JpaRepository<IdentityStudent,Long> {
}
