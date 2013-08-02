package com.dingcan.api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.dingcan.util.ConfigReader;
import com.dingcan.util.CustomDate;

import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

/**
 * ֧����
 * @author Administrator
 * 2013-5-23 ����8:42:08 2013
 *
 */
@Service
public class AliPayService{

	public static boolean startPay(String price, String out_trade_no, String goodsUrl, String receive_name, String receive_address, 
							String receive_zip, String receive_mobile){
		//������������������
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "trade_create_by_buyer");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", "1");
		sParaTemp.put("notify_url", "http://www.xn--4qrz40kyoi.com/alipay_notify.html");
		sParaTemp.put("return_url", "http://www.xn--4qrz40kyoi.com/alipay_return.html");
		sParaTemp.put("seller_email", "13898176737");
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", "���Զ��ͰɵĶ���");
		sParaTemp.put("price", price);
		sParaTemp.put("quantity", "1");
		sParaTemp.put("logistics_fee", "0.00");
		sParaTemp.put("logistics_type", "EXPRESS");
		sParaTemp.put("logistics_payment", "SELLER_PAY");
		sParaTemp.put("body", "���Զ��ͰɵĶ���");
		sParaTemp.put("show_url", goodsUrl);
		sParaTemp.put("receive_name", receive_name);
		sParaTemp.put("receive_address", receive_address);
		sParaTemp.put("receive_zip", receive_zip);
		sParaTemp.put("receive_phone", receive_mobile);
		sParaTemp.put("receive_mobile", receive_mobile);
		
		//��������
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"post","ȷ��");
		System.out.println(sHtmlText);
		return false;		
	}
	
	public static void main(String[] args){
		AliPayService.startPay("0.1", CustomDate.getOrderNum(), "http://www.xn--4qrz40kyoi.com/product.html?id=100", 
				"��ǿ", "2A554", "110000", "13312341234");
	}
}
