package com.example.demo.common.config;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate ;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;

/*
    Auditing 어노테이션을 제외할 때 추가 (@EntityListeners(AuditingEntityListener.class))

    @PrePersist
    public void onPrePersist() {
        this.createdDate = new Date(); // 생성시간 설정
        this.modifiedDate = this.createdDate; // 업데이트 시간도 동일하게 설정
    }

    @PreUpdate
    public void onPreUpdate() {
        this.modifiedDate = new Date(); // 업데이트 시간 갱신
    }
    */
}
