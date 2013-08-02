package com.dingcan.goods.model;

//@SuppressWarnings("serial")
//public class Goods implements java.io.Serializable{
//	private int GoodsId;
//	private int ShopId;
//	private String GoodsName;
//	private String GoodsDescription;
//	private String GoodsUrl;        //��ƷͼƬ��ַ
//	private String PriceOrganal;    //ԭ��
//	private String PriceDiscount;   //���ۼ�
//	private int count;            //���ﳵ�еĸ���
//	private int cate;              //��Ʒ���
//	
//	public int getShopId() {
//		return ShopId;
//	}
//	public void setShopId(int shopId) {
//		ShopId = shopId;
//	}
//	public String getGoodsName() {
//		return GoodsName;
//	}
//	public void setGoodsName(String goodsName) {
//		GoodsName = goodsName;
//	}
//	public String getGoodsDescription() {
//		return GoodsDescription;
//	}
//	public void setGoodsDescription(String goodsDescription) {
//		GoodsDescription = goodsDescription;
//	}
//	public String getGoodsUrl() {
//		return GoodsUrl;
//	}
//	public void setGoodsUrl(String goodsUrl) {
//		GoodsUrl = goodsUrl;
//	}
//	public String getPriceOrganal() {
//		return PriceOrganal;
//	}
//	public void setPriceOrganal(String priceOrganal) {
//		PriceOrganal = priceOrganal;
//	}
//	public String getPriceDiscount() {
//		return PriceDiscount;
//	}
//	public void setPriceDiscount(String priceDiscount) {
//		PriceDiscount = priceDiscount;
//	}
//	public int getGoodsId() {
//		return GoodsId;
//	}
//	public void setGoodsId(int goodsId) {
//		GoodsId = goodsId;
//	}
//	public int getCate() {
//		return cate;
//	}
//	public void setCate(int cate) {
//		this.cate = cate;
//	}
//	public int getCount() {
//		return count;
//	}
//	public void setCount(int count) {
//		this.count = count;
//	}
//}

/**
 * �����˶̽���shortDescription,
 * @author Administrator
 * 2013-4-30 ����12:03:16 2013
 *
 */
@SuppressWarnings("serial")
public class Goods implements java.io.Serializable{
	private int GoodsId;
	private int ShopId;
	private String GoodsName;
	private String GoodsDescription;
	private String GoodsUrl;        //��ƷͼƬ��ַ
	private Double PriceOrganal;    //ԭ��
	private Double PriceDiscount;   //���ۼ�
	private int count;            //���ﳵ�еĸ���
	private int cate;              //��Ʒ���
	//���ݿ��в����ڵ��ֶ�
	private String shortDescription; //�̽��� 
	private String bigImage;    //��Ʒ��ͼ
	
	public int getShopId() {
		return ShopId;
	}
	public void setShopId(int shopId) {
		ShopId = shopId;
	}
	public String getGoodsName() {
		return GoodsName;
	}
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	public String getGoodsDescription() {
		return GoodsDescription;
	}
	public void setGoodsDescription(String goodsDescription) {
		GoodsDescription = goodsDescription;
	}
	public String getGoodsUrl() {
		return GoodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		GoodsUrl = goodsUrl;
	}
	public int getGoodsId() {
		return GoodsId;
	}
	public Double getPriceOrganal() {
		return PriceOrganal;
	}
	public void setPriceOrganal(Double priceOrganal) {
		PriceOrganal = priceOrganal;
	}
	public Double getPriceDiscount() {
		return PriceDiscount;
	}
	public void setPriceDiscount(Double priceDiscount) {
		PriceDiscount = priceDiscount;
	}
	public void setGoodsId(int goodsId) {
		GoodsId = goodsId;
	}
	public int getCate() {
		return cate;
	}
	public void setCate(int cate) {
		this.cate = cate;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getShortDescription() {
		String shortDes = this.GoodsDescription;
		if(shortDes.length() > 15){
			shortDes = shortDes.substring(0, 15)+"...";
		}
		if(shortDes.trim().length() == 0){
			shortDes = "����Ʒû�н���";
		}
		return shortDes;
	}
	public String getBigImage() {
		return this.GoodsUrl.replace("200w", "360w");
	}
}

