package com.example.aop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SequenceStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(
            name = "table_generator", table = "sequence_seq",
            pkColumnName = "sequence_name")

    private Long id;
    private int num;



}
