package com.campus.service;

import com.campus.Result.PageResult;
import com.campus.dto.CreditChangeDTO;
import com.campus.dto.UserPageQueryDTO;
import com.campus.vo.UserVO;

public interface AdminUserService {
    PageResult<UserVO> page(UserPageQueryDTO userPageQueryDTO);

    void updateCredit(CreditChangeDTO creditChangeDTO);
}
