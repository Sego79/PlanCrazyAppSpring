package com.plancrazyappfrontofficespring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "progress_status")
public class ProgressStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="progress_status_id", nullable = false, unique = true)
    private Long progressStatusId;

    @Column(nullable = false, unique = true)
    private String status;

    public ProgressStatus() {

    }

    public Long getProgressStatusId() {
        return progressStatusId;
    }

    public void setProgressStatusId(Long progressStatusId) {
        this.progressStatusId = progressStatusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
