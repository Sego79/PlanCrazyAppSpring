package com.plancrazyappfrontofficespring.controller.dto;

public class ShareRequest {
    private String appUserToShareEmail;
    private Long taskId;

    public ShareRequest() {
    }

    public String getAppUserToShareEmail() {
        return appUserToShareEmail;
    }

    public void setAppUserToShareEmail(String appUserToShareEmail) {
        this.appUserToShareEmail = appUserToShareEmail;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
