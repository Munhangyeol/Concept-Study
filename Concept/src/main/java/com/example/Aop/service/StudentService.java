package com.example.Aop.service;

import com.example.Aop.TimeTrace;
import com.example.Aop.domain.IdentityStudent;
import com.example.Aop.domain.SequenceStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final IdentityStudentRepository identityStudentRepository;
    private final SequenceStudentRepository sequenceStudentRepository;
    private int insertNum=1000;

    @TimeTrace
    public void saveIdentity(){
        for(int i=0; i<insertNum;i++){
            System.out.println("save I: "+i);
            identityStudentRepository.save(IdentityStudent.builder()
                    .num(i)
                    .build());
        }
    }
    @TimeTrace
    public void saveSequence(){
        for(int i=0; i<insertNum;i++){
            System.out.println("save I: "+i);
            SequenceStudent save = sequenceStudentRepository.save(SequenceStudent.builder()
                    .num(i)
                    .build());
            System.out.println("save id: "+save.getId());
        }
    }

}
