package com.dingcan.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.dingcan.goods.dao.GoodsDao;
import com.dingcan.goods.model.Goods;
import com.dingcan.goods.model.GoodsVoCount;
import com.dingcan.goods.model.Sales;
import com.dingcan.shop.dao.ShopDao;
import com.dingcan.shop.model.Recommend;
import com.dingcan.shop.model.Shop;
import com.dingcan.shop.model.ShopVo;
import com.dingcan.util.ConfigReader;
import com.dingcan.util.CustomDate;
import com.dingcan.util.PageModel;

@Service
public class ShopService {
	 ApplicationContext context = 
 		new ClassPathXmlApplicationContext("Spring-Module.xml");

	 ShopDao shopDao = (ShopDao) context.getBean("shopDao");
	 GoodsDao goodsDao = (GoodsDao) context.getBean("goodsDao");
 	 
 	 public boolean addshop(Shop shop){
 		shop.setCreateDate(CustomDate.getStringDate());
 		return shopDao.addShop(shop)>0;
 	 }
 	 
 	 public Shop getShopByAccount(String shopAccount){
 		return shopDao.findByShopAccount(shopAccount);
 	 }
 	 
 	 /**
 	  * �����Ʒ��,��������
 	  * @param goods
 	  * @return
 	  * Green Lei
 	  * 2012-12-2 ����11:30:54 2012
 	  */
 	 public int addGoods(Goods goods){
 		return shopDao.addGoods(goods);
 	 }
 	 
 	 /**
 	  * ����Ʒlist����,ÿ5��һ��,
 	  * @param shopId
 	  * @return
 	  * Green Lei
 	  * 2012-11-29 ����8:23:17 2012
 	  */
 	 public Map<String, List<Goods>> getGoodsByShopId(int shopId){
 		Map<String, List<Goods>> map = new HashMap<String, List<Goods>>(); 
 		List<Goods> goodsList = shopDao.getGoodsByShopId(shopId);
 		for (int i = 0; i < goodsList.size(); i += 5 )  {
 			if(i+5>goodsList.size()){    //�����ѭ��һ�ξʹ���list.size,�����ֵȡ��goodsList.size()����ֹѭ��
 				map.put("goodsList"+i, goodsList.subList(i, goodsList.size()));
 			}else{
 				map.put("goodsList"+i, goodsList.subList(i, i+5));
 			}
 		}
 		return map;
 	 }
 	 
 	 /**
 	  * �����̼�id,��ѯ���е���Ʒ
 	  * @param shopId
 	  * @return
 	  */
 	public List<Goods> getGoodsListByShopId(int shopId){
 		return shopDao.getGoodsByShopId(shopId);
 	 }
 	
 	/**
 	 * �����̼�id��ҳ��ѯ
 	 * @param pageNo
 	 * @param pageSize
 	 * @param shopId
 	 * @return
 	 * 2013-6-12 ����5:13:36 2013
 	 */
 	public List<GoodsVoCount> getGoodsVoCountByShopId(int pageNo, int pageSize, int shopId){
 		List<GoodsVoCount> shopVoList = new ArrayList<GoodsVoCount>();
 		GoodsVoCount goodsVoCount;
 		int saleCount = 0;
 		for(Goods goods : shopDao.getGoodsLimitedByShopId(pageNo,pageSize,shopId)){
 			goodsVoCount = new GoodsVoCount();
 			goodsVoCount.setGoods(goods);
 			goodsVoCount.setLike(goodsDao.getLikeCount(goods.getGoodsId()));
 			for(Sales sales:goodsDao.getSalesByGoodsId(goods.getGoodsId())){
 				saleCount = saleCount + sales.getQuantity();
 			}
 			goodsVoCount.setSale(saleCount);
 			shopVoList.add(goodsVoCount);
 			saleCount = 0;
 		}
 		return shopVoList;
 	}
 	 
 	 public Goods getGoodsByGoodsId(int goodsId){
 		return shopDao.getGoodsByGoodsId(goodsId);
 	 }
 	 
 	 public Shop getShopByShopId(int shopId){
 		 return shopDao.getShopByShopId(shopId);
 	 }
 	 
