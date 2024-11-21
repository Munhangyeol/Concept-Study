package com.example.EntityPractice.service;

import com.example.EntityPractice.domain.SequenceStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceStudentRepository extends JpaRepository<SequenceStudent,Long> {


}
