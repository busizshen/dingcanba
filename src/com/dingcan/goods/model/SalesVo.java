package com.dingcan.goods.model;

/**
 * ���׵���ϸ,������Ʒ����,����,�̼�
 * @author Administrator
 * 2013-4-30 ����12:25:52 2013
 *
 */
@SuppressWarnings("serial")
public class SalesVo extends Sales {
	private String goodsName;
	private String shopName;
	private String userName;
	private int price;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
