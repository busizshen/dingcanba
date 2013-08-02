package com.dingcan.tradition.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weibo4j.model.WeiboException;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.dingcan.api.service.FeixinService;
import com.dingcan.api.service.QqService;
import com.dingcan.base.BaseController;
import com.dingcan.comments.model.Comment;
import com.dingcan.comments.service.CommentsService;
import com.dingcan.cons.Cons;
import com.dingcan.goods.model.Goods;
import com.dingcan.goods.model.GoodsVo;
import com.dingcan.goods.model.GoodsVoCount;
import com.dingcan.goods.service.GoodsService;
import com.dingcan.message.model.Message;
import com.dingcan.message.model.MessageVo;
import com.dingcan.message.service.MessageService;
import com.dingcan.shop.model.Shop;
import com.dingcan.shop.model.ShopVo;
import com.dingcan.shop.service.ShopService;
import com.dingcan.tag.model.Tag;
import com.dingcan.tag.service.TagService;
import com.dingcan.user.model.AutoLogin;
import com.dingcan.user.model.User;
import com.dingcan.user.model.UserThird;
import com.dingcan.user.service.UserService;
import com.dingcan.util.CartManager;
import com.dingcan.util.ConfigReader;
import com.dingcan.util.CookieManager;
import com.dingcan.util.CustomDate;
import com.dingcan.util.PageModel;

@Controller
public class ProductController extends BaseController{
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private TagService tagService;
	@Autowired
	private CommentsService commentsService;
	@Autowired
	private FeixinService feixinService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
	@Autowired
	private QqService qqService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(){
		ModelAndView mav = new ModelAndView("index");
//		mav.addObject("userList", userService.getNewUserList(5));
//		mav.addObject("shopList", shopService.getNewShopList(5));
//		mav.addObject("goodsList", goodsService.getNewGoodsList(5));
		mav.addObject("stroll", messageService.getMessageVoList(1, 12));
		return mav;
	}
	
	@RequestMapping("/index/{id}")
	public void index(@PathVariable int id, HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		List<MessageVo> list = messageService.getMessageVoList(id, 12);
		StringBuffer htmlStr = new StringBuffer();
		for(MessageVo messageVo:list){
			htmlStr.append("<div class=\"item masonry_brick\">");
			htmlStr.append("	<div class=\"item_t\">");
			htmlStr.append("		<div class=\"img\">");
			htmlStr.append("			<a href=\"product.html?id="+messageVo.getGoods().getGoodsId()+"\"><img width=\"210\" height=\"240\" alt=\""+messageVo.getGoods().getGoodsName()+"\" src=\""+messageVo.getGoods().getGoodsUrl()+"\" /></a>");
			htmlStr.append("			<span class=\"price\">�� "+messageVo.getGoods().getPriceDiscount()+"</span>");
			htmlStr.append("			<div class=\"btns\">");
			htmlStr.append("				<a onclick=\"addCart("+messageVo.getGoods().getGoodsId()+")\" class=\"img_album_btn\">����ͳ�</a>");
			htmlStr.append("			</div>");
			htmlStr.append("		</div>");
			htmlStr.append("	<div class=\"title\">");
			htmlStr.append("		<div class=\"title1\"> ");
			htmlStr.append("		<div class=\"tiimg pull-left\">");
			htmlStr.append("			<a href=\"user.html?id="+messageVo.getUser().getUserId()+"\">");
			htmlStr.append("				<img alt=\""+messageVo.getUser().getUserName()+"\" src=\""+messageVo.getUser().getPhoto50()+"\" />");
			htmlStr.append("			</a>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"titext pull-left\">");
			htmlStr.append("			<div class=\"titext_1\">");
			htmlStr.append("				<div class=\"tname pull-left\"><a href=\"user.html?id="+messageVo.getUser().getUserId()+"\">"+messageVo.getUser().getUserName()+"</a></div>");
			htmlStr.append("				<div class=\"add_follow pull-left\" onclick=\"addFriends("+messageVo.getUser().getUserId()+")\"></div> ");
			htmlStr.append("				<div class=\"clear\"></div>");
			htmlStr.append("			</div>");
			htmlStr.append("			<div class=\"titext_2\">");
			htmlStr.append("				"+messageVo.getDate()+" ����");
			htmlStr.append("			</div>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"clear\"></div>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"title2\">");
			htmlStr.append("			<h2>"+messageVo.getGoods().getGoodsName()+"</h2>");
			htmlStr.append("		</div>");
			htmlStr.append("	</div>");
			htmlStr.append("	</div>");
			htmlStr.append("	<div class=\"item_b clearfix\">");
			htmlStr.append("		<div class=\"items_likes fl\">");
			htmlStr.append("			<a class=\"like_btn\"  onclick=\"addLike("+messageVo.getGoods().getGoodsId()+")\"></a>");
			htmlStr.append("			<em class=\"bold\" id=\"like"+messageVo.getGoods().getGoodsId()+"\">"+messageVo.getLike()+"</em>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"items_comment fr\"><a onclick=\"buyNow("+messageVo.getGoods().getGoodsId()+")\">��������</a><em class=\"bold\">("+messageVo.getSale()+")</em></div>");
			htmlStr.append("	</div>");
			htmlStr.append("</div>");
		}
		out.print(htmlStr);
	}
	
	
//	@RequestMapping(value="/getProductAjax")
//	public @ResponseBody PageModel<GoodsVo> getProductAjax(HttpServletRequest request) throws NumberFormatException, TimeoutException, InterruptedException{
//		String currentPage = request.getParameter("p");	
//		PageModel<GoodsVo> pageModel = goodsService.getNewGoodsVoPage(currentPage);
//		return pageModel;
//	}
	
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public ModelAndView getProductPage(HttpServletRequest request) throws Exception{
//		String productId = request.getParameter("id");
//		GoodsVo goods =  goodsService.getGoodsVoByGoodsId(productId);
//		ModelAndView mav = new ModelAndView("detail");
//		mav.addObject("product", goods);
//		mav.addObject("comments", commentsService.getCommentsVoByGoodsId(goods.getGoodsId()));
//		mav.addObject("order", messageService.getOrderListByGoodsId(goods.getGoodsId()));
//		mav.addObject("recommends", goodsService.getGoodsListByShopId(goods.getShopId()));
		int goodsId = Integer.parseInt(request.getParameter("id"));
		GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
		ModelAndView mav = new ModelAndView("detail");
		mav.addObject("product", goodsVo);
		mav.addObject("recommend", shopService.getRecommend(goodsVo.getShopId()));
		mav.addObject("newBuyer", goodsService.getBuyerByGoodsId(goodsId, 6));
		return mav;
	}
	
