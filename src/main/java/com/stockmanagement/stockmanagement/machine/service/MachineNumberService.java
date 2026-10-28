package com.stockmanagement.stockmanagement.machine.service;



import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.MachineNumberSequence;
import com.stockmanagement.stockmanagement.machine.repository.MachineNumberSequenceRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MachineNumberService {

    private final MachineNumberSequenceRepository
            sequenceRepository;


    public Long generateNextNumber(
            Kareegar kareegar) {

        MachineNumberSequence sequence =
                sequenceRepository
                        .findByKareegarForUpdate(kareegar)
                        .orElseGet(
                                () -> createSequence(kareegar)
                        );

        long nextNumber =
                sequence.getLastNumber() + 1;

        sequence.setLastNumber(nextNumber);

        return nextNumber;
    }


    private MachineNumberSequence createSequence(
            Kareegar kareegar) {

        MachineNumberSequence sequence =
                MachineNumberSequence.builder()
                        .kareegar(kareegar)
                        .lastNumber(0L)
                        .build();

        return sequenceRepository.save(sequence);
    }
}
