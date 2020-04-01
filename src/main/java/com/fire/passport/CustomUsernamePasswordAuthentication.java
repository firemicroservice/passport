package com.fire.passport;

import com.fire.passport.domain.CasUser;
import com.fire.passport.mapper.CasUserMapper;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomUsernamePasswordAuthentication extends AbstractUsernamePasswordAuthenticationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUsernamePasswordAuthentication.class);

    private CasUserMapper casUserMapper;

    public CustomUsernamePasswordAuthentication(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order, CasUserMapper casUserMapper) {
        super(name, servicesManager, principalFactory, order);
        this.casUserMapper = casUserMapper;
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential, String originalPassword) throws GeneralSecurityException {

        String username = credential.getUsername();

        String password = credential.getPassword();
        System.out.println("=====================username: " + username );
        LOGGER.info("username: " + username );
        LOGGER.info("password: " + password);

        CasUser casUser =casUserMapper.getByUserName(username);


        if (casUser == null) {
            throw new AccountException("Sorry, username not found!");
        }

        if (!casUser.getPassword().equals(password)) {
            throw new FailedLoginException("Sorry, password not correct!");
        } else {

            //可自定义返回给客户端的多个属性信息
            Map<String, Object> returnInfo = new HashMap<>();
            returnInfo.put("expired", casUser.isExpired());
            returnInfo.put("disabled", casUser.isDisabled());

            final List<MessageDescriptor> list = new ArrayList<>();

            return createHandlerResult(credential,
                    this.principalFactory.createPrincipal(username, returnInfo), list);
        }

    }

}
