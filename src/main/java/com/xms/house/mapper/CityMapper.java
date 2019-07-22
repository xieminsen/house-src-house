package com.xms.house.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xms.house.model.City;


@Mapper
public interface CityMapper {
  
  public List<City> selectCitys(City city);

}
