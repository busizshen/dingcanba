package com.dingcan.user.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.dingcan.api.service.QqService;
import com.dingcan.api.service.RenrenService;
import com.dingcan.api.service.SinaService;
import com.dingcan.api.service.VpnService;
import com.dingcan.base.BaseController;
import com.dingcan.cons.Cons;
import com.dingcan.message.model.Message;
import com.dingcan.message.service.MessageService;
import com.dingcan.user.model.AutoLogin;
import com.dingcan.user.model.User;
import com.dingcan.user.model.UserThird;
import com.dingcan.user.service.UserService;
import com.dingcan.util.ConfigReader;
import com.dingcan.util.FileUploadUtil;

@Controller
public class ThirdUserLoginMobileController extends BaseController{
	@Autowired
	private SinaService sinaService;
	@Autowired
	private RenrenService renrenService;
	@Autowired
	private UserService userService;
	@Autowired
	private QqService qqService;
	@Autowired
	private VpnService vpnService;
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value = "/sinaLoginMobile", method = RequestMethod.GET)
	public ModelAndView sinaLoginMobile(HttpServletRequest request, HttpServletResponse response) throws WeiboException, JSONException {
		String code = request.getParameter("code");
		JSONObject tokenJson = null;
        if (code == null || code.length() == 0) {
            //ȱ����Ч��������ת����¼ҳȥ
            return new ModelAndView("shome");
        }
		
        tokenJson = sinaService.getTokenAndUidByCode(code);
        
		if (tokenJson != null) {
			String uid = tokenJson.getString("uid");
			UserThird userThird =  userService.getUserThirdByThirdIdAndLoginType(uid, Cons.SINA_LOGIN);
			if(userThird != null){       //�����������¼�����м�¼,�����Ѿ���¼��,ֱ����user����ȡ��Ϣ
				String userId = userThird.getUserId()+"";
				User user = userService.getUserByUserId(userId);
				setSessionUser(request, user);      //��ӵ�session
				
				AutoLogin autoLogin = new AutoLogin();
				autoLogin.setSessionId(request.getSession().getId());
				autoLogin.setUserId(userId);
				userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
				
				ModelAndView mav = new ModelAndView("mprofile");
				return mav;
			}else{
				String accessToken = tokenJson.getString("access_token");
				JSONObject userJson = sinaService.showUser(uid, accessToken);
			
				User user = new User();
				user.setUserName(userJson.getString("name"));
				user.setUserSex(userJson.getString("gender"));
				user.setPhoto(userJson.getString("profile_image_url"));
				int userId = userService.addUser(user);    //���һ�����û� 
				userThird = new UserThird();
				userThird.setAccessToken(accessToken);
				userThird.setInvalid("");
				userThird.setLoginType(Cons.SINA_LOGIN);
				userThird.setThirdUserId(uid);
				userThird.setUserId(userId);
				userService.addThirdUser(userThird);       //����������Ĺ���
				
				setSessionUser(request, user);      //��ӵ�session
				AutoLogin autoLogin = new AutoLogin();
				autoLogin.setSessionId(request.getSession().getId());
				autoLogin.setUserId(userId+"");
				userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
				
				ModelAndView mav = new ModelAndView("mprofile");
				return mav;
			}
		}
		ModelAndView mav = new ModelAndView("mhome");
		return mav;
	}
	
	@RequestMapping(value = "/renrenLoginMobile", method = RequestMethod.GET)
	public ModelAndView renrenLoginMobile(HttpServletRequest request, HttpServletResponse response) throws WeiboException, JSONException {
		String code = request.getParameter("code");
		org.json.simple.JSONObject tokenJson = null;
        if (code == null || code.length() == 0) {
            //ȱ����Ч��������ת����¼ҳȥ
            return new ModelAndView("shome");
        }
		
        tokenJson = renrenService.getTokenAndUidByCode(code);
        
		if (tokenJson != null) {
			String uid = ((org.json.simple.JSONObject)tokenJson.get("user")).get("id").toString();
			UserThird userThird =  userService.getUserThirdByThirdIdAndLoginType(uid, Cons.RENREN_LOGIN);
			if(userThird != null){       //�����������¼�����м�¼,�����Ѿ���¼��,ֱ����user����ȡ��Ϣ
				String userId = userThird.getUserId()+"";
				User user = userService.getUserByUserId(userId);
				setSessionUser(request, user);      //��ӵ�session
				
				AutoLogin autoLogin = new AutoLogin();
				autoLogin.setSessionId(request.getSession().getId());
				autoLogin.setUserId(userId);
				userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
				
				ModelAndView mav = new ModelAndView("mprofile");
				return mav;
			}else{
				JSONArray userArray = renrenService.showUser((String) tokenJson.get("access_token"));
				org.json.simple.JSONObject userJson = (org.json.simple.JSONObject) userArray.get(0);
				User user = new User();
				user.setUserName((String)userJson.get("name"));
				user.setUserSex("1".equals(userJson.get("sex").toString())?"m":"f");
				user.setPhoto((String)userJson.get("headurl"));
				int userId = userService.addUser(user);    //���һ�����û� 
				userThird = new UserThird();
				userThird.setAccessToken((String)tokenJson.get("access_token"));
				userThird.setInvalid("");
				userThird.setLoginType(Cons.RENREN_LOGIN);
				userThird.setThirdUserId(uid);
				userThird.setUserId(userId);
				userService.addThirdUser(userThird);       //����������Ĺ���
				
				setSessionUser(request, user);      //��ӵ�session
				AutoLogin autoLogin = new AutoLogin();
				autoLogin.setSessionId(request.getSession().getId());
				autoLogin.setUserId(userId+"");
				userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
				
				ModelAndView mav = new ModelAndView("mprofile");
				return mav;
			}
		}
        
		ModelAndView mav = new ModelAndView("mprofile");
		return mav;
	}
	
