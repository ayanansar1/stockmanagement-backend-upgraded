package com.stockmanagement.stockmanagement.machine.entity;



import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.enums.JacquardType;
import com.stockmanagement.stockmanagement.machine.enums.MachineType;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "machines",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_machine_code",
                        columnNames = "machine_code"
                ),

                @UniqueConstraint(
                        name = "uk_kareegar_machine_no",
                        columnNames = {
                                "kareegar_id",
                                "machine_no"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Machine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "machine_code",
            nullable = false,
            length = 30
    )
    private String machineCode;

    @Column(
            name = "machine_no",
            nullable = false
    )
    private Long machineNo;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_machine_kareegar"
            )
    )
    private Kareegar kareegar;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "machine_type",
            nullable = false,
            length = 30
    )
    private MachineType machineType;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "jacquard_type",
            nullable = false,
            length = 30
    )
    private JacquardType jacquardType;

    @Column(
            name = "jacquard_chok",
            nullable = false
    )
    private Integer jacquardChok;

    @Column(
            name = "date_of_join",
            nullable = false
    )
    private LocalDate dateOfJoin;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "machine_status",
            nullable = false,
            length = 20
    )
    private Status machineStatus;
}