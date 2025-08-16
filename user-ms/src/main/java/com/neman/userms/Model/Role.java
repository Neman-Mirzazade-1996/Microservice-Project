package com.neman.userms.Model;

import java.util.Collections;
import java.util.Set;

public enum Role {
    USER(Collections.emptySet()),
    ADMIN(Set.of(
            "user:read",
            "user:write",
            "user:delete"
    )),
    MANAGER(Set.of(
            "user:read"
    ));

    private final Set<String> permissions;

    Role(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