	/**
	 * �����������Ʒ,get�ύ������,�������id��
	 * @param request
	 * @return
	 * @throws Exception
	 * 2013-4-27 ����10:44:29 2013
	 */
	@RequestMapping(value="/find", method = RequestMethod.GET)
	public ModelAndView toFind(HttpServletRequest request) throws Exception{
		int cateId = Integer.parseInt(request.getParameter("id"));
		String sort = request.getParameter("sort")==null?"GoodsId":request.getParameter("sort");
		int pageNo = request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
		ModelAndView mav = new ModelAndView("goodslist");
		mav.addObject("goodsPage", goodsService.getGoodsPageBySort(12, pageNo, sort, cateId));
		mav.addObject("cateId", cateId);
		mav.addObject("cateName", ConfigReader.getValue("product_category").split(",")[cateId]);
		mav.addObject("sort", sort);
		return mav;
	}
	
	/**
	 * ��Ʒҳ
	 * @param request
	 * @return
	 * @throws Exception
	 * 2013-6-5 ����7:14:33 2013
	 */
	@RequestMapping(value="/goodsList", method = RequestMethod.GET)
	public ModelAndView goodsList(HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("goodslist");
		//�������û�����࣬���ѯ������Ʒ
		String cate = request.getParameter("cate");  
		if(cate == null){
			mav.addObject("goodsList", goodsService.getGoodsVoCountListByCate(1, 12, 0));
		}else{
			mav.addObject("goodsList", goodsService.getGoodsVoCountListByCate(1, 12, Integer.parseInt(cate)));
			mav.addObject("cate", cate);
		}
		return mav;
	}
	