 	 /**
 	  * ��������id��ѯ
 	  * @param pageNoStr
 	  * @return
 	  * 2013-5-8 ����11:27:28 2013
 	  */
 	public PageModel<Shop> getShopPageById(int pageNo, int pageSize){
		return shopDao.getShopPage(pageNo, pageSize);
	}
 	
 	/**
 	 * ������Ʒ����������
 	 * @param pageNoStr
 	 * @return
 	 * 2013-5-8 ����12:10:03 2013
 	 */
 	public PageModel<Shop> getShopPageByShopName(int pageNo, int pageSize){
		return shopDao.getShopPageByShopName(pageNo, pageSize);
	}
 	
 	/**
 	 * �����̼ҽ�����
 	 * @param pageNoStr
 	 * @return
 	 * 2013-5-8 ����1:22:54 2013
 	 */
 	public PageModel<Shop> getShopPageBySales(int pageNo, int pageSize){
 		//TODO����ķѵ���Դ̫����
		return shopDao.getShopPageBySale(pageNo, pageSize);
	}
 	
 	/**
 	 * �����������̼�list
 	 * @param pageNo
 	 * @param pageSize
 	 * @return
 	 * 2013-6-6 ����5:45:41 2013
 	 */
 	public List<ShopVo> getShopVoListBySales(int pageNo, int pageSize){
 		List<ShopVo> shopVoList = new ArrayList<ShopVo>();
 		ShopVo shopVo;
 		double turnover = 0;
 		for(Map<String, Object> map : shopDao.getShopIdBySale(pageNo, pageSize)){
 			shopVo = new ShopVo();
 			shopVo.setShop(shopDao.getShopByShopId((Integer)map.get("shopId")));
 			shopVo.setLike(shopDao.getLikeCount((Integer)map.get("shopId")));
 			shopVo.setView(shopDao.getViewCount((Integer)map.get("shopId")));
 			
 			for(Sales sales:shopDao.getSalesByShopId((Integer)map.get("shopId"))){
 				if(shopDao.getGoodsByGoodsId(sales.getGoodsId()) == null){
 					turnover = turnover + 0;
 				}else{
 					turnover = turnover + shopDao.getGoodsByGoodsId(sales.getGoodsId()).getPriceDiscount()*sales.getQuantity();
 				}
 			}
 			shopVo.setTurnover(turnover);
 			shopVoList.add(shopVo);
 			turnover = 0;
 		}
 		return shopVoList;
 	}
 	
 	/**
 	 * ������Ʒ����ѯ�̼�
 	 * @param pageNo
 	 * @param pageSize
 	 * @return
 	 * 2013-6-6 ����6:43:54 2013
 	 */
 	public List<ShopVo> getShopVoListBySum(int pageNo, int pageSize){
 		List<ShopVo> shopVoList = new ArrayList<ShopVo>();
 		ShopVo shopVo;
 		double turnover = 0;
 		for(Map<String, Object> map : shopDao.getShopIdBySum(pageNo, pageSize)){
 			shopVo = new ShopVo();
 			shopVo.setShop(shopDao.getShopByShopId((Integer)map.get("shopId")));
 			shopVo.setLike(shopDao.getLikeCount((Integer)map.get("shopId")));
 			shopVo.setView(shopDao.getViewCount((Integer)map.get("shopId")));
 			
 			for(Sales sales:shopDao.getSalesByShopId((Integer)map.get("shopId"))){
 				if(shopDao.getGoodsByGoodsId(sales.getGoodsId()) == null){
 					turnover = turnover + 0;
 				}else{
 					turnover = turnover + shopDao.getGoodsByGoodsId(sales.getGoodsId()).getPriceDiscount()*sales.getQuantity();
 				}
 			}
 			shopVo.setTurnover(turnover);
 			shopVoList.add(shopVo);
 			turnover = 0;
 		}
 		return shopVoList;
 	}
 	
 	/**
 	 * ������Ʒ��������
 	 * @param pageNo
 	 * @param pageSize
 	 * @return
 	 * 2013-5-8 ����2:55:17 2013
 	 */
 	public PageModel<Shop> getShopPageBySum(int pageNo, int pageSize){
 		//TODO����ķѵ���Դ̫����
		return shopDao.getShopPageBySum(pageNo, pageSize);
	}
 	
