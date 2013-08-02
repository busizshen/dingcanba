package com.dingcan.user.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dingcan.base.BaseController;
import com.dingcan.cons.Cons;
import com.dingcan.goods.model.Goods;
import com.dingcan.goods.model.GoodsVo;
import com.dingcan.goods.service.GoodsService;
import com.dingcan.shop.model.Shop;
import com.dingcan.shop.service.ShopService;
import com.dingcan.user.model.AutoLogin;
import com.dingcan.user.model.User;
import com.dingcan.user.service.UserService;
import com.dingcan.util.CartManager;
import com.dingcan.util.ConfigReader;
import com.dingcan.util.CookieManager;
import com.dingcan.util.FileUploadForm;
import com.dingcan.util.FileUploadUtil;
import com.dingcan.util.ImageCut;
import com.dingcan.util.RandomInt;

@Controller
public class UserController extends BaseController{
	@Autowired
	private UserService userService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value = "/header.html", method = RequestMethod.GET)
	public ModelAndView header() {
		return new ModelAndView("header");
	}
	
	@RequestMapping(value = "/navigation", method = RequestMethod.GET)
	public ModelAndView navigation(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("navigation");
		mav.addObject("type", request.getParameter("type"));
		mav.addObject("totalPrice", CartManager.getTotalPrice(request));
		mav.addObject("totalNum", CartManager.getTotalNum(request));
		return mav;
	}
	
	@RequestMapping(value = "/banner", method = RequestMethod.GET)
	public ModelAndView banner() {
		return new ModelAndView("banner");
	}
	
	@RequestMapping(value = "/newsletter", method = RequestMethod.GET)
	public ModelAndView newsletter() {
		return new ModelAndView("newsletter");
	}
	
	@RequestMapping(value = "/footer", method = RequestMethod.GET)
	public ModelAndView footer() {
		return new ModelAndView("footer");
	}
	
	@RequestMapping(value = "/slide_nav", method = RequestMethod.GET)
	public ModelAndView slide_nav() {
		return new ModelAndView("slide_nav");
	}
	
	@RequestMapping(value = "/snavigation", method = RequestMethod.GET)
	public ModelAndView snavigation(HttpServletRequest request) {
		return new ModelAndView("snavigation","type",request.getParameter("type"));
	}
	
	@RequestMapping(value = "/unavigation", method = RequestMethod.GET)
	public ModelAndView unavigation(HttpServletRequest request) {
		return new ModelAndView("unavigation","type",request.getParameter("type"));
	}
	
	/**
	 * �̼��Ƽ�����Ʒ,���Ʋ�
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/recommend", method = RequestMethod.GET)
	public ModelAndView reommend(HttpServletRequest request) {
		int shopId = Integer.parseInt(request.getParameter("id"));
		List<Goods> list = shopService.getRecommend(shopId);
		return new ModelAndView("sside","recommend",list);
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		CookieManager.clearCookie(request, response);
		HttpSession session = request.getSession();
		session.removeAttribute(Cons.USER_CONTEXT);
		session.removeAttribute(Cons.SHOP_CONTEXT);
		return "redirect:index.html";
	}
	
	
	@RequestMapping(value = "/getProfileMobile", method = RequestMethod.GET)
	public ModelAndView getProfileMobile() {
		return new ModelAndView("mprofile");
	}
	
	@RequestMapping(value = "/updateProfileMobile", method = RequestMethod.POST)
	public ModelAndView updateProfileMobile(HttpServletRequest request) {
		String phone = request.getParameter("phone");
		String location = request.getParameter("location");
		User user = getSessionUser(request);
		user.setPhone(phone);
		user.setLocation(location);
		userService.updateUserPhoneAndLocation(user);
		return new ModelAndView("mprompt", "prompt", ConfigReader.getValue("message_tip5"));
	}

	/**
	 * �鿴�û�
	 * @param request
	 * @return
	 * 2013-4-27 ����10:00:30 2013
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView	user(HttpServletRequest request){
		int userId = Integer.parseInt(request.getParameter("id"));
		User user = getSessionUser(request);
		if(user == null){
			ModelAndView mav = new ModelAndView("login", "command", new User());
			mav.addObject("message", "�͹ٶԲ���,Ϊ�����˵İ�ȫ,�����ȵ�½!");
			mav.addObject("redirectTo", "user.html?id="+userId); //Ҫת����ҳ��
			return mav;
		}else if(userService.idFriends(user.getUserId(), userId)){
			ModelAndView mav = new ModelAndView("user");
			User friend = userService.getUserByUserId(userId+""); 
			mav.addObject("user", friend);
			mav.addObject("userName", friend.getUserName());
			mav.addObject("userPhoto", friend.getPhoto());
			mav.addObject("userId", friend.getUserId());
			return mav;
		}else{
			ModelAndView mav = new ModelAndView("user");
			User friend = userService.getUserByUserId(userId+""); 
			mav.addObject("userName", friend.getUserName());
			mav.addObject("userPhoto", friend.getPhoto());
			mav.addObject("userId", friend.getUserId());
			return mav;
		}
	}
	
	/**
	 * �޸�ͷ��
	 * @param goodsForm
	 * @param request
	 * @return
	 * 2013-5-1 ����11:25:58 2013
	 */
	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public String avatar(@ModelAttribute("goodsForm") FileUploadForm goodsForm,
			HttpServletRequest request){
		//�ļ��ϴ�
		List<MultipartFile> files = goodsForm.getFiles();
		String imagePath = FileUploadUtil.genetateUserimgPath(request);
		String imageUrl = null;    //���ݿ��б����url
		if(null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {
				//��չ��
				String fileExt = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
				String fileName = System.currentTimeMillis() + "." + fileExt;
				imageUrl = "userimg"+imagePath.split("userimg")[1]+"200w"+fileName;
				try{
					FileUploadUtil.inputstreamtofile(multipartFile.getInputStream(), imagePath, fileName);
					ImageCut.scale(imagePath+fileName, imagePath+"200w"+fileName,200,240);
					ImageCut.scale(imagePath+fileName, imagePath+"65w"+fileName,65,70);
					ImageCut.scale(imagePath+fileName, imagePath+"50w"+fileName,50,50);
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
		//�����û�ͷ��
		User user = getSessionUser(request);
		user.setPhoto(imageUrl);
		userService.updateUserAvatar(user);
		return "redirect:myhome.html";
	}
	
	@RequestMapping(value = "/userMobile", method = RequestMethod.GET)
	public ModelAndView	userMobile(HttpServletRequest request){
		String userId = request.getParameter("id");
		User user = userService.getUserByUserId(userId);
		return new ModelAndView("muser", "user", user);
	}
	
	@RequestMapping(value = "/setInfoMobile", method = RequestMethod.GET)
	public ModelAndView	setInfoMobile(){
		return new ModelAndView("msetInfo");
	}
	
	@RequestMapping(value = "/setInfoMobile", method = RequestMethod.POST)
	public String	doSetInfoMobile(HttpServletRequest request){
		String phone = request.getParameter("phone");
		String location = request.getParameter("location");
		User user = getSessionUser(request);
		user.setPhone(phone);
		user.setLocation(location);
		userService.updateUserPhoneAndLocation(user);
		return "redirect:indexMobile.html";
	}
	
	/**
	 * ��������
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/myhome", method = RequestMethod.GET)
	public ModelAndView myhome(HttpServletRequest request){
		User user = getSessionUser(request);
		String message = "����δ��½,���ȵ�½��ע��!";
		if(request.getParameter("message")!=null){     //�����ʾ��Ϣ��Ϊ��,��ʹ����ʾ��Ϣ
			try {
				message = new String(request.getParameter("message").getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(user == null){
			ModelAndView mav = new ModelAndView("login", "command", new User());
			mav.addObject("message", message);
			mav.addObject("redirectTo", "myhome.html"); //Ҫת����ҳ��
			return mav;
		}
		ModelAndView mav =  new ModelAndView("uhome", "user", user);
		mav.addObject("back", request.getParameter("back"));
		return mav;
	}
	
	/**
	 * ����˵�
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ubill", method = RequestMethod.GET)
	public ModelAndView	ubill(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("ubill");
		mav.addObject("bill", goodsService.getSalesRecByUserId(getSessionUser(request).getUserId()));
		mav.addObject("user", getSessionUser(request));
		return mav;
	}
	
	/**
	 * �ҵ��ղ�
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mycollect", method = RequestMethod.GET)
	public ModelAndView	mycollect(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("ucollect");
		mav.addObject("ShopList", userService.getCollectedShops(getSessionUser(request).getUserId()));
		mav.addObject("user", getSessionUser(request));
		return mav;
	}
	
	/**
	 * �ҵĺ���
	 * @return
	 * 2013-4-27 ����10:23:43 2013
	 */
	@RequestMapping(value = "/myfriends", method = RequestMethod.GET)
	public ModelAndView	myfriends(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("ufriends");
		mav.addObject("UserList", userService.getMyFriends(getSessionUser(request).getUserId()));
		mav.addObject("user", getSessionUser(request));
		return mav;
	}
	
	/**
	 * �Ӻ���
	 * @return
	 * 2013-4-28 ����12:48:23 2013
	 */
	@RequestMapping(value = "/addfriends", method = RequestMethod.GET)
	public @ResponseBody int addfriends(HttpServletRequest request){
		int friendId = Integer.parseInt(request.getParameter("id"));
		User user = getSessionUser(request);
		if(user == null){
			return 2;      //δ��¼
		}else if(userService.addFriend(user.getUserId(), friendId) == 1){
			return 1;      //��ӳɹ�
		}else if(userService.addFriend(user.getUserId(), friendId) == 0){
			return 0;      //�Ѿ�������
		}else{
			return -1;     //����쳣
		}
	}
	
	/**
	 * �жϸ��û����Ƿ����
	 * @param request
	 * @return
	 * 2013-4-29 ����9:05:54 2013
	 */
	@RequestMapping(value = "/existedUser", method = RequestMethod.POST)
	public @ResponseBody boolean existedUser(HttpServletRequest request){
		String UserPhone = request.getParameter("UserPhone");
		return userService.existedUser(UserPhone);
	}
	
	/**
	 * ����¼��ע��
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("login", "command", new User());
		mav.addObject("message", "�͹�!��ӭ�������Ͱ�...(*^__^*) ");
		return mav;
	}
	
	/**
	 * �û�ע��
	 * @param user
	 * @param result
	 * @param request
	 * @return
	 * 2013-4-29 ����10:12:43 2013
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String addContact(@ModelAttribute("user")
			User user, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		user.setUserName("·�˼�");
		user.setPhoto("default/avatar/200w_"+RandomInt.getRandomInt(0, 10)+".jpg");
		setSessionUser(request, user);
		user.setUserId(userService.addUser(user));
		//��������
		Shop shop = new Shop();
		shop.setUserId(user.getUserId());
		shop.setShopName("����Ƶ�û����");
		shop.setSignboardUrl("default/logo/w200_"+RandomInt.getRandomInt(0, 10)+".jpg");
		shopService.addshop(shop);
		//��ӵ��Զ���¼
		AutoLogin autoLogin = new AutoLogin();
		autoLogin.setSessionId(request.getSession().getId());
		autoLogin.setUserId(user.getUserId()+"");
		userService.addAutoLogin(autoLogin, response);  
		return "redirect:myhome.html";
	}
	
	/**
	 * ������Ϣ����
	 * @param user
	 * @param result
	 * @param request
	 * @return
	 * 2013-4-29 ����10:17:28 2013
	 */
	@RequestMapping(value = "/setInfo", method = RequestMethod.POST)
	public @ResponseBody boolean setInfo(HttpServletRequest request) {
		User user = getSessionUser(request);
		user.setLocation(request.getParameter("location"));
		user.setPhone(request.getParameter("phone"));
		user.setUserName(request.getParameter("userName"));
		return userService.updateUserInfo(user);
	}
	
	/**
	 * �û���¼
	 * @param user
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 * 2013-4-29 ����11:03:38 2013
	 */
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public ModelAndView doLogin(@ModelAttribute("user")
			User user, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		//�ض���ĵ�ַ,������Ǹ������Ļ����ҵ�����,���¼���ض�����ҳ
		String redirectTo = "".equals(request.getParameter("redirectTo")) || request.getParameter("redirectTo") == null?"index.html":request.getParameter("redirectTo");
		String gotoPage = "redirect:"+redirectTo;
		User userAim = userService.getUserByPhone(user.getPhone());
		if(userAim != null && userAim.getUserPwd().equals(user.getUserPwd())){
			setSessionUser(request, userAim);
			
			//��ӵ��Զ���¼
			AutoLogin autoLogin = new AutoLogin();
			autoLogin.setSessionId(request.getSession().getId());
			autoLogin.setUserId(userAim.getUserId()+"");
			userService.addAutoLogin(autoLogin, response);     		
			return new ModelAndView(gotoPage, "User", userAim);
		}else if(userAim != null && !userAim.getUserPwd().equals(user.getUserPwd())){
			ModelAndView model = new ModelAndView("redirect:myhome.html");
			model.addObject("message", "�������!");
			return model;
		}else if(userAim == null){
			ModelAndView model = new ModelAndView("redirect:myhome.html");
			model.addObject("message", "�˺Ų�����!");
			return model;
		}else{
			ModelAndView model = new ModelAndView("redirect:myhome.html");
			model.addObject("message", "��¼ʧ��,δ֪����!");
			return model;
		}
	}
	
	/**
	 * ����ղ�
	 * @param request
	 * @return
	 * 2013-4-29 ����11:33:25 2013
	 */
	@RequestMapping(value = "/addCollection", method = RequestMethod.GET)
	public @ResponseBody int addCollection(HttpServletRequest request){
		int shopId = Integer.parseInt(request.getParameter("id"));
		User user = getSessionUser(request);
		if(user == null){
			return 2;      //δ��¼
		}else if(userService.addCollection(user.getUserId(), shopId) == 1){
			return 1;      //��ӳɹ�
		}else if(userService.addCollection(user.getUserId(), shopId) == 0){
			return 0;      //�Ѿ����ղ�
		}else{
			return -1;     //����쳣
		}
	}
	
	/**
	 * ȡ���ղص��̼�
	 * @param request
	 * @return
	 * 2013-4-29 ����11:46:19 2013
	 */
	@RequestMapping(value = "/cancleCollect", method = RequestMethod.GET)
	public @ResponseBody boolean cancleCollect(HttpServletRequest request){
		int shopId = Integer.parseInt(request.getParameter("id"));
		return userService.cancleCollect(getSessionUser(request).getUserId(), shopId);
	}
	
	/**
	 * ɾ������
	 * @param request
	 * @return
	 * 2013-4-29 ����11:47:14 2013
	 */
	@RequestMapping(value = "/cancleFriend", method = RequestMethod.GET)
	public @ResponseBody boolean cancleFriend(HttpServletRequest request){
		int friendId = Integer.parseInt(request.getParameter("id"));
		return userService.delFriend(getSessionUser(request).getUserId(), friendId);
	}
	
	/**
	 * �������ǵĲ���
	 * @param request
	 * @return
	 * 2013-5-8 ����5:50:53 2013
	 */
	@RequestMapping(value = "/about_side", method = RequestMethod.GET)
	public ModelAndView about_side(HttpServletRequest request){
		return new ModelAndView("about_side");
	}
	
	/**
	 * ��������,��վʹ������
	 * @param request
	 * @return
	 * 2013-5-8 ����5:47:10 2013
	 */
	@RequestMapping(value = "/about_website", method = RequestMethod.GET)
	public ModelAndView about_website(HttpServletRequest request){
		return new ModelAndView("about_website");
	}
	
	/**
	 * ���ֻ���
	 * @param request
	 * @return
	 * 2013-5-8 ����5:54:52 2013
	 */
	@RequestMapping(value = "/about_credits", method = RequestMethod.GET)
	public ModelAndView about_credits(HttpServletRequest request){
		return new ModelAndView("about_credits");
	}
	
	/**
	 * �Ŷӳ�Ա
	 * @param request
	 * @return
	 * 2013-5-8 ����5:55:32 2013
	 */
	@RequestMapping(value = "/about_member", method = RequestMethod.GET)
	public ModelAndView about_member(HttpServletRequest request){
		return new ModelAndView("about_member");
	}
	
	/**
	 * ��ϵ����
	 * @param request
	 * @return
	 * 2013-5-8 ����5:56:00 2013
	 */
	@RequestMapping(value = "/about_contact", method = RequestMethod.GET)
	public ModelAndView about_contact(HttpServletRequest request){
		return new ModelAndView("about_contact");
	}
	
	@RequestMapping(value = "/honest", method = RequestMethod.GET)
	public ModelAndView honest(HttpServletRequest request){
		return new ModelAndView("honest");
	}
	
	@RequestMapping(value = "/addhonest", method = RequestMethod.GET)
	public ModelAndView addhonest(HttpServletRequest request){
		return new ModelAndView("honest");
	}
}