	/**
	 * �ٲ�����ȡ��Ʒ
	 * @param id
	 * @param response
	 * @throws IOException
	 * 2013-6-5 ����7:57:42 2013
	 */
	@RequestMapping("/goodsList/{id}")
	public void goodsList(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String cate = request.getParameter("cate");
		List<GoodsVoCount> list;
		if("".equals(cate) || cate == null){
			list = goodsService.getGoodsVoCountListByCate(1, 12, id-1);
		}else{
			list = goodsService.getGoodsVoCountListByCate(id, 12, Integer.parseInt(cate));
		} 
		StringBuffer htmlStr = new StringBuffer();
		for(GoodsVoCount goodsVoCount:list){
			htmlStr.append("<div class=\"item masonry_brick\">");
			htmlStr.append("	<div class=\"item_t\">");
			htmlStr.append("		<div class=\"img\">");
			htmlStr.append("			<a href=\"product.html?id="+goodsVoCount.getGoods().getGoodsId()+"\"><img width=\"210\" height=\"240\" alt=\""+goodsVoCount.getGoods().getGoodsName()+"\" src=\""+goodsVoCount.getGoods().getGoodsUrl()+"\" /></a>");
			htmlStr.append("			<span class=\"price\">�� "+goodsVoCount.getGoods().getPriceDiscount()+"</span>");
			htmlStr.append("			<div class=\"btns\">");
			htmlStr.append("				<a onclick=\"addCart("+goodsVoCount.getGoods().getGoodsId()+")\" class=\"img_album_btn\">����ͳ�</a>");
			htmlStr.append("			</div>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"title\"><span>"+goodsVoCount.getGoods().getGoodsName()+"</span></div>");
			htmlStr.append("	</div>");
			htmlStr.append("	<div class=\"item_b clearfix\">");
			htmlStr.append("		<div class=\"items_likes fl\">");
			htmlStr.append("			<a class=\"like_btn\"  onclick=\"addLike("+goodsVoCount.getGoods().getGoodsId()+")\"></a>");
			htmlStr.append("			<em class=\"bold\" id=\"like"+goodsVoCount.getGoods().getGoodsId()+"\">"+goodsVoCount.getLike()+"</em>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"items_comment fr\"><a onclick=\"buyNow("+goodsVoCount.getGoods().getGoodsId()+")\">��������</a><em class=\"bold\">("+goodsVoCount.getSale()+")</em></div>");
			htmlStr.append("	</div>");
			htmlStr.append("</div>");
		}
		out.print(htmlStr);
	}
	
	@RequestMapping(value = "/addLikeGoods", method = RequestMethod.GET)
	public @ResponseBody boolean addLikeShop(HttpServletRequest request){
		int goodsId = Integer.parseInt(request.getParameter("id"));
		User user = getSessionUser(request);
		int userId = user == null ? 0: getSessionUser(request).getUserId();
		return goodsService.addLikeGoods(userId, goodsId);
	}
	
	/**
	 * ���ajax��ȡ��Ʒ
	 * @param request
	 * @return
	 * @throws Exception
	 * 2013-5-24 ����3:26:23 2013
	 */
	@RequestMapping("/page/{id}")
	public void getProductPaginationByTagAjax(@PathVariable long id, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		List<Message> list = messageService.getMessagePageByPageno(id+"").getList();
		StringBuffer htmlStr = new StringBuffer();
		for(Message message:list){
			htmlStr.append("<div class=\"item masonry_brick\">");
			htmlStr.append("	<div class=\"item_t\">");
			htmlStr.append("		<div class=\"img\">");
			htmlStr.append("			<a href=\"product.html?id="+message.getToId()+"\"><img width=\"210\" height=\"240\" alt=\""+message.getContentText()+"\" src=\""+message.getPictureUrl()+"\" /></a>");
			htmlStr.append("			<span class=\"price\">��195.00</span>");
			htmlStr.append("			<div class=\"btns\">");
			htmlStr.append("				<a onclick=\"addCart("+message.getToId()+")\" class=\"img_album_btn\">����ͳ�</a>");
			htmlStr.append("			</div>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"title\"><span>"+message.getContentText()+"</span></div>");
			htmlStr.append("	</div>");
			htmlStr.append("	<div class=\"item_b clearfix\">");
			htmlStr.append("		<div class=\"items_likes fl\">");
			htmlStr.append("			<a class=\"like_btn\"></a>");
			htmlStr.append("			<em class=\"bold\">916</em>");
			htmlStr.append("		</div>");
			htmlStr.append("		<div class=\"items_comment fr\"><a onclick=\"buyNow("+message.getToId()+")\">��������</a><em class=\"bold\">(0)</em></div>");
			htmlStr.append("	</div>");
			htmlStr.append("</div>");
		}
		out.print(htmlStr);
	}
	
	@RequestMapping("/dianpu")
	public String toDianPu(){
		return "dianpu";
	}
	
	@RequestMapping("/getLatestShopsAjax")
	public @ResponseBody List<Shop> getLatestShopsAjax(){
		return shopService.getNewShopList();
	}
	
	@RequestMapping("/getLatestTagAjax")
	public @ResponseBody List<Tag> getLatestTagAjax(){
		return tagService.getNewTagList();
	}
	
//	@RequestMapping("/getMostFavoriteProductAjax")
//	public @ResponseBody List<GoodsVo> getMostFavoriteProductAjax(){
//		// TODO ����ʵ�ֹ��
//		List<GoodsVo> goodsList = new ArrayList<GoodsVo>();
//		goodsList.add(goodsService.getGoodsVoByGoodsId(43+""));
//		goodsList.add(goodsService.getGoodsVoByGoodsId(42+""));
//		return goodsList;
//	}
	
