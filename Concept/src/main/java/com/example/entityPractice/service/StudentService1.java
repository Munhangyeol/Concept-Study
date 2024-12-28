package com.example.entityPractice.service;


import com.example.entityPractice.domain.IdentityStudent1;
import com.example.entityPractice.domain.SequenceStudent1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService1 {
    private final IdentityStudentRepository1 identityStudentRepository;
    private final SequenceStudentRepository1 sequenceStudentRepository;
    private int insertNum=1000;

    public void saveIdentity(){
        for(int i=0; i<insertNum;i++){
            System.out.println("save I: "+i);
            identityStudentRepository.save(IdentityStudent1.builder()
                    .num(i)
                    .build());
        }
    }
    public void saveSequence(){
        for(int i=0; i<insertNum;i++){
            System.out.println("save I: "+i);
            SequenceStudent1 save = sequenceStudentRepository.save(SequenceStudent1.builder()
                    .num(i)
                    .build());
            System.out.println("save id: "+save.getId());
        }
    }

}
