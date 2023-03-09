package com.infinity.organization.events;
public record DepartmentChangeModel(String type, String action, Long DepartmentId, String CorrelationId) {
}
