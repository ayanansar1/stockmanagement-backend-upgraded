package com.stockmanagement.stockmanagement.common.generator;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "code_sequence",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_code_sequence_entity",
                        columnNames = "entity_name"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "entity_name",
            nullable = false,
            unique = true,
            length = 30
    )
    private CodeType entityName;

    @Column(nullable = false, length = 10)
    private String prefix;

    @Column(name = "last_number", nullable = false)
    private Long lastNumber;
}