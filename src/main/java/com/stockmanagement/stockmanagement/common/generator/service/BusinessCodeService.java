package com.stockmanagement.stockmanagement.common.generator.service;


import com.stockmanagement.stockmanagement.common.generator.CodeSequence;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.repository.CodeSequenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessCodeService {

    private final CodeSequenceRepository repository;

    @Transactional
    public String generateCode(CodeType codeType) {

        CodeSequence sequence =
                repository.findByEntityNameForUpdate(codeType)
                        .orElseGet(() -> createSequence(codeType));

        long nextNumber =
                sequence.getLastNumber() + 1;

        sequence.setLastNumber(nextNumber);

        repository.save(sequence);

        return String.format(
                "%s%06d",
                sequence.getPrefix(),
                nextNumber
        );
    }

    private CodeSequence createSequence(CodeType codeType) {

        CodeSequence sequence = CodeSequence.builder()
                .entityName(codeType)
                .prefix(codeType.getPrefix())
                .lastNumber(0L)
                .build();

        return repository.save(sequence);
    }
}
