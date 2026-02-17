package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO {
   private Long id;
   private String name;
   private String image;
   private Integer status;
   private Integer capacity;
   private String location;
   private String description;
}
