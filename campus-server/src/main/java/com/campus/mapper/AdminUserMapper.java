package com.campus.mapper;

import com.campus.dto.CreditChangeDTO;
import com.campus.dto.UserPageQueryDTO;
import com.campus.vo.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    List<UserVO> page(UserPageQueryDTO userPageQueryDTO);
    void updateCredit(CreditChangeDTO creditChangeDTO);
    void updateCreditStatus(CreditChangeDTO creditChangeDTO);
}
