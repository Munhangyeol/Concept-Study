package com.example.aop.service;

import com.example.aop.domain.SequenceStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceStudentRepository extends JpaRepository<SequenceStudent,Long> {


}
