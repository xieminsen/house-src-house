package com.xms.house.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xms.house.mapper.CityMapper;
import com.xms.house.model.City;


@Service
public class CityService {
  
  @Autowired
  private CityMapper cityMapper;
  
  public List<City> getAllCitys(){
    City query = new City();
    return cityMapper.selectCitys(query);
  }

}
