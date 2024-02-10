package com.backend.seabook.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumPermission {
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    CUSTOMER_READ("customer:read"),
    CUSTOMER_CREATE("customer:create"),
    CUSTOMER_UPDATE("customer:update"),
    CUSTOMER_DELETE("customer:delete");

    @Getter
    private final String permission;
}
