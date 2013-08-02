package com.dingcan.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dingcan.goods.model.Goods;

/**
 * ���ﳵ������
 * @author Administrator
 * 2013-4-29 ����12:48:56 2013
 *
 */
public class CartManager {
	/**
	 * �����ﳵ������Ʒ
	 * @param goods
	 * @param request
	 * 2013-4-29 ����1:03:52 2013
	 */
	public static void addGoods(Goods goods, HttpServletRequest request){
		HttpSession session = request.getSession(true);
		@SuppressWarnings("unchecked")
		List<Goods> cart = (List<Goods>)session.getAttribute("cart");
		if(cart == null){
			cart = new ArrayList<Goods>();
		}
		cart.add(goods);
		session.setAttribute("cart", cart);
	}
	
	/**
	 * ɾ����Ʒ
	 * @param goods
	 * @param request
	 * 2013-4-29 ����1:16:13 2013
	 */
	public static void delGoods(Goods goods, HttpServletRequest request){
		HttpSession session = request.getSession(true);
		@SuppressWarnings("unchecked")
		List<Goods> cart = (List<Goods>)session.getAttribute("cart");
		if(cart == null){
			cart = new ArrayList<Goods>();
		}
		for ( int i = 0; i < cart.size(); i ++ ) {
			if(goods.getGoodsId() == cart.get(i).getGoodsId()){
				cart.remove(i);
			}
		} 
		session.setAttribute("cart", cart);
	}
	
	/**
	 * ��չ��ﳵ
	 * @param request
	 * 2013-5-1 ����3:54:57 2013
	 */
	public static void clearGoods(HttpServletRequest request){
		request.getSession(true).removeAttribute("cart");
	}
	
	/**
	 * ��ȡ���ﳵ�е���Ʒ
	 * @param request
	 * @return
	 * 2013-4-29 ����1:17:27 2013
	 */
	@SuppressWarnings("unchecked")
	public static List<Goods> getGoodsList(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		List<Goods> list = (List<Goods>)session.getAttribute("cart");
		if(list == null){
			list = new ArrayList<Goods>();
		}
		for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {
			for ( int j = list.size() - 1 ; j > i; j -- ) {
				if (list.get(j).getGoodsId()==(list.get(i).getGoodsId())) {
					list.get(i).setCount(list.get(i).getCount()+list.get(j).getCount());
					list.remove(j);
				} 
	       } 
	    } 
		return list;
	}
	
	/**
	 * ����ܼ�
	 * @param request
	 * @return
	 * 2013-4-30 ����10:10:57 2013
	 */
	public static Double getTotalPrice(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		@SuppressWarnings("unchecked")
		List<Goods> list = (List<Goods>)session.getAttribute("cart");
		Double total = 0.0;
		if(list == null){
			list = new ArrayList<Goods>();
		}
		for(Goods goods:list){
			total = goods.getPriceDiscount()*goods.getCount()+total;
		}
		return total;
	}
	
	/**
	 * ����Ʒ����
	 * @param request
	 * @return
	 * 2013-4-30 ����10:11:50 2013
	 */
	@SuppressWarnings("unchecked")
	public static int getTotalNum(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		List<Goods> list= (List<Goods>)session.getAttribute("cart");
		int total = 0;
		if(list == null){
			list = new ArrayList<Goods>();
		}
		for(Goods goods:list){
			total = goods.getCount()+total;
		}
		return total;
	}
}