	/**
	 * �����
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stroll", method = RequestMethod.GET)
	public ModelAndView	stroll(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("stroll", messageService.getMessagePageByPageno("1").getList());
		return mav;
	}
	
	/**
	 * �����
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/json_stroll", method = RequestMethod.GET)
	public @ResponseBody List<Message> json_stroll(HttpServletRequest request){
		return messageService.getMessagePageByPageno(request.getParameter("p")).getList();
	}
	
	/**
	 * ������
	 * @param request
	 * @return
	 * @throws WeiboException
	 * Green Lei
	 * 2012-12-8 ����2:00:05 2012
	 * @throws IOException 
	 */
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public void sendMessage(HttpServletRequest request,HttpServletResponse response) throws WeiboException, IOException {
		request.setCharacterEncoding("utf-8");
		String goodsId = request.getParameter("goodsId");
		String goodsCount = request.getParameter("goodsCount");
		response.setContentType("text/html;charset=UTF-8");
		int goodsIdInt = 0;
		if(goodsId != null){
			goodsIdInt = Integer.parseInt(goodsId);
		}
		Goods goods = goodsService.getGoodsByGoodsId(goodsIdInt);
		Shop shop = shopService.getShopByShopId(goods.getShopId());
		User user = getSessionUser(request);
		StringBuffer message = new StringBuffer();
		message.append(user.getLocation())
				.append("("+user.getPhone()+"):")
				.append(goods.getGoodsName()+"/"+goodsCount+"��");
		if(user == null){
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('��Ǹ,����δ��½,������ѡ�ε�ѧ�ŵ�½��!'); location='index.html';</script>");
		}else if(user.getLocation() == null){
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('��Ǹ,����δ��д�Ͳ͵�ַ,������һ�°�!'); location='index.html';</script>");
		}else if(user.getPhone() == null){
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('��Ǹ,����δ��д��ϵ�绰,������һ�°�!'); location='index.html';</script>");
		}else if(feixinService.sendMessage(shop.getFeixin()+"", message.toString())){
			Message messageObj = new Message();
			messageObj.setContentText(user.getUserName()+"����"+goods.getGoodsName());
			messageObj.setPictureUrl(goods.getGoodsUrl());
			messageObj.setFromId(user.getUserId());
			messageObj.setToId(goods.getGoodsId());
			messageObj.setType(Cons.ADD_MESSAGE_MESSAGE);
			messageService.addMessage(messageObj);
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('���ŷ��ͳɹ�,һ������͵�!!'); location='index.html';</script>");
		}else{
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('δ֪����,���ŷ���ʧ��,�����¶���!!'); location='index.html';</script>");
		}
	}
	
	/**
	 * δ��¼�µ�
	 * @param request
	 * @param response
	 * @return
	 * @throws WeiboException
	 * @throws IOException
	 * 2013-4-30 ����9:51:05 2013
	 */
	@RequestMapping(value = "/orderNoLogin", method = RequestMethod.POST)
	public @ResponseBody boolean orderNoLogin(HttpServletRequest request, HttpServletResponse response) throws WeiboException, IOException {
		String location = request.getParameter("location");
		String phone = request.getParameter("phone");
		//����Ϣ����cookie
		userService.addBuyerInfoToCookie(phone, location, response);
		
		List<Goods> goodsList = CartManager.getGoodsList(request);
		
		StringBuffer message = new StringBuffer("["+phone+","+location+"]   ");
		//TODO �����ڷ��Ͷ���֮ǰ�Ͱѽ��׼�¼�������ݿ���,Ӧ���ڷ��Ͷ���֮����
		for(Goods goods:goodsList){
			message.append("["+goods.getGoodsName()+goods.getCount()+"��-"+shopService.getShopByShopId(goods.getShopId()).getShopName()+"]");
			//δ��¼ʹ��0����userId
			goodsService.addSale(goods.getShopId(), 0, goods.getGoodsId(), goods.getCount());
		}
		message.append("�����Ͱ�ף��������¡��");
		//��չ��ﳵ
		CartManager.clearGoods(request);
		return feixinService.sendMessage("13898176737", message.toString());
	}
	
