package com.campus.service;

import com.campus.vo.CategoryVO;
import com.campus.vo.UserQueryVO;

import java.util.List;

public interface UserQueryService {
    List<UserQueryVO> getCategories(String type);
    List<CategoryVO> getList (Long  id,String type);

    CategoryVO getDetail(Long id, String type);
}
