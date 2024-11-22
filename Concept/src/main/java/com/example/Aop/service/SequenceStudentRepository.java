package com.example.Aop.service;

import com.example.Aop.domain.SequenceStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceStudentRepository extends JpaRepository<SequenceStudent,Long> {


}
