package com.keystone.backend.repository;

import com.keystone.backend.domain.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>, JpaSpecificationExecutor<WorkOrder> {
    List<WorkOrder> findByAssignedTechnician_Id(Long technicianId);
}