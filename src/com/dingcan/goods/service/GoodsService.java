package com.dingcan.goods.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.dingcan.comments.model.CommentVo;
import com.dingcan.comments.service.CommentsService;
import com.dingcan.goods.dao.GoodsDao;
import com.dingcan.goods.model.Goods;
import com.dingcan.goods.model.GoodsVo;
import com.dingcan.goods.model.GoodsVoCount;
import com.dingcan.goods.model.Sales;
import com.dingcan.goods.model.SalesVo;
import com.dingcan.message.model.Message;
import com.dingcan.message.service.MessageService;
import com.dingcan.shop.model.Shop;
import com.dingcan.shop.model.ShopVo;
import com.dingcan.user.model.User;
import com.dingcan.util.CartManager;
import com.dingcan.util.ConfigReader;
import com.dingcan.util.CustomDate;
import com.dingcan.util.PageModel;

@Service
public class GoodsService {
	@Autowired
	private CommentsService commentsService;
	@Autowired
	private MessageService messageService;
	ApplicationContext context = 
	 		new ClassPathXmlApplicationContext("Spring-Module.xml");
	GoodsDao goodsDao = (GoodsDao) context.getBean("goodsDao");
	
	public List<Goods> getGoodsListByShopId(int shopId){
 		return goodsDao.getGoodsListByShopId(shopId);
 	}
	
	public Goods getGoodsByGoodsId(int goodsId){
		return goodsDao.getGoodsByGoodsId(goodsId);
	}
	
	public PageModel<Goods> getGoodsPage(String pageNoStr){
		int pageSize = Integer.parseInt(ConfigReader.getValue("pagesize_mobile_home"));
		int pageNo = pageNoStr==null?1:Integer.parseInt(pageNoStr);
		return goodsDao.getGoodsPage(pageSize, pageNo);
	}
	
	/**
	 * ��ѯ��Ʒ
	 * @param query
	 * @return
	 * 2013-5-6 ����7:49:54 2013
	 */
	public List<Goods> getGoodsListByQuery(String query){
		return goodsDao.getGoodsListByQuery(query);
	}
	
	public List<User> getUserListByQuery(String query){
		return goodsDao.getUserListByQuery(query);
	}
	
	/**
	 * ��ѯ�̼�
	 * @param query
	 * @return
	 * 2013-5-6 ����7:50:04 2013
	 */
	public List<Shop> getShopListByQuery(String query){
		return goodsDao.getShopListByQuery(query);
	}
	
	/**
	 z ����ҳ��ʾ��Ʒ
	 * @param pageNoStr
	 * @return
	 * Green Lei
	 * 2012-12-7 ����5:07:19 2012
	 */
//	public PageModel<GoodsVo> getNewGoodsVoPage(String pageNoStr){
//		int pageSize = Integer.parseInt(ConfigReader.getValue("good_size_index"));
//		int pageNo = pageNoStr==null?1:Integer.parseInt(pageNoStr);
//		PageModel<GoodsVo> goodsVoPage = goodsDao.getNewGoodsVoPage(pageSize, pageNo);
//		List<GoodsVo> list = goodsVoPage.getList();
//		for(GoodsVo goodsVo:list){
//			List<CommentVo> commentVoList = commentsService.getCommentsVoByGoodsId(goodsVo.getGoodsId());
//			if(commentVoList.size() != 0){
//				goodsVo.setCommenterId(commentVoList.get(0).getCommenterId());
//				goodsVo.setCommenterName(commentVoList.get(0).getUserName());
//				goodsVo.setCommentsCount(commentVoList.size());
//				goodsVo.setCommentLatest(commentVoList.get(0).getContent());
//			}
//			goodsVo.setOrderCount(messageService.getOrderCountByGoodsId(goodsVo.getGoodsId()));
//		}
//		return goodsVoPage;
//	}
	
	/**
	 * ��ѯ��Ʒ,���̼����ͱ���������
	 * @param goodsIdStr
	 * @return
	 * 2013-4-30 ����6:04:02 2013
	 */
	public GoodsVo getGoodsVoByGoodsId(int goodsId){
		GoodsVo goodsVo = goodsDao.getGoodsVoByGoodsId(goodsId);
		goodsVo.setOrderCount(messageService.getOrderCountByGoodsId(goodsVo.getGoodsId()));
		goodsVo.setOrderCount(beBuyedCount(goodsId));
		return goodsVo;
	}
	
	/**
	 * ��������ѯ��Ʒ
	 * @param cate
	 * @return
	 */
	public List<Goods> getGoodsListByCate(String cate){
		return goodsDao.getGoodsListByCateId(cate);
	}
	
