package com.example.Aop.service;

import com.example.Aop.domain.IdentityStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityStudentRepository extends JpaRepository<IdentityStudent,Long> {
}
