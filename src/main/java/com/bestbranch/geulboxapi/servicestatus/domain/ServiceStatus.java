package com.bestbranch.geulboxapi.servicestatus.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity(name = "service_status")
@Getter
public class ServiceStatus {

    @Id
    @Column(name = "service_status_no")
    private Long serviceStatusNo;

    @Column(name = "service_availability_yn")
    private String serviceAvailabilityYn;

    @Column(name = "service_status_message")
    private String serviceStatusMessage;

    @Column(name = "shutdown_yn")
    private String shutdownYn;
}
