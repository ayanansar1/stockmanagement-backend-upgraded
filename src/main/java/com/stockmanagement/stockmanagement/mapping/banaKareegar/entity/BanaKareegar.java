package com.stockmanagement.stockmanagement.mapping.banaKareegar.entity;

import com.stockmanagement.stockmanagement.bana.entity.Bana;
import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "bana_kareegars",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_bana_kareegar_code",
                        columnNames = "bana_kareegar_code"
                )
        },
        indexes = {
                @Index(
                        name = "idx_bana_kareegar_kareegar",
                        columnList = "kareegar_id"
                ),
                @Index(
                        name = "idx_bana_kareegar_bana",
                        columnList = "bana_id"
                ),
                @Index(
                        name = "idx_bana_kareegar_machine",
                        columnList = "machine_id"
                ),
                @Index(
                        name = "idx_bana_kareegar_status_deleted",
                        columnList = "status, deleted"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanaKareegar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "bana_kareegar_code",
            nullable = false,
            length = 30
    )
    private String banaKareegarCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_bana_kareegar_kareegar"
            )
    )
    private Kareegar kareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "bana_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_bana_kareegar_bana"
            )
    )
    private Bana bana;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "machine_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_bana_kareegar_machine"
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