//	@RequestMapping(value = "/qqLoginMobile", method = RequestMethod.GET)
//	public ModelAndView qqLoginMobile(HttpServletRequest request, HttpServletResponse response) throws WeiboException, JSONException {
//		String code = request.getParameter("code");
//		String tokenStr = null;
//        if (code == null || code.length() == 0) {
//            //ȱ����Ч��������ת����¼ҳȥ
//            return new ModelAndView("shome");
//        }
//		
//        tokenStr = qqService.getAccessToken(code);
//        
//		if (tokenStr != null) {
//			String uid = qqService.getOpenId(tokenStr);
//			UserThird userThird =  userService.getUserThirdByThirdIdAndLoginType(uid, Cons.QQ_LOGIN);
//			if(userThird != null){       //�����������¼�����м�¼,�����Ѿ���¼��,ֱ����user����ȡ��Ϣ
//				String userId = userThird.getUserId()+"";
//				User user = userService.getUserByUserId(userId);
//				setSessionUser(request, user);      //��ӵ�session
//				
//				AutoLogin autoLogin = new AutoLogin();
//				autoLogin.setSessionId(request.getSession().getId());
//				autoLogin.setUserId(userId);
//				userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
//				
//				ModelAndView mav = new ModelAndView("mprofile");
//				return mav;
//			}else{
//				JSONObject userJson = qqService.getUserInfo(tokenStr, uid);
//				User user = new User();
//				user.setUserName(userJson.getString("nickname"));
//				user.setUserSex("��".equals(userJson.getString("gender"))?"m":"f");
//				user.setPhoto(userJson.getString("figureurl_1"));
//				int userId = userService.addUser(user);    //���һ�����û� 
//				userThird = new UserThird();
//				userThird.setAccessToken(tokenStr);
//				userThird.setInvalid("");
//				userThird.setLoginType(Cons.QQ_LOGIN);
//				userThird.setThirdUserId(uid);
//				userThird.setUserId(userId);
//				userService.addThirdUser(userThird);       //����������Ĺ���
//				
//				setSessionUser(request, user);      //��ӵ�session
//				AutoLogin autoLogin = new AutoLogin();
//				autoLogin.setSessionId(request.getSession().getId());
//				autoLogin.setUserId(userId+"");
//				userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
//				
//				qqService.updateStatus(tokenStr, uid, "0", 
//						"0", "0",
//						"0", "0");
//				
//				ModelAndView mav = new ModelAndView("mprofile");
//				return mav;
//			}
//		}
//        
//		ModelAndView mav = new ModelAndView("mprofile");
//		return mav;
//	}
	
	@RequestMapping(value = "/vpnLoginMobile", method = RequestMethod.POST)
	public String vpnLogin(HttpServletRequest request, HttpServletResponse response) throws WeiboException, JSONException {
		String savePath = FileUploadUtil.genetateUserimgPath(request);
		String userName = request.getParameter("name");
		String userPasw = request.getParameter("pasw");
		
		UserThird userThird =  userService.getUserThirdByThirdIdAndLoginType(userName, Cons.VPN_LOGIN);
		if(userThird != null){       //�����������¼�����м�¼,�����Ѿ���¼��,ֱ����user����ȡ��Ϣ
			String userId = userThird.getUserId()+"";
			User user = userService.getUserByUserId(userId);
			setSessionUser(request, user);      //��ӵ�session
			
			AutoLogin autoLogin = new AutoLogin();
			autoLogin.setSessionId(request.getSession().getId());
			autoLogin.setUserId(userId);
			userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
			
			return "redirect:userMobile.html?id="+userId;
		}else{
			User user = new User();
			try {
				user = vpnService.getUserInfo(userName, userPasw, savePath);
			} catch (Exception e) {
				return "redirect:indexMobile.html";
			}
			user.setUserPwd(userPasw);
			int userId = userService.addUser(user);    //���һ�����û� 
			userThird = new UserThird();
			userThird.setAccessToken("");
			userThird.setInvalid("");
			userThird.setLoginType(Cons.VPN_LOGIN);
			userThird.setThirdUserId(userName);
			userThird.setUserId(userId);
			userService.addThirdUser(userThird);       //����������Ĺ���
			
			user.setUserId(userId);
			setSessionUser(request, user);      //��ӵ�session
			AutoLogin autoLogin = new AutoLogin();
			autoLogin.setSessionId(request.getSession().getId());
			autoLogin.setUserId(userId+"");
			userService.addAutoLogin(autoLogin, response);     //��ӵ��Զ���¼�Ĺ���
			
			Message messageObj = new Message();
			messageObj.setContentText(user.getUserName()+"ʹ��vpn��½�˶��Ͱ�");
			messageObj.setPictureUrl(user.getPhoto());
			messageObj.setFromId(user.getUserId());
			messageObj.setType(Cons.ADD_LOGIN_MESSAGE);
			messageService.addMessage(messageObj);
			
			return "redirect:userMobile.html?id="+userId;
		}
	}
}