 	public List<Shop> getNewShopList(){
 		int count = Integer.parseInt(ConfigReader.getValue("shop_new_size"));
 		return shopDao.getNewShopList(count);
 	}
 	
 	/**
 	 * ɾ����Ʒ
 	 * @param id
 	 * @return
 	 */
 	public boolean delGoodsById(String id){
 		return shopDao.delGoodsById(id)>0;
 	}
 	
 	/**
 	 * ������Ʋ�
 	 * @param shopId
 	 * @param goodsId
 	 * @return
 	 */
 	public boolean addRecommend(int shopId, int goodsId){
 		return shopDao.addRecommendGoods(shopId, goodsId)>0;
 	}
 	
 	/**
 	 * ɾ�����Ʋ�
 	 * @param goodsId
 	 * @return
 	 */
 	public boolean delRecommend(int goodsId){
 		return shopDao.delRecommendGoods(goodsId)>0;
 	}
 	
 	/**
 	 * �����̼ҵ�id,��ȡ���̼ҵ����Ʋ�
 	 * @param shopId
 	 * @return
 	 */
 	public List<Goods> getRecommend(int shopId){
 		return shopDao.getRecommendGoods(shopId);
 	}
 	
 	/**
 	 * ��������̼�
 	 * @param quantity
 	 * @return
 	 * 2013-4-27 ����9:40:20 2013
 	 */
 	public List<Shop> getNewShopList(int quantity){
 		return shopDao.getNewShopList(quantity);
 	}
 	
 	/**
 	 * �����û�id�����������
 	 * @param userId
 	 * @return
 	 * 2013-5-1 ����11:29:17 2013
 	 */
 	public Shop getShopByUserId(int userId){
 		return shopDao.getShopByUserId(userId);
 	}
 	
 	public boolean setShopLogo(Shop shop){
 		return shopDao.updateShopLogo(shop)>0;
 	}
 	
 	/**
 	 * �̼���Ϣ����
 	 * @param shop
 	 * @return
 	 * 2013-5-1 ����11:43:44 2013
 	 */
 	public boolean setShopInfo(Shop shop){
 		return shopDao.updateShopInfo(shop)>0;
 	}
 	
 	/**
 	 * ϲ��һ���̼�
 	 * @param userId
 	 * @param shopId
 	 * @return
 	 * 2013-6-5 ����10:44:35 2013
 	 */
 	public boolean addLikeShop(int userId, int shopId){
 		return shopDao.addLikeShop(userId, shopId, CustomDate.getStringDate())>0;
 	}
 	
 	/**
 	 * �̼ұ��鿴�Ĵ���
 	 * @param userId
 	 * @param shopId
 	 * @return
 	 * 2013-6-5 ����11:23:11 2013
 	 */
 	public boolean addViewShop(int userId, int shopId){
 		return shopDao.addViewShop(userId, shopId, CustomDate.getStringDate())>0;
 	}
 	
 	/**
 	 * �����̼����ͺ�����鿴�̼�
 	 * @param pageNo
 	 * @param pageSize
 	 * @return
 	 * 2013-6-5 ����1:22:44 2013
 	 */
 	public List<ShopVo> getShopVoListByShoptypeAndSort(int pageNo, int pageSize, int shopType, String sort){
 		List<ShopVo> shopVoList = new ArrayList<ShopVo>();
 		ShopVo shopVo;
 		double turnover = 0;
 		for(Shop shop : shopDao.getShopListByTypeAndSort(pageNo, pageSize, shopType, sort)){
 			shopVo = new ShopVo();
 			shopVo.setShop(shop);
 			shopVo.setLike(shopDao.getLikeCount(shop.getShopId()));
 			shopVo.setView(shopDao.getViewCount(shop.getShopId()));
 			
 			for(Sales sales:shopDao.getSalesByShopId(shop.getShopId())){
 				if(shopDao.getGoodsByGoodsId(sales.getGoodsId()) == null){
 					turnover = turnover + 0;
 				}else{
 					turnover = turnover + shopDao.getGoodsByGoodsId(sales.getGoodsId()).getPriceDiscount()*sales.getQuantity();
 				}
 			}
 			shopVo.setTurnover(turnover);
 			shopVoList.add(shopVo);
 			turnover = 0;
 		}
 		return shopVoList;
 	}
}
