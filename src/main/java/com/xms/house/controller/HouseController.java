package com.xms.house.controller;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Objects;
import com.xms.house.common.CommonConstants;
import com.xms.house.common.HouseUserType;
import com.xms.house.common.LimitOffset;
import com.xms.house.common.RestCode;
import com.xms.house.common.RestResponse;
import com.xms.house.model.City;
import com.xms.house.model.Community;
import com.xms.house.model.House;
import com.xms.house.model.HouseQueryReq;
import com.xms.house.model.HouseUserReq;
import com.xms.house.model.ListResponse;
import com.xms.house.model.UserMsg;
import com.xms.house.service.HouseService;
import com.xms.house.service.RecommendService;

@RequestMapping("house")
@RestController
public class HouseController {
  
  @Autowired
  private HouseService houseService;
  
  @Autowired
  private RecommendService recommendService;

  /**
   * 查询房产列表
   * @param req
   * @return
   */
  @RequestMapping("list")
  public RestResponse<ListResponse<House>> houseList(@RequestBody HouseQueryReq req){
    Integer limit  = req.getLimit();
    Integer offset = req.getOffset();
    House   query  = req.getQuery();
    Pair<List<House>, Long> pair = houseService.queryHouse(query,LimitOffset.build(limit, offset));
    return RestResponse.success(ListResponse.build(pair.getKey(), pair.getValue()));
  }
  
  /**
   * 1.查询房屋详情
   * @param id
   * @return
   */
  @RequestMapping("detail")
  public RestResponse<House> houseDetail(long id){
    House house = houseService.queryOneHouse(id);
    recommendService.increaseHot(id);//查询房屋详情时添加房产热度redis
    return RestResponse.success(house);
  }
  
  /**
   * 1 添加留言功能
   * @param userMsg
   * @return
   */
  @RequestMapping("addUserMsg")
  public RestResponse<Object> houseMsg(@RequestBody UserMsg userMsg){
    houseService.addUserMsg(userMsg);
    return RestResponse.success();
  }
  
  /**
   * 1评分功能
   * @param rating
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping("rating")
  public RestResponse<Object> houseRate(Double rating,Long id){
    houseService.updateRating(id,rating);
    return RestResponse.success();
  }
  
  
  @RequestMapping("allCommunitys")
  public RestResponse<List<Community>> toAdd(){
    List<Community> list = houseService.getAllCommunitys();
    return RestResponse.success(list);
  }
  
  @RequestMapping("allCitys")
  public RestResponse<List<City>> allCitys(){
    List<City> list = houseService.getAllCitys();
    return RestResponse.success(list);
  }
  
  /**
   * 房产新增
   * @param house
   * @return
   */
  @RequestMapping("add")
  public RestResponse<Object> doAdd(@RequestBody House house){
    house.setState(CommonConstants.HOUSE_STATE_UP);
    if (house.getUserId() == null) {
      return RestResponse.error(RestCode.ILLEGAL_PARAMS);
    }
    houseService.addHouse(house,house.getUserId());
    return RestResponse.success();
  }
  
  /**
   * 绑定接口
   * @param req
   * @return
   */
  @RequestMapping("bind")
  public RestResponse<Object> delsale(@RequestBody HouseUserReq req){
    Integer bindType = req.getBindType();
    HouseUserType houseUserType = Objects.equal(bindType, 1) ? HouseUserType.SALE : HouseUserType.BOOKMARK;
    if (req.isUnBind()) {
    	//当用户取消收藏的时候解绑
      houseService.unbindUser2Houser(req.getHouseId(),req.getUserId(),houseUserType);
    }else {
      houseService.bindUser2House(req.getHouseId(), req.getUserId(), houseUserType);
    }
    return RestResponse.success();
  }
  
  /**
   * 1热门推荐
   * @param size
   * @return
   */
  @RequestMapping("hot")
  public RestResponse<List<House>> getHotHouse(Integer size){
   List<House> list =   recommendService.getHotHouse(size);
    return RestResponse.success(list);
  }
  
  /**
   * 1最近新加房产
   * @return
   */
  @RequestMapping("lastest")
  public RestResponse<List<House>> getLastest(){
    return RestResponse.success(recommendService.getLastest());
  }
  
}