	/**
	 * ��¼���µ�
	 * @param request
	 * @param response
	 * @return
	 * @throws WeiboException
	 * @throws IOException
	 * 2013-4-30 ����9:51:54 2013
	 */
	@RequestMapping(value = "/orderLogin", method = RequestMethod.POST)
	public @ResponseBody int orderLogin(HttpServletRequest request, HttpServletResponse response) throws WeiboException, IOException {
		String phone = request.getParameter("UserMail");
		String userPwd = request.getParameter("UserPwd");
		User userAim = userService.getUserByPhone(phone);
		if(userAim != null && userAim.getUserPwd().equals(userPwd)){
			setSessionUser(request, userAim);
			if("".equals(userAim.getPhone()) || "".equals(userAim.getLocation())){
				return 0;   //�û��ĵ绰���ߵ�ַΪ��д
			}
			
			//��ӵ��Զ���¼
			AutoLogin autoLogin = new AutoLogin();
			autoLogin.setSessionId(request.getSession().getId());
			autoLogin.setUserId(userAim.getUserId()+"");
			userService.addAutoLogin(autoLogin, response); 
			
			//�µ�
			List<Goods> goodsList = CartManager.getGoodsList(request);
			
			StringBuffer message = new StringBuffer("["+userAim.getPhone()+","+userAim.getLocation()+"]   ");
			for(Goods goods:goodsList){
				message.append("["+goods.getGoodsName()+goods.getCount()+"��-"+shopService.getShopByShopId(goods.getShopId()).getShopName()+"]");
				goodsService.addSale(goods.getShopId(), userAim.getUserId(), goods.getGoodsId(), goods.getCount());
			}
			message.append("�����Ͱ�ף��������¡��");
			//��չ��ﳵ
			CartManager.clearGoods(request);
			feixinService.sendMessage("13898176737", message.toString());
			return 1;        //�µ��ɹ�
		}else if(userAim != null && !userAim.getUserPwd().equals(userPwd)){
			ModelAndView model = new ModelAndView("redirect:myhome.html");
			model.addObject("message", "�������!");
			return 2;
		}else if(userAim == null){
			ModelAndView model = new ModelAndView("redirect:myhome.html");
			model.addObject("message", "�˺Ų�����!");
			return 3;
		}else{
			ModelAndView model = new ModelAndView("redirect:myhome.html");
			model.addObject("message", "��¼ʧ��,δ֪����!");
			return -1;
		}
	}
	
	/**
	 * �ѵ�¼���µ�
	 * @param request
	 * @return
	 * @throws WeiboException
	 * @throws IOException
	 * 2013-4-30 ����10:46:43 2013
	 */
	@RequestMapping(value = "/orderLogined", method = RequestMethod.GET)
	public @ResponseBody boolean orderLogined(HttpServletRequest request) throws WeiboException, IOException {
		User user = getSessionUser(request);
		String location = user.getLocation();
		String phone = user.getPhone();
		
		List<Goods> goodsList = CartManager.getGoodsList(request);
		
		StringBuffer message = new StringBuffer("["+phone+","+location+"]");
		for(Goods goods:goodsList){
			message.append("["+goods.getGoodsName()+goods.getCount()+"��-"+shopService.getShopByShopId(goods.getShopId()).getShopName()+"]");
			goodsService.addSale(goods.getShopId(), user.getUserId(), goods.getGoodsId(), goods.getCount());
			
			//��һ��������
			Message newMessage = new Message();
			newMessage.setContentText(":������{"+goods.getGoodsName()+"]");
			newMessage.setFromId(user.getUserId());
			newMessage.setPictureUrl(goods.getGoodsUrl());
			newMessage.setToId(goods.getGoodsId());
			newMessage.setType(Cons.ADD_GOODS_MESSAGE);
			messageService.addMessage(newMessage);
		}
		message.append("�����Ͱ�ף��������¡��");
		//��չ��ﳵ
		CartManager.clearGoods(request);
//		//��qq״̬
//		UserThird userThird= userService.getUserThirdByUserIdAndLoginType(getSessionUser(request).getUserId()+"", 3);	
//		if(userThird != null){
//			String openid = userThird.getThirdUserId();
//	        String access_token = userThird.getAccessToken();
//			qqService.updateStatus(access_token, openid, "������ʡ��,���гԺõ�,�������Ͱ�.", "http://www.xn--4qrz40kyoi.com/product.html?id="+goodsList.get(0).getGoodsId(), "�ո���#���Ͱ�#����һ�ݷ�,���ע���˵����������ܶ���,ͦ�����,�͵Ļ���,�����Ժ����϶��Ͱ�", 
//					goodsList.get(0).getGoodsName(),"http://www.xn--4qrz40kyoi.com/product.html?id="+goodsList.get(0).getGoodsId(), "http://www.xn--4qrz40kyoi.com/"+goodsList.get(0).getGoodsUrl());
//		}

		return feixinService.sendMessage("13898176737", message.toString());
	}
	
	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	public ModelAndView addComment(@ModelAttribute("commentForm") Comment comment,
			HttpServletRequest request) {
		ModelAndView mav = null;
		User user = getSessionUser(request);
		Goods goods = goodsService.getGoodsByGoodsId(comment.getGoodsId());
		if(comment.getCommenterId() == 0){    //δ��½,��ת����½ҳ��
			mav = new ModelAndView("mprompt");
			mav.addObject("prompt", ConfigReader.getValue("message_comment_tip1"));
			return mav;
		}else if(comment.getContent() == null || "".equals(comment.getContent())){    //����Ϊ��
			mav = new ModelAndView("msingle");
			mav.addObject("Goods", goodsService.getGoodsByGoodsId(comment.getGoodsId()));
			mav.addObject("comments", commentsService.getCommentsByGoodsId(comment.getGoodsId()));
			mav.addObject("message", ConfigReader.getValue("message_comment_tip2"));
			return mav;
		}else{
			commentsService.addComment(comment);
			Message message = new Message();
			message.setContentText(user.getUserName()+":"+comment.getContent());
			message.setFromId(comment.getCommenterId());
			message.setPictureUrl(goods.getGoodsUrl());
			message.setToId(comment.getGoodsId());
			message.setType(Cons.ADD_COMMENT_MESSAGE);
			messageService.addMessage(message);
			
			return new ModelAndView("redirect:product.html?productId="+comment.getGoodsId());
		}
	}
	
