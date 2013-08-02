package com.dingcan.goods.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.dingcan.goods.model.Goods;
import com.dingcan.goods.model.GoodsRowMapper;
import com.dingcan.goods.model.GoodsVo;
import com.dingcan.goods.model.GoodsVoCount;
import com.dingcan.goods.model.GoodsVoRowMapper;
import com.dingcan.goods.model.Sales;
import com.dingcan.goods.model.SalesRowMapper;
import com.dingcan.goods.model.SalesVo;
import com.dingcan.goods.model.SalesVoRowMapper;
import com.dingcan.shop.model.Shop;
import com.dingcan.shop.model.ShopRowMapper;
import com.dingcan.user.model.User;
import com.dingcan.user.model.UserRowMapper;
import com.dingcan.util.PageModel;

public class GoodsDao extends JdbcDaoSupport{
	
	/**
	 * ������Ʒid������,�����ѯ20����Ʒ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getGoodsByIdList(){
		String sql = "SELECT * FROM t_goods order by GoodsId desc";
		List<Goods> listGoods ;
		
		try{
			listGoods = getJdbcTemplate().query(
					sql,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		}
		return listGoods;
	}
	
	@SuppressWarnings("unchecked")
	public Goods getGoodsByGoodsId(int goodsId){
		String sql = "SELECT * FROM t_goods where GoodsId = ?";
		Goods goods = null;
		
		try{
			goods = getJdbcTemplate().queryForObject(
					sql,new Object[] {goodsId}, new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			goods = null;
		}
		return goods;
	}
	
	/**
	 * ��ҳ��ʾ��Ʒ
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * Green Lei
	 * 2012-11-30 ����11:17:25 2012
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Goods> getGoodsPage(int pageSize,int pageNo){
		PageModel<Goods> result = new PageModel<Goods>();
		String sql = "SELECT * FROM t_goods order by GoodsId desc limit ?,?;";
		List<Goods> listGoods = new ArrayList<Goods>();
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		try{
			listGoods = getJdbcTemplate().query(
					sql,params,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		}
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setList(listGoods);
		result.setTotalRecords(getTotalRecordsOfGoods());
		return result;
	}
	
	public int getTotalRecordsOfGoods(){
		String sql = "SELECT count(1) FROM t_goods;";
		int result = getJdbcTemplate().queryForInt(sql);
		return result;
	}
	
	/**
	 * ��ѯ��Ʒ
	 * @param queryString
	 * @return
	 * Green Lei
	 * 2012-12-3 ����9:50:35 2012
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getGoodsListByQuery(String queryString){
		//ʹ����ģ����ѯ
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_goods a ");
		//�����ѯ�Ĺؼ���Ϊ��,���ѯ���еļ�¼,����ģ����ѯ
		if(queryString != null || "".equals(queryString)){
			sql.append("where (a.GoodsName like '%"+queryString+"%' or a.GoodsDescription like '%"+queryString+"%')");
		}
		sql.append(" order by GoodsId desc;");
		String sqlString = sql.toString();
		List<Goods> listGoods = new ArrayList<Goods>();
		try{
			listGoods = getJdbcTemplate().query(
					sqlString,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		}
		return listGoods;
	}
	
	/**
	 * ��ѯ�û�
	 * @param queryString
	 * @return
	 * Green Lei
	 * 2012-12-3 ����9:58:15 2012
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserListByQuery(String queryString){
		//ʹ����ģ����ѯ
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_user_info a ");
		//�����ѯ�Ĺؼ���Ϊ��,���ѯ���еļ�¼,����ģ����ѯ
		if(queryString != null || "".equals(queryString)){
			sql.append("where (a.UserName like '"+queryString+"%' or a.Location like '"+queryString+"%')");
		}
		sql.append(" order by UserId desc;");
		String sqlString = sql.toString();
		List<User> listUser = new ArrayList<User>();
		try{
			listUser = getJdbcTemplate().query(
					sqlString,new UserRowMapper());
		}catch(EmptyResultDataAccessException e){
			listUser = null;
		}
		return listUser;
	}
	
	/**
	 * ��ѯ�̼�
	 * @param queryString
	 * @return
	 * Green Lei
	 * 2012-12-3 ����10:00:09 2012
	 */
	@SuppressWarnings("unchecked")
	public List<Shop> getShopListByQuery(String queryString){
		//ʹ����ģ����ѯ
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_shop a ");
		//�����ѯ�Ĺؼ���Ϊ��,���ѯ���еļ�¼,����ģ����ѯ
		if(queryString != null || "".equals(queryString)){
			sql.append("where (a.ShopName like '%"+queryString+"%' or a.ShopDescription like '%"+queryString+"%')");
		}
		sql.append(" order by ShopId desc;");
		String sqlString = sql.toString();
		List<Shop> listShop = new ArrayList<Shop>();
		try{
			listShop = getJdbcTemplate().query(
					sqlString,new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			listShop = null;
		}
		return listShop;
	}
	
	/**
	 * ��ҳ��ѯ������Ʒ
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageModel<GoodsVo> getNewGoodsVoPage(int pageSize, int pageNo){
		PageModel<GoodsVo> result = new PageModel<GoodsVo>();
		String sql = "select t_goods.* ,t_shop.ShopName, t_shop.SignboardUrl from t_goods inner join t_shop on t_goods.Shopid=t_shop.Shopid" +
				" order by GoodsId desc limit ?,?;";
		List<GoodsVo> listGoods = new ArrayList<GoodsVo>();
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		try{
			listGoods = getJdbcTemplate().query(
					sql,params,new GoodsVoRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		}  
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setList(listGoods);
		result.setTotalRecords(getTotalRecordsOfGoods());
		return result;
	}
	
	/**
	 * ��ô��̼�������Ʒ
	 * @param goodsId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public GoodsVo getGoodsVoByGoodsId(int goodsId){
		String sql = "select t_goods.* ,t_shop.ShopName from t_goods inner join t_shop on t_goods.Shopid=t_shop.Shopid" +
				" where t_goods.goodsId=?;";
		GoodsVo goods = new GoodsVo();
		Object[] params = new Object[]{goodsId};
		try{
			goods = getJdbcTemplate().queryForObject(
					sql,params,new GoodsVoRowMapper());
		}catch(EmptyResultDataAccessException e){
			goods = null;
		}  
		return goods;
	}
	
	/**
	 * �����̼Ҳ�ѯǰ6���Ƽ���Ʒ
	 * @param shopId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getGoodsListByShopId(int shopId){
		String sql = "select * from t_goods where shopId=? order by GoodsId desc  limit 0,6";
		return getJdbcTemplate().query(sql,new Object[]{shopId},new GoodsRowMapper());
	}
	
	/**
	 * ������Ʒ�����ѯ��Ʒ
	 * @param cateId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getGoodsListByCateId(String cateId){
		String sql = "select * from t_goods where cate=? order by goodsid desc;";
		return getJdbcTemplate().query(sql,new Object[]{cateId},new GoodsRowMapper());
	}
	
	/**
	 * ��ѯ���µ���Ʒ
	 * @param quantity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getNewGoods(int quantity){
		String sql = "SELECT * FROM t_goods order by GoodsId desc limit 0,"+quantity;
		List<Goods> listGoods ;
		try{
			listGoods = getJdbcTemplate().query(sql,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		}
		return listGoods;
	}
	
	/**
	 * ���һ�����ۼ�¼
	 * @param sale
	 * @return
	 * 2013-4-30 ����12:06:26 2013
	 */
	public int addSale(Sales sale){
		String sql = "INSERT INTO `t_sales` (`buyerId`, `shopId`, `goodsId`, `quantity`, `date`) VALUES "+
				"(?, ?, ?, ?, ?);";
		Object[] param = new Object[] {sale.getBuyerId(),sale.getShopId(),sale.getGoodsId(),sale.getQuantity(),sale.getDate()};
		return getJdbcTemplate().update(sql, param);
	}
	
	/**
	 * ��ҵ��˵�
	 * �ı����ϲ�ѯ
	 * @param userId
	 * @return
	 * 2013-4-30 ����1:15:43 2013
	 */
	@SuppressWarnings("unchecked")
	public List<SalesVo> getSalesRecByUserId(int userId){
		//TODO�ı����ϲ�ѯЧ����,��Ҫ�޸�
		String sql = "SELECT t_sales.* , t_goods.PriceDiscount , t_goods.goodsName, t_shop.shopName, t_user_info.userName FROM t_sales "+
						"INNER JOIN t_goods ON "+
						"t_sales.goodsId=t_goods.goodsId "+
						"INNER JOIN t_user_info ON  " +
						"t_sales.buyerId=t_user_info.userId "+
						"INNER JOIN t_shop ON "+
						"t_sales.shopId=t_shop.shopId "+
						"WHERE "+
						"t_sales.buyerId=?;";
		List<SalesVo> listSalesVo ;
		try{
			listSalesVo = getJdbcTemplate().query(sql,new Object[]{userId}, new SalesVoRowMapper());
		}catch(EmptyResultDataAccessException e){
			listSalesVo = null;
		}
		return listSalesVo;
	}
	
	/**
	 * ���ҵ��˵�
	 * @param shopId
	 * @return
	 * 2013-4-30 ����1:33:43 2013
	 */
	@SuppressWarnings("unchecked")
	public List<SalesVo> getSalesRecByShopId(int shopId){
		//TODO�ı����ϲ�ѯЧ����,��Ҫ�޸�
		String sql = "SELECT t_sales.* , t_goods.PriceDiscount , t_goods.goodsName, t_shop.shopName, t_user_info.userName FROM t_sales "+
						"INNER JOIN t_goods ON "+
						"t_sales.goodsId=t_goods.goodsId "+
						"INNER JOIN t_user_info ON  " +
						"t_sales.buyerId=t_user_info.userId "+
						"INNER JOIN t_shop ON "+
						"t_sales.shopId=t_shop.shopId "+
						"WHERE "+
						"t_sales.shopId=?;";
		List<SalesVo> listSalesVo ;
		try{
			listSalesVo = getJdbcTemplate().query(sql,new Object[]{shopId}, new SalesVoRowMapper());
		}catch(EmptyResultDataAccessException e){
			listSalesVo = null;
		}
		return listSalesVo;
	}
	
	/**
	 * �����Ʒ�������ļ�¼
	 * @param goodsId
	 * @return
	 * 2013-4-30 ����9:05:12 2013
	 */
	@SuppressWarnings("unchecked")
	public List<Sales> getSalesRecByGoodsId(int goodsId){
		String sql = "SELECT * FROM t_sales where GoodsId = ?;";
		List<Sales> listSales ;
		try{
			listSales = getJdbcTemplate().query(sql,new Object[]{goodsId},new SalesRowMapper());
		}catch(EmptyResultDataAccessException e){
			listSales = null;
		}
		return listSales;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getBuyerByGoodsId(int goodsId, int count){
		String sql = "SELECT t_user_info.* " +
				"FROM t_sales INNER JOIN t_user_info " +
				"ON t_sales.buyerId=t_user_info.UserId WHERE t_sales.goodsId = ? order by id desc limit 0,?;";
		List<User> users ;
		try{
			users = getJdbcTemplate().query(sql,new Object[]{goodsId, count},new UserRowMapper());
		}catch(EmptyResultDataAccessException e){
			users = null;
		}
		return users;
	}
	
	/**
	 * ������,ʱ��,�۸������
	 * @param pageSize
	 * @param pageNo
	 * @param sort
	 * @return
	 * 2013-5-2 ����6:06:12 2013
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Goods> getGoodsPageBySort(int pageSize, int pageNo, String sort, int goodsCate){
		PageModel<Goods> result = new PageModel<Goods>();
		//TODO�ⲻ���������ʽ����params��,����Ч��
		String sql = "select * from t_goods where cate=? order by "+sort+" desc limit ?,?;";
		List<Goods> listGoods = new ArrayList<Goods>();
		Object[] params = new Object[]{goodsCate, (pageNo-1)*pageSize, pageSize};
		try{
			listGoods = getJdbcTemplate().query(
					sql,params,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		}  
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setList(listGoods);
		result.setTotalRecords(getTotalRecordsOfCate(goodsCate));
		result.setQueryString(sort);
		return result;
	}
	
	public int getTotalRecordsOfCate(int goodsCate){
		String sql = "SELECT count(1) FROM t_goods where cate=?;";
		int result = getJdbcTemplate().queryForInt(sql,new Object[]{goodsCate});
		return result;
	}
	
	public int addLikeGoods(int userId, int goodsId, String date){
		String sql = "INSERT INTO `t_like_goods` (`userId`, `goodsId`, `date`) " +
				"VALUES "+
				"(?, ?, ?);";
		Object[] param = new Object[] {userId,goodsId,date};
		return getJdbcTemplate().update(sql, param);
	}
	
	public int getLikeCount(int goodsId){
		String sql = "SELECT count(1) FROM t_like_goods where goodsid = "+goodsId+";";
		return getJdbcTemplate().queryForInt(sql);
	}
	
	public int addViewGoods(int userId, int goodsId, String date){
		String sql = "INSERT INTO `t_view_goods` (`userId`, `goodsId`, `date`) " +
				"VALUES "+
				"(?, ?, ?);";
		Object[] param = new Object[] {userId,goodsId,date};
		return getJdbcTemplate().update(sql, param);
	}
	
	public int getViewCount(int goodsId){
		String sql = "SELECT count(1) FROM t_view_goods where goodsid = "+goodsId+";";
		return getJdbcTemplate().queryForInt(sql);
	}
	
	public List<Sales> getSalesByGoodsId(int goodsId){
		String sql = "SELECT * FROM t_sales where goodsId ="+goodsId;
		return getJdbcTemplate().query(sql, new SalesRowMapper());
	}
	
	/**
	 * ������Ʒ����ѯ��Ʒlist
	 * @param pageNo
	 * @param pageSize
	 * @param goodsCate
	 * @return
	 * 2013-6-6 ����7:25:55 2013
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getGoodsLimitedByCate(int pageNo, int pageSize, int goodsCate){
		String sql = "select * from t_goods where cate=? order by goodsid desc limit ?,?;";
		List<Goods> listGoods = new ArrayList<Goods>();
		Object[] params = new Object[]{goodsCate, (pageNo-1)*pageSize, pageSize};
		try{
			listGoods = getJdbcTemplate().query(
					sql,params,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		} 
		return listGoods;
	}
	
	/**
	 * ��ʱ���ѯ������Ʒ
	 * @param pageNo
	 * @param pageSize
	 * @param goodsCate
	 * @return
	 * 2013-6-6 ����7:26:25 2013
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getAllGoodsLimited(int pageNo, int pageSize){
		String sql = "select * from t_goods order by goodsid desc limit ?,?;";
		List<Goods> listGoods = new ArrayList<Goods>();
		Object[] params = new Object[]{(pageNo-1)*pageSize, pageSize};
		try{
			listGoods = getJdbcTemplate().query(
					sql,params,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		} 
		return listGoods;
	}
}
