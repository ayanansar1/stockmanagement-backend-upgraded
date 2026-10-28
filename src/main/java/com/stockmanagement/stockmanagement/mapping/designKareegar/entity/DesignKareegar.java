package com.stockmanagement.stockmanagement.mapping.designKareegar.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.design.entity.Design;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "design_kareegars",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_design_kareegar_code",
                        columnNames = "design_kareegar_code"
                )
        },
        indexes = {
                @Index(
                        name = "idx_design_kareegar_kareegar",
                        columnList = "kareegar_id"
                ),
                @Index(
                        name = "idx_design_kareegar_design",
                        columnList = "design_id"
                ),
                @Index(
                        name = "idx_design_kareegar_machine",
                        columnList = "machine_id"
                ),
                @Index(
                        name = "idx_design_kareegar_status_deleted",
                        columnList = "status, deleted"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignKareegar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "design_kareegar_code",
            nullable = false,
            length = 30
    )
    private String designKareegarCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_design_kareegar_kareegar"
            )
    )
    private Kareegar kareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "design_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_design_kareegar_design"
            )
    )
    private Design design;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "machine_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_design_kareegar_machine"
            )
    )
    private Machine machine;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private Status status;

    @Column(
            name = "assigned_date",
            nullable = false
    )
    private LocalDate assignedDate;
}