	/**
	 * ��Ʒ���а�
	 * @return
	 */
	@RequestMapping(value = "/goodsBillboard", method = RequestMethod.GET)
	public ModelAndView	goodsBillboard(){
		ModelAndView mav = new ModelAndView("side_nav_goods");
		mav.addObject("newgoods", goodsService.getNewGoodsList(5));
		return mav;
	}
	
	/**
	 * �̼����а�
	 * @return
	 */
	@RequestMapping(value = "/shopBillboard", method = RequestMethod.GET)
	public ModelAndView	shopBillboard(){
		ModelAndView mav = new ModelAndView("side_nav_shop");
		mav.addObject("newshop", shopService.getNewShopList(5));
		return mav;
	}
	
	/**
	 * ��Ʒ����չʾ
	 * @return
	 */
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public ModelAndView	category(){
		ModelAndView mav = new ModelAndView("goodscate");
		String[] pcate = ConfigReader.getValue("product_category").split(",");
		for(int n =0; n < pcate.length; n++){
			mav.addObject("goodslist"+n, goodsService.getGoodsListByCate(n+""));
		}
		return mav;
	}
	
	/**
	 * ת�����ﳵ
	 * @return
	 * 2013-4-27 ����1:05:46 2013
	 */
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public ModelAndView	cart(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("cart","GoodsList",CartManager.getGoodsList(request));
		return mav;
	}
	
	/**
	 * ����
	 * @return
	 * 2013-4-27 ����1:45:17 2013
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public ModelAndView	checkout(HttpServletRequest request){
		 Cookie[] cookies = request.getCookies();
		 String buyerLocation = "";
		 String buyerPhone = "";
	     if(cookies != null){
	    	 buyerLocation = CookieManager.getCookieValue(cookies, Cons.BUYER_LOCATION);
	    	 buyerPhone = CookieManager.getCookieValue(cookies, Cons.BUYER_PHONE);
	     }
		ModelAndView mav = new ModelAndView("checkout");
		mav.addObject("location", buyerLocation);
		mav.addObject("phone", buyerPhone);
		mav.addObject("user", getSessionUser(request));
		return mav;
	}
	
	/**
	 * ���ﳵ�ύ������
	 * @param request
	 * @return
	 * 2013-5-6 ����5:52:51 2013
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String	checkoutPost(HttpServletRequest request){
		//�����session��ԭ�е���Ʒ
		CartManager.clearGoods(request);
		String[] goodsId = request.getParameterValues("GoodsId");
		String[] goodsCount = request.getParameterValues("GoodsCount");
		Goods goods = null;
		for(int n = 0; n < goodsId.length; n++){
			goods = goodsService.getGoodsByGoodsId(Integer.parseInt(goodsId[n]));
			goods.setCount(Integer.parseInt(goodsCount[n]));
			CartManager.addGoods(goods, request);
		}
		return "redirect:checkout.html";
	}
	
	/**
	 * �����ﳵ����
	 * @param request
	 * @param response
	 * @return
	 * @throws WeiboException
	 * @throws IOException
	 * 2013-4-29 ����12:32:30 2013
	 */
	@RequestMapping(value = "/addCart", method = RequestMethod.GET)
	public @ResponseBody boolean addCart(HttpServletRequest request) {
		int goodsId = Integer.parseInt(request.getParameter("id"));
		Goods goods = goodsService.getGoodsByGoodsId(goodsId);
		if(request.getParameter("count") != null){
			goods.setCount(Integer.parseInt(request.getParameter("count")));
		}else{
			goods.setCount(1);
		}
		boolean flag = false;
		try{
			CartManager.addGoods(goods, request);
			flag = true;
		}catch(Exception e){
			
		}
		return flag;
	}
	
