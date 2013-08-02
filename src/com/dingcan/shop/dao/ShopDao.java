package com.dingcan.shop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.dingcan.goods.model.Goods;
import com.dingcan.goods.model.GoodsRowMapper;
import com.dingcan.goods.model.Sales;
import com.dingcan.goods.model.SalesRowMapper;
import com.dingcan.shop.model.Recommend;
import com.dingcan.shop.model.RecommendRowMapper;
import com.dingcan.shop.model.Shop;
import com.dingcan.shop.model.ShopRowMapper;
import com.dingcan.user.model.User;
import com.dingcan.util.PageModel;

public class ShopDao extends JdbcDaoSupport{

	public int addShop(Shop shop){
		String sql = "INSERT INTO `t_shop` (`ShopName`, `UserId`,`SignboardUrl`,`CreateDate`) VALUES "+
				"(?, ?, ?, ?);";
		Object[] param = new Object[] {shop.getShopName(),shop.getUserId(),shop.getSignboardUrl(),shop.getCreateDate()};
		return getJdbcTemplate().update(sql, param);
	}	
	
	/**
	 * �����̼��˺Ų�ѯһ���̼�
	 * @param shopAccount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Shop findByShopAccount(String shopAccount){
		String sql = "SELECT * FROM t_shop WHERE ShopAccount = ?";
		Shop shop = null;
		
		try{
			shop = (Shop)getJdbcTemplate().queryForObject(
					sql, new Object[] { shopAccount }, 
					new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			shop = null;
		}
		return shop;
	}

	/**
	 * ����һ����Ʒ
	 * @param goods
	 * @return
	 */
	/**
	 * �޸�Ϊ������Ʒ��,��������
	 * @param goods
	 * @return
	 * Green Lei
	 * 2012-12-2 ����11:30:27 2012
	 */
	public int addGoods(final Goods goods){
		final String sql = "INSERT INTO `t_goods` (`ShopId`, `GoodsName`, `GoodsDescription`, " +
				"`GoodsUrl`, `cate`, `PriceOrganal`, `PriceDiscount`, `count`) " +
				"VALUES "+
				"(?, ?, ?, ?, ?, ?, ?, ?);";
		int key = 0;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, goods.getShopId());
				ps.setString(2, goods.getGoodsName());
				ps.setString(3, goods.getGoodsDescription());
				ps.setString(4, goods.getGoodsUrl());
				ps.setInt(5, goods.getCate());
				ps.setDouble(6, goods.getPriceOrganal());
				ps.setDouble(7, goods.getPriceDiscount());
				ps.setInt(8, goods.getCount());
				return ps;
			}
		};
		getJdbcTemplate().update(psc, keyHolder);
		key = keyHolder.getKey().intValue();
		return key;
	}
	
	@SuppressWarnings("unchecked")
	public List<Goods> getGoodsByShopId(int shopId){
		String sql = "SELECT * FROM t_goods WHERE ShopId = ?";
		List<Goods> listGoods;
		
		try{
			listGoods = getJdbcTemplate().query(
					sql, new Object[] { shopId }, 
					new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		}
		return listGoods;
	}
	
	@SuppressWarnings("unchecked")
	public List<Goods> getGoodsLimitedByShopId(int pageNo, int pageSize, int shopId){
		String sql = "select * from t_goods where ShopId=? order by goodsid desc limit ?,?;";
		List<Goods> listGoods = new ArrayList<Goods>();
		Object[] params = new Object[]{shopId, (pageNo-1)*pageSize, pageSize};
		try{
			listGoods = getJdbcTemplate().query(
					sql,params,new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			listGoods = null;
		} 
		return listGoods;
	}
	
	@SuppressWarnings("unchecked")
	public Goods getGoodsByGoodsId(int goodsId){
		String sql = "SELECT * FROM t_goods WHERE GoodsId = ?";
		Goods goods = null;
		
		try{
			goods = (Goods) getJdbcTemplate().queryForObject(
					sql, new Object[] { goodsId }, 
					new GoodsRowMapper());
		}catch(EmptyResultDataAccessException e){
			goods = null;
		}
		return goods;
	}
	
	@SuppressWarnings("unchecked")
	public Shop getShopByShopId(int shopId){
		String sql = "SELECT * FROM t_shop WHERE ShopId = ?";
		Shop shop = null;
		
		try{
			shop = (Shop) getJdbcTemplate().queryForObject(
					sql, new Object[] { shopId }, 
					new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			shop = null;
		}
		return shop;
	}
	
	/**
	 * �����̼ҵ�id����
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * 2013-5-8 ����1:27:22 2013
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Shop> getShopPage(int pageNo, int pageSize){
		PageModel<Shop> result = new PageModel<Shop>();
		String sql = "SELECT * FROM t_shop order by ShopId desc limit ?,?;";
		List<Shop> listShop = new ArrayList<Shop>();
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		try{
			listShop = getJdbcTemplate().query(
					sql,params,new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			listShop = null;
		}
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setList(listShop);
		result.setTotalRecords(getTotalRecordsOfShop());
		return result;
	}
	
	/**
	 * �����̼�����ѯ
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * 2013-5-8 ����11:29:21 2013
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Shop> getShopPageByShopName(int pageNo, int pageSize){
		PageModel<Shop> result = new PageModel<Shop>();
		String sql = "SELECT * FROM t_shop order by ShopName desc limit ?,?;";
		List<Shop> listShop = new ArrayList<Shop>();
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		try{
			listShop = getJdbcTemplate().query(
					sql,params,new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			listShop = null;
		}
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setList(listShop);
		result.setTotalRecords(getTotalRecordsOfShop());
		return result;
	}
	
	/**
	 * ������������
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * 2013-5-8 ����11:45:43 2013
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Shop> getShopPageBySale(int pageNo, int pageSize){
		PageModel<Shop> result = new PageModel<Shop>();
		String sql = "select t_shop.ShopId,t_shop.UserId,t_shop.ShopName,t_shop.Feixin,t_shop.ShopDescription,t_shop.ShopAccount,t_shop.ShopPassword,t_shop.SignboardUrl,t_shop.PasswordAsk,t_shop.PasswordAnswer,t_shop.CreateDate,"+
						"count(*) as IsAuth "+
						"from t_sales right outer join t_shop on t_sales.shopid = t_shop.shopid "+
						"group by shopid "+
						"order by IsAuth desc limit ?,?;";
		List<Shop> listShop = new ArrayList<Shop>();
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		try{
			listShop = getJdbcTemplate().query(
					sql,params,new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			listShop = null;
		}
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setList(listShop);
		result.setTotalRecords(getTotalRecordsOfShop());
		return result;
	}
	
	/**
	 * �̼һ�������
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * 2013-5-8 ����1:27:59 2013
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Shop> getShopPageBySum(int pageNo, int pageSize){
		PageModel<Shop> result = new PageModel<Shop>();
		String sql = "select t_shop.ShopId,t_shop.UserId,t_shop.ShopName,t_shop.Feixin,t_shop.ShopDescription,t_shop.ShopAccount,t_shop.ShopPassword,t_shop.SignboardUrl,t_shop.PasswordAsk,t_shop.PasswordAnswer,t_shop.CreateDate,"+
				"count(*) as IsAuth "+
				"from t_goods right outer join t_shop on t_goods.shopid = t_shop.shopid "+
				"group by shopid "+
				"order by IsAuth desc limit ?,?;";
		List<Shop> listShop = new ArrayList<Shop>();
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		try{
			listShop = getJdbcTemplate().query(
					sql,params,new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			listShop = null;
		}
		result.setPageNo(pageNo);
		result.setPageSize(pageSize);
		result.setList(listShop);
		result.setTotalRecords(getTotalRecordsOfShop());
		return result;
	}
	
	public int getTotalRecordsOfShop(){
		String sql = "SELECT count(1) FROM t_shop;";
		return getJdbcTemplate().queryForInt(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<Shop> getNewShopList(int count){
		String sql = "select * from t_shop order by shopId desc limit 0,"+count;
		return getJdbcTemplate().query(sql, new ShopRowMapper());
	}
	
	public int delGoodsById(String id){
		String sql = "delete from t_goods WHERE goodsid=?";
		return getJdbcTemplate().update(sql, new Object[]{id});
	}
	
	/**
	 * ����Ϊ���Ʋ�
	 * @param id
	 * @return
	 */
	public int addRecommendGoods(int shopId, int goodsId){
		String sql = "insert into t_recommend (shopId, goodsId) values (?,?);";
		return getJdbcTemplate().update(sql, new Object[]{shopId,goodsId});
	}
	
	/**
	 * ȡ�����Ʋ�
	 * @param goodsId
	 * @return
	 */
	public int delRecommendGoods(int goodsId){
		String sql = "delete from t_recommend where goodsId=?;";
		return getJdbcTemplate().update(sql, new Object[]{goodsId});
	}
	
	/**
	 * ��ѯ���Ʋ�
	 * @param shopId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Goods> getRecommendGoods(int shopId){
		String sql = "select * from t_goods where goodsid in (select goodsid from t_recommend where shopid =?);";
		return getJdbcTemplate().query(sql, new Object[]{shopId}, new GoodsRowMapper());
	}
	
	/**
	 * ������µ��̼�
	 * @param quantity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Shop> getNewShop(int quantity){
		String sql = "SELECT * FROM t_shop order by shopId desc limit 0,"+quantity;
		return getJdbcTemplate().query(sql, new ShopRowMapper());
	}
	
	/**
	 * �����û�id��ѯ��������
	 * @param userId
	 * @return
	 * 2013-4-29 ����11:18:48 2013
	 */
	@SuppressWarnings("unchecked")
	public Shop getShopByUserId(int userId){
		String sql = "SELECT * FROM t_shop WHERE UserId = ?";
		Shop shop = null;
		
		try{
			shop = (Shop) getJdbcTemplate().queryForObject(
					sql, new Object[] { userId }, 
					new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			shop = null;
		}
		return shop;
	}	
	
	public int updateShopLogo(Shop shop){
		String sql = "UPDATE `t_shop` SET `SignboardUrl` = ? WHERE ShopId=?;";
		Object[] param = new Object[] {shop.getSignboardUrl(),shop.getShopId()};
		return getJdbcTemplate().update(sql, param);
	}
	
	public int updateShopInfo(Shop shop){
		String sql = "UPDATE `t_shop` SET `ShopDescription` = ?,`Feixin` = ?, `ShopName` = ? WHERE ShopId=?;";
		Object[] param = new Object[] {shop.getShopDescription(),shop.getFeixin(),shop.getShopName(),shop.getShopId()};
		return getJdbcTemplate().update(sql, param);
	}
	
	public int addLikeShop(int userId, int shopId, String date){
		String sql = "INSERT INTO `t_like_shop` (`userId`, `shopId`, `date`) " +
				"VALUES "+
				"(?, ?, ?);";
		Object[] param = new Object[] {userId,shopId,date};
		return getJdbcTemplate().update(sql, param);
	}
	
	public int getLikeCount(int shopId){
		String sql = "SELECT count(1) FROM t_like_shop where shopid = "+shopId+";";
		return getJdbcTemplate().queryForInt(sql);
	}
	
	public int addViewShop(int userId, int shopId, String date){
		String sql = "INSERT INTO `t_view_shop` (`userId`, `shopId`, `date`) " +
				"VALUES "+
				"(?, ?, ?);";
		Object[] param = new Object[] {userId,shopId,date};
		return getJdbcTemplate().update(sql, param);
	}
	
	public int getViewCount(int shopId){
		String sql = "SELECT count(1) FROM t_view_shop where shopid = "+shopId+";";
		return getJdbcTemplate().queryForInt(sql);
	}
	
	public List<Sales> getSalesByShopId(int shopId){
		String sql = "SELECT * FROM t_sales where shopId ="+shopId;
		return getJdbcTemplate().query(sql, new SalesRowMapper());
	}
	
	/**
	 * ����������
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * 2013-6-6 ����5:20:39 2013
	 */
	public List<Map<String, Object>> getShopIdBySale(int pageNo, int pageSize){
		String sql = "select shopId from t_sales group by shopid order by sum(quantity) desc limit ?,?;";
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		List<Map<String, Object>> listShopId = new ArrayList<Map<String, Object>>();
		try{
			listShopId = getJdbcTemplate().queryForList(sql, params);
		}catch(EmptyResultDataAccessException e){
			listShopId = null;
		}
		return listShopId;
	}
	
	/**
	 * ������Ʒ������
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * 2013-6-6 ����6:33:28 2013
	 */
	public List<Map<String, Object>> getShopIdBySum(int pageNo, int pageSize){
		String sql = "select ShopId from t_goods group by shopid order by count(1) desc limit ?,?;";
		Object[] params = new Object[]{(pageNo-1)*pageSize,pageSize};
		List<Map<String, Object>> listShopId = new ArrayList<Map<String, Object>>();
		try{
			listShopId = getJdbcTemplate().queryForList(sql, params);
		}catch(EmptyResultDataAccessException e){
			listShopId = null;
		}
		return listShopId;
	}
	
	@SuppressWarnings("unchecked")
	public List<Shop> getShopListByTypeAndSort(int pageNo, int pageSize, int type, String sort){
		String sql = "SELECT * FROM t_shop where IsAuth = ? order by "+sort+" limit ?,?;";
		List<Shop> listShop = new ArrayList<Shop>();
		Object[] params = new Object[]{type, (pageNo-1)*pageSize, pageSize};
		try{
			listShop = getJdbcTemplate().query(
					sql,params,new ShopRowMapper());
		}catch(EmptyResultDataAccessException e){
			listShop = null;
		}
		return listShop;
	}
}
