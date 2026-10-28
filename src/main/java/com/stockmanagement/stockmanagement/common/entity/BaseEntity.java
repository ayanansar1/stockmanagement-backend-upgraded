package com.stockmanagement.stockmanagement.common.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {



    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(
            name = "updated_at",
            nullable = false
    )
    protected LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    protected Long version;

    @Column(nullable = false)
    protected boolean deleted = false;

    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    public void softDelete() {

        if (!deleted) {
            deleted = true;
            deletedAt = LocalDateTime.now();
        }
    }

    public void restore() {

        if (deleted) {
            deleted = false;
            deletedAt = null;
        }
    }
}
