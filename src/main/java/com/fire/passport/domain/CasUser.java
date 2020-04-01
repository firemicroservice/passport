package com.fire.passport.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CasUser {
    private String username;
    private String password;
    private boolean expired;
    private boolean disabled;
}
