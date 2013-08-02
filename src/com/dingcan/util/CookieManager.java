package com.dingcan.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dingcan.cons.Cons;

/**
 * ����cookie����,ͨ������ȡֵ
 * @author Green Lei
 * 2012-11-29 ����2:13:01 2012
 */
public class CookieManager {
	public static String getCookieValue(Cookie[] cookies, String cookieName){
		String value = null;
		for(Cookie c :cookies ){
			if(cookieName.equals(c.getName())){
				value = c.getValue();
			}
	    }
		return value;	
	}
	
	/**
	 * ɾ����¼��Ϣ
	 * @param request
	 * @param response
	 * 2013-4-29 ����4:02:20 2013
	 */
	public static void clearCookie(HttpServletRequest request,HttpServletResponse response) {  
		Cookie[] cookies = request.getCookies();  
		try {  
			for(Cookie c :cookies ){
				if(Cons.AUTOLOGIN_USERID.equals(c.getName()) || Cons.SESSIONID.equals(c.getName())){
					 Cookie cookie = new Cookie(c.getName(), null);  
				     cookie.setMaxAge(0);  
			         response.addCookie(cookie);  
				}
		    }
		 }catch(Exception ex){  
		       System.out.println("���Cookies�����쳣��");  
		 }   
	}  
	
	/**
	 * ����cookie
	 * @param cookies
	 * 2013-4-28 ����2:43:10 2013
	 */
	public static void traversalCookie(Cookie[] cookies){
		int n = 0;
		for(Cookie c :cookies ){
			n++;
			System.out.println(n+"---------"+c.getName()+":"+c.getValue());
	    }
	}
}
