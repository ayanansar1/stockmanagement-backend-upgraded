package com.stockmanagement.stockmanagement.buyer.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "buyers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_buyer_code",
                        columnNames = "buyer_code"
                ),
                @UniqueConstraint(
                        name = "uk_buyer_mobile",
                        columnNames = "mobile_no"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Buyer extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "buyer_code",
            nullable = false,
            length = 30
    )
    private String buyerCode;

    @Column(
            name = "buyer_name",
            nullable = false,
            length = 100
    )
    private String buyerName;

    @Column(
            name = "firm_name",
            length = 150
    )
    private String firmName;

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

    @Column(
            name = "address",
            length = 255
    )
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private Status status;
}