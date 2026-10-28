package com.stockmanagement.stockmanagement.machine.entity;



import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
        name = "machine_number_sequence",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_machine_sequence_kareegar",
                        columnNames = "kareegar_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineNumberSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            unique = true
    )
    private Kareegar kareegar;

    @Column(
            name = "last_number",
            nullable = false
    )
    private Long lastNumber;
}
