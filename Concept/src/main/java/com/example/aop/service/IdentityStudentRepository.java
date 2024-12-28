package com.example.aop.service;

import com.example.aop.domain.IdentityStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityStudentRepository extends JpaRepository<IdentityStudent,Long> {
}
