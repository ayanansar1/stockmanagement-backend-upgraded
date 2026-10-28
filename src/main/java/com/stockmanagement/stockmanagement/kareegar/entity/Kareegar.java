package com.stockmanagement.stockmanagement.kareegar.entity;


import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Role;
import com.stockmanagement.stockmanagement.common.enums.Status;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@Entity
@Table(
        name = "kareegars",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_kareegar_code",
                        columnNames = "kareegar_code"
                ),
                @UniqueConstraint(
                        name = "uk_kareegar_mobile",
                        columnNames = "mobile_no"
                ),
                @UniqueConstraint(
                        name = "uk_kareegar_email",
                        columnNames = "email_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Kareegar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "kareegar_code",
            nullable = false,
            length = 30
    )
    private String kareegarCode;

    @Column(
            name = "kareegar_name",
            nullable = false,
            length = 100
    )
    private String kareegarName;

    @Column(
            name = "mobile_no",
            nullable = false,
            length = 15
    )
    private String mobileNo;

    @Column(
            name = "email_id",
            length = 150
    )
    private String emailId;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(
            name = "join_date",
            nullable = false
    )
    private LocalDate joinDate;
}