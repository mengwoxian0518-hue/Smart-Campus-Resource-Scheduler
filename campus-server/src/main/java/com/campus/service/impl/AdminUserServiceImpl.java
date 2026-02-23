package com.campus.service.impl;

import com.campus.Result.PageResult;
import com.campus.dto.CreditChangeDTO;
import com.campus.dto.UserPageQueryDTO;
import com.campus.mapper.AdminUserMapper;
import com.campus.service.AdminUserService;
import com.campus.vo.UserVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    AdminUserMapper adminUserMapper;
    @Override
    public PageResult<UserVO> page(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
       Page<UserVO> page = (Page<UserVO>)adminUserMapper.page(userPageQueryDTO);
       return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void updateCredit(CreditChangeDTO creditChangeDTO) {
        adminUserMapper.updateCredit(creditChangeDTO);
        if(creditChangeDTO.getAction().equals("ADD"))
        {
            creditChangeDTO.setType(1);
        }
        else creditChangeDTO.setType(2);
        creditChangeDTO.setCreateTime(LocalDateTime.now());
        adminUserMapper.updateCreditStatus(creditChangeDTO);
    }
}
