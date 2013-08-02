package com.dingcan.base;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.DispatcherServlet;

import com.dingcan.cons.Cons;
import com.dingcan.user.model.User;
import com.dingcan.user.service.UserService;
import com.dingcan.util.CookieManager;
/**
 * filterͳһ����,��ʵ���Զ���¼
 * @author Green Lei
 * 2012-11-29 ����2:13:33 2012
 */
public class CustomDispatcherServlet extends DispatcherServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
	
	 protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {  
		 request.setCharacterEncoding("utf-8");   //filterͳһ����
		 
		 HttpSession session = request.getSession(true);
		 String userid;
		 String sessionid;   // ��sessionid���ϴ��û���¼ʱ�������û��˵�ʶ���룬�����û��������ʵ��Զ���¼�����Ǳ��η��ʵ�session id��
		 Cookie[] cookies;
		 boolean isAutoLogin;
		 
		 User user = (User) session.getAttribute(Cons.USER_CONTEXT);
	 	 if (user == null) { 
	 		 //user = new User(); // ��ʱuser�е�username����Ϊ""����ʾ�û�δ��¼��
		     cookies = request.getCookies();
		     if(cookies != null){
		    	 userid = CookieManager.getCookieValue(cookies, Cons.AUTOLOGIN_USERID);
			     sessionid = CookieManager.getCookieValue(cookies, Cons.SESSIONID);
			     isAutoLogin = userService.isAutoLoginBySessionId(userid, sessionid); // ��������ݿ����ҵ�����Ӧ��¼����˵�������Զ���¼��
			     if (isAutoLogin) {
			    	 user = userService.getUserByUserId(userid);
			         session.setAttribute(Cons.USER_CONTEXT, user); // ��user bean��ӵ�session�С�
			     } 
		     }
		 }
		 super.doService(request, response);  
	 }  
}
