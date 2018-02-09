package com.even.jwt.jwt;

import com.even.jwt.vo.UserVo;

public interface UserService {
    UserVo getUserVoByMobile(String mobile);
}