	/**
	 * ������µ���Ʒ
	 * @param quantity
	 * @return
	 */
	public List<Goods> getNewGoodsList(int quantity){
		return goodsDao.getNewGoods(quantity);
	}
	
	/**
	 * ���һ�����ۼ�¼
	 * @param shopId
	 * @param buyerId
	 * @param goodsId
	 * @param quantity
	 * @return
	 * 2013-4-30 ����12:13:23 2013
	 */
	public boolean addSale(int shopId, int buyerId, int goodsId, int quantity){
		Sales sale = new Sales();
		sale.setBuyerId(buyerId);
		sale.setDate(CustomDate.getStringDate());
		sale.setGoodsId(goodsId);
		sale.setQuantity(quantity);
		sale.setShopId(shopId);
		return goodsDao.addSale(sale)>0;
	}
	
	/**
	 * �û�����˵�
	 * @param userId
	 * @return
	 * 2013-4-30 ����12:59:20 2013
	 */
	public List<SalesVo> getSalesRecByUserId(int userId){
		return goodsDao.getSalesRecByUserId(userId);
	}
	
	/**
	 * �����˵�
	 * @param shopId
	 * @return
	 * 2013-4-30 ����1:34:10 2013
	 */
	public List<SalesVo> getSalesRecByShopId(int shopId){
		return goodsDao.getSalesRecByShopId(shopId);
	}
	
	/**
	 * ��ѯĳ����Ʒ������������
	 * @param goodsId
	 * @return
	 * 2013-4-30 ����6:03:34 2013
	 */
	public int beBuyedCount(int goodsId){
		List<Sales> sales = goodsDao.getSalesRecByGoodsId(goodsId);
		int count = 0;
		for(Sales sale:sales){
			count = count+sale.getQuantity();
		}
		return count;
	}
	
	/**
	 * ����������Ʒ����
	 * @param goodsId
	 * @param count  ��ȡ������
	 * @return
	 * 2013-4-30 ����9:21:16 2013
	 */
	public List<User> getBuyerByGoodsId(int goodsId, int count){
		return goodsDao.getBuyerByGoodsId(goodsId, count);
	}
	
	/**
	 * ����Ʒ��������,�����Ʒ��ҳ
	 * @param pageSize
	 * @param pageNo
	 * @param sort
	 * @param goodsCate
	 * @return
	 * 2013-5-2 ����6:13:34 2013
	 */
	public PageModel<Goods> getGoodsPageBySort(int pageSize, int pageNo, String sort, int goodsCate){
		//�������ʽΪ��,��Ĭ��Ϊ��ʱ��
		if(sort == null || "".equals(sort)){
			sort = "GoodsId";    
		}
		//���ÿҳ��ʾ��ѧΪ��,��Ĭ��Ϊ12
		if(pageSize == 0 || "".equals(pageSize)){
			pageSize = 12;    
		}
		return goodsDao.getGoodsPageBySort(pageSize, pageNo, sort, goodsCate);
	}
	
	/**
	 * ϲ��һ����Ʒ
	 * @param userId
	 * @param goodsId
	 * @return
	 * 2013-6-5 ����7:20:25 2013
	 */
	public boolean addLikeGoods(int userId, int goodsId){
 		return goodsDao.addLikeGoods(userId, goodsId, CustomDate.getStringDate())>0;
 	}
	
	/**
	 * ���������Ʒ
	 * @param pageNo
	 * @param pageSize
	 * @param goodsCate
	 * @return
	 * 2013-6-6 ����1:07:42 2013
	 */
	public List<GoodsVoCount> getGoodsVoCountListByCate(int pageNo, int pageSize, int goodsCate){
 		List<GoodsVoCount> shopVoList = new ArrayList<GoodsVoCount>();
 		GoodsVoCount goodsVoCount;
 		int saleCount = 0;
 		for(Goods goods : goodsDao.getGoodsLimitedByCate(pageNo, pageSize, goodsCate)){
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
	
	/**
	 * ��ʱ����������Ʒ
	 * @param pageNo
	 * @param pageSize
	 * @param goodsCate
	 * @return
	 * 2013-6-6 ����7:27:29 2013
	 */
	public List<GoodsVoCount> getGoodsVoCountList(int pageNo, int pageSize){
 		List<GoodsVoCount> shopVoList = new ArrayList<GoodsVoCount>();
 		GoodsVoCount goodsVoCount;
 		int saleCount = 0;
 		for(Goods goods : goodsDao.getAllGoodsLimited(pageNo, pageSize)){
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
}
