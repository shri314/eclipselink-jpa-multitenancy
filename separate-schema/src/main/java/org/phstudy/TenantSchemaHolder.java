package org.phstudy;

import org.springframework.stereotype.Component;

@Component
public class TenantSchemaHolder {

    static private ThreadLocal<String> storage = new ThreadLocal<>();

    public static String getTenantSchema() {
        return storage.get();
    }

    public static void setTenantSchema(String tenantSchema) {
        storage.set(tenantSchema);
    }

    public static void clearTenantSchema() {
        storage.remove();
    }
}