	/**
	 * ���ﳵ��ɾ����Ʒ
	 * @param request
	 * @return
	 * 2013-4-29 ����2:11:31 2013
	 */
	@RequestMapping(value = "/delCart", method = RequestMethod.GET)
	public String delCart(HttpServletRequest request) {
		int goodsId = Integer.parseInt(request.getParameter("id"));
		Goods goods = goodsService.getGoodsByGoodsId(goodsId);
		CartManager.delGoods(goods, request);
		return "redirect:cart.html";
	}
	
	/**
	 * ��ѯ��Ʒ���̼�
	 * @param request
	 * @return
	 * 2013-5-6 ����7:46:58 2013
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request) {
		String keyword = request.getParameter("keyword");
		ModelAndView mav = new ModelAndView("search");
		mav.addObject("goodslist", goodsService.getGoodsListByQuery(keyword));
		mav.addObject("shoplist", goodsService.getShopListByQuery(keyword));
		return mav;
	}
	
	/**
	 * �����̳�
	 * @param request
	 * @return
	 * 2013-5-10 ����3:48:16 2013
	 */
	@RequestMapping(value = "/credits", method = RequestMethod.GET)
	public ModelAndView credits(HttpServletRequest request) {
		return new ModelAndView("credits");
	}
	
	/**
	 * ��ʱ����
	 * @param request
	 * @return
	 * 2013-5-10 ����3:49:08 2013
	 */
	@RequestMapping(value = "/snapUp", method = RequestMethod.GET)
	public ModelAndView snapUp(HttpServletRequest request) {
		return new ModelAndView("snap");
	}
	
	/**
	 * ����֧��
	 * @param request
	 * @return
	 * 2013-5-23 ����11:25:50 2013
	 */
	
