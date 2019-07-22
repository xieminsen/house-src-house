package com.xms.house.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.xms.house.common.LimitOffset;
import com.xms.house.model.House;


@Service
public class RecommendService {
  
  private static final String HOT_HOUSE_KEY = "_hot_house";
  
  @Autowired
  private HouseService houseService;
  
  @Autowired
  private StringRedisTemplate redisTemplate;

  
  public List<House> getHotHouse(Integer size) {
//  Set<String> idSet =  redisTemplate.opsForZSet().range(HOT_HOUSE_KEY, 0, -1);
    Set<String> idSet =  redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, -1);//bug修复，根据热度从高到底排序
    List<Long> ids = idSet.stream().map(b -> Long.parseLong(b)).collect(Collectors.toList());
    House query = new House();
    query.setIds(ids);
    return houseService.queryAndSetImg(query, LimitOffset.build(size, 0));
  }
  
  /**
   * 1设置房产热度
   * @param id
   */
  public void increaseHot(long id) {
    redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY, ""+id, 1.0D);//每次点击都会增加一个热门分数
    redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY, 0, -11);//redis只保留前10位
  }

  public List<House> getLastest() {
    House query = new House();
    query.setSort("create_time");
    return houseService.queryAndSetImg(query, LimitOffset.build(8, 0));
  }
  
  
  
}
