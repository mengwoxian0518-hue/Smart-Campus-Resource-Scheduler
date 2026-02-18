package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO implements Serializable {
   private Long id;
   private String name;
   private String image;
   private Integer status;
   private Integer capacity;
   private String location;
   private String description;
   private Integer creditCost;
   private Integer price;
}
