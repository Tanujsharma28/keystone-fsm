package com.keystone.backend.dto;

import java.util.List;
import java.util.Map;

public class ReportSummaryResponse {

    private Map<String, Long> statusCounts;
    private long overdueCount;
    private double slaCompliancePercent;
    private List<TechnicianLoad> byTechnician;

    public ReportSummaryResponse(Map<String, Long> statusCounts, long overdueCount,
                                  double slaCompliancePercent, List<TechnicianLoad> byTechnician) {
        this.statusCounts = statusCounts;
        this.overdueCount = overdueCount;
        this.slaCompliancePercent = slaCompliancePercent;
        this.byTechnician = byTechnician;
    }

    public Map<String, Long> getStatusCounts() { return statusCounts; }
    public long getOverdueCount() { return overdueCount; }
    public double getSlaCompliancePercent() { return slaCompliancePercent; }
    public List<TechnicianLoad> getByTechnician() { return byTechnician; }

    // Chhota inner class — ek technician ke naam + uske active jobs ki ginti
    public static class TechnicianLoad {
        private String technicianName;
        private long activeJobCount;

        public TechnicianLoad(String technicianName, long activeJobCount) {
            this.technicianName = technicianName;
            this.activeJobCount = activeJobCount;
        }

        public String getTechnicianName() { return technicianName; }
        public long getActiveJobCount() { return activeJobCount; }
    }
}