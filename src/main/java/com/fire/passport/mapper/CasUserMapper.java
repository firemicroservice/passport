package com.fire.passport.mapper;

import com.fire.passport.domain.CasUser;
import org.apache.ibatis.annotations.Mapper;

//@Mapper
public interface CasUserMapper {
    CasUser getByUserName(String username);
}