	@RequestMapping(value = "/aliPay", method = RequestMethod.GET)
	public ModelAndView aliPay(HttpServletRequest request) {
		User user = getSessionUser(request);
		//������������������
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "trade_create_by_buyer");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", "1");
		sParaTemp.put("notify_url", "http://www.xn--4qrz40kyoi.com/alipay_notify.html");
		sParaTemp.put("return_url", "http://www.xn--4qrz40kyoi.com/alipay_return.html");
		sParaTemp.put("seller_email", "13898176737");
		sParaTemp.put("out_trade_no", CustomDate.getOrderNum());
		sParaTemp.put("subject", "���Զ��ͰɵĶ���");
		sParaTemp.put("price", Double.toString(CartManager.getTotalPrice(request)));
		sParaTemp.put("quantity", "1");
		sParaTemp.put("logistics_fee", "0.00");
		sParaTemp.put("logistics_type", "EXPRESS");
		sParaTemp.put("logistics_payment", "SELLER_PAY");
		sParaTemp.put("body", "���Զ��ͰɵĶ���");
		sParaTemp.put("show_url", "http://www.xn--4qrz40kyoi.com/");
		sParaTemp.put("receive_name", user == null?"δ��¼":user.getUserName());
		sParaTemp.put("receive_address", user == null?"δ��¼":user.getLocation());
		sParaTemp.put("receive_zip", "110000");
		sParaTemp.put("receive_phone", user == null?"δ��¼":user.getPhone());
		sParaTemp.put("receive_mobile", user == null?"δ��¼":user.getPhone());		
		System.out.println( AlipaySubmit.buildRequest(sParaTemp,"post","ȷ��"));
		return new ModelAndView("alipay", "sHtmlText",  AlipaySubmit.buildRequest(sParaTemp,"post","ȷ��"));
	}

	@RequestMapping(value = "/alipay_notify")
	public void alipay_notify(HttpServletRequest request, HttpServletResponse response) throws WeiboException, IOException {
		PrintWriter out = response.getWriter();

		//��ȡ֧����POST����������Ϣ
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת��
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		//��ȡ֧������֪ͨ���ز������ɲο������ĵ���ҳ����תͬ��֪ͨ�����б�(���½����ο�)//
		//�̻�������

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//֧�������׺�

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//����״̬
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//��ȡ֧������֪ͨ���ز������ɲο������ĵ���ҳ����תͬ��֪ͨ�����б�(���Ͻ����ο�)//

		if(AlipayNotify.verify(params)){//��֤�ɹ�
			//////////////////////////////////////////////////////////////////////////////////////////
			//������������̻���ҵ���߼��������

			//�������������ҵ���߼�����д�������´�������ο�������
			
			if(trade_status.equals("WAIT_BUYER_PAY")){
				//���жϱ�ʾ�������֧�������׹����в����˽��׼�¼����û�и���
				
					//�жϸñʶ����Ƿ����̻���վ���Ѿ���������
						//���û�������������ݶ����ţ�out_trade_no�����̻���վ�Ķ���ϵͳ�в鵽�ñʶ�������ϸ����ִ���̻���ҵ�����
						//���������������ִ���̻���ҵ�����
					
					out.println("success");	//�벻Ҫ�޸Ļ�ɾ��
				} else if(trade_status.equals("WAIT_SELLER_SEND_GOODS")){
					
					out.println("success");	//�벻Ҫ�޸Ļ�ɾ��
				} else if(trade_status.equals("WAIT_BUYER_CONFIRM_GOODS")){
				//���жϱ�ʾ�����Ѿ����˻�������һ�û����ȷ���ջ��Ĳ���
				
					//�жϸñʶ����Ƿ����̻���վ���Ѿ���������
						//���û�������������ݶ����ţ�out_trade_no�����̻���վ�Ķ���ϵͳ�в鵽�ñʶ�������ϸ����ִ���̻���ҵ�����
						//���������������ִ���̻���ҵ�����
					
					out.println("success");	//�벻Ҫ�޸Ļ�ɾ��
				} else if(trade_status.equals("TRADE_FINISHED")){
				//���жϱ�ʾ����Ѿ�ȷ���ջ�����ʽ������
				
					//�жϸñʶ����Ƿ����̻���վ���Ѿ���������
						//���û�������������ݶ����ţ�out_trade_no�����̻���վ�Ķ���ϵͳ�в鵽�ñʶ�������ϸ����ִ���̻���ҵ�����
						//���������������ִ���̻���ҵ�����
					
					out.println("success");	//�벻Ҫ�޸Ļ�ɾ��
				}
				else {
					out.println("success");	//�벻Ҫ�޸Ļ�ɾ��
				}

			//�������������ҵ���߼�����д�������ϴ�������ο�������

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//��֤ʧ��
			out.println("fail");
		}		
		
	}
	
	@RequestMapping(value = "/alipay_return")
	public ModelAndView alipay_return(HttpServletRequest request) throws UnsupportedEncodingException, WeiboException {
		ModelAndView mav = new ModelAndView("alipay_return");
		//��ȡ֧����GET����������Ϣ
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת��
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		//��ȡ֧������֪ͨ���ز������ɲο������ĵ���ҳ����תͬ��֪ͨ�����б�(���½����ο�)//
		//�̻�������

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//֧�������׺�

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//����״̬
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//��ȡ֧������֪ͨ���ز������ɲο������ĵ���ҳ����תͬ��֪ͨ�����б�(���Ͻ����ο�)//
		
		//����ó�֪ͨ��֤���
		boolean verify_result = AlipayNotify.verify(params);
		
		if(verify_result){//��֤�ɹ�
			//////////////////////////////////////////////////////////////////////////////////////////
			//������������̻���ҵ���߼��������
			//�������������ҵ���߼�����д�������´�������ο�������
			
			if(trade_status.equals("WAIT_SELLER_SEND_GOODS")){
				//�жϸñʶ����Ƿ����̻���վ���Ѿ���������
					//���û�������������ݶ����ţ�out_trade_no�����̻���վ�Ķ���ϵͳ�в鵽�ñʶ�������ϸ����ִ���̻���ҵ�����
					//���������������ִ���̻���ҵ�����
			}
		
			if(trade_status.equals("TRADE_FINISHED")){
				//�жϸñʶ����Ƿ����̻���վ���Ѿ���������
					//���û�������������ݶ����ţ�out_trade_no�����̻���վ�Ķ���ϵͳ�в鵽�ñʶ�������ϸ����ִ���̻���ҵ�����
					//���������������ִ���̻���ҵ�����
			}
			
			//��ҳ�����ҳ�������༭
			mav.addObject("message", "�͹����ã��Ѿ��ɹ�Ϊ���µ�����������ʳ�����϶��;�����ô���㣡");
			//�������������ҵ���߼�����д�������ϴ�������ο�������

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
			mav.addObject("message", "�͹ٱ�Ǹ���µ�ʧ�ܣ�����ϵ�ͷ���");
		}		
		return mav;
	}
}
