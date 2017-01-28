package com.paypal.test.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by rahulaw
 */

@MappedSuperclass
@Getter
public abstract class BaseEntity implements Serializable {

    @JsonIgnore
    @Column(name = "created_at", updatable = false, nullable = false)
    protected final Timestamp createdAt;

    @JsonIgnore
    @Column(name = "updated_at", insertable = false, updatable = false)
    protected Timestamp updatedAt;

    public BaseEntity() {
        this.createdAt = new Timestamp(new Date().getTime());
    }
}