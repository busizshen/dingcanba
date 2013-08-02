package com.dingcan.user.service;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.dingcan.cons.Cons;
import com.dingcan.shop.model.Shop;
import com.dingcan.user.dao.UserDao;
import com.dingcan.user.model.AutoLogin;
import com.dingcan.user.model.Collection;
import com.dingcan.user.model.Friends;
import com.dingcan.user.model.User;
import com.dingcan.user.model.UserThird;
import com.dingcan.util.CustomDate;


@Service
public class UserService {
	ApplicationContext context = 
	 		new ClassPathXmlApplicationContext("Spring-Module.xml");

	UserDao userDao = (UserDao) context.getBean("userDao");
	
	/**
	 * �����û���id
	 * @param user
	 * @return
	 * 2013-4-29 ����10:54:22 2013
	 */
	public int addUser(User user){
		user.setRegisterTime(CustomDate.getStringDate());
		return userDao.addUser(user);
	}
	
	public boolean addThirdUser(UserThird userThird){
		return userDao.addUserThird(userThird)>0;
	}
	
	public UserThird getUserThirdByThirdIdAndLoginType(String thirdUid, int loginType){
		return userDao.getUserThirdByThirdIdAndLoginType(thirdUid, loginType);
	}
	
	/**
	 * 
	 * @param userid
	 * @param loginType
	 * @return
	 * 2013-5-12 ����1:22:29 2013
	 */
	public UserThird getUserThirdByUserIdAndLoginType(String userid, int loginType){
		return userDao.getUserThirdByUserIdAndLoginType(userid, loginType);
	}
	
	/**
	 * ��cookie���͵������,����14��,������һ����¼
	 * @param autoLogin
	 * @param res
	 * @return
	 * Green Lei
	 * 2012-11-29 ����2:20:43 2012
	 */
	public boolean addAutoLogin(AutoLogin autoLogin, HttpServletResponse res){
		Cookie ckUserid, ckSessionid;
		ckUserid = new Cookie(Cons.AUTOLOGIN_USERID, autoLogin.getUserId()); 
		ckUserid.setMaxAge(60 * 60 * 24 * 100);   //����Cookie��Ч��Ϊ100��
		res.addCookie(ckUserid);
		ckSessionid = new Cookie(Cons.SESSIONID, autoLogin.getSessionId());
		ckSessionid.setMaxAge(60 * 60 * 24 * 100);
		res.addCookie(ckSessionid);
		return userDao.addAutoLogin(autoLogin)>0;   // �����ݿ��в�����Ӧ��¼
	}
	
	/**
	 * �ж��Ƿ����autologin�ļ�¼
	 * @param sessionId
	 * @return
	 * Green Lei
	 * 2012-11-29 ����2:19:10 2012
	 */
	public boolean isAutoLoginBySessionId(String usernId, String sessionId){
		AutoLogin autoLogin = userDao.getAutoLoginBySessionId(sessionId);
		boolean flag = false;
		if(autoLogin != null && autoLogin.getUserId().equals(usernId)){
			flag = true;
		}
		return flag;
	}
	
	public User getUserByUserId(String userId){
		return userDao.getUserByUserid(userId);
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * Green Lei
	 * 2012-12-1 ����7:00:55 2012
	 */
	public boolean updateUserPhoneAndLocation(User user){
		return userDao.updateUserPhoneAndLocation(user)>0;
	}
	
	/**
	 * ��ѯ���µ��û�
	 * @param quantity
	 * @return
	 * 2013-4-27 ����9:32:30 2013
	 */
	public List<User> getNewUserList(int quantity){
		return userDao.getNewUserList(quantity);
	}
	
	/**
	 * ��Ӻ���
	 * @param myId
	 * @param friendId
	 * @return
	 * 2013-4-28 ����1:00:51 2013
	 */
	public int addFriend(int myId, int friendId){
		List<Friends> friends = userDao.getMyFriends(myId);
		boolean isFriends = false;
		for(Friends friend:friends){   //�ж��Ƿ�������
			if(friendId == friend.getFriendId()){
				isFriends = true;
			}
		}
		if(!isFriends){
			userDao.addFriends(myId, friendId, 1); 
			return 1;         //��ӳɹ�
		}else{
			return 0;          //�Ѿ�������
		}
	}
	
	public boolean idFriends(int myId, int friendId){
		List<Friends> friends = userDao.getMyFriends(myId);
		boolean isFriends = false;
		for(Friends friend:friends){   //�ж��Ƿ�������
			if(friendId == friend.getFriendId()){
				isFriends = true;
			}
		}
		return isFriends;
	}
	
	/**
	 * �����û������ж��Ƿ�����û�,���ڷ�����
	 * @param userMail
	 * @return
	 * 2013-4-29 ����9:15:44 2013
	 */
	public boolean existedUser(String userphone){
		return userDao.getUserByUserPhone(userphone) != null;
	}
	
	/**
	 * �޸��û��ǳ�,�绰,��ַ
	 * @param user
	 * @return
	 * 2013-4-29 ����10:16:53 2013
	 */
	public boolean updateUserInfo(User user){
		return userDao.updateUserInfo(user)>0;
	}
	
	/**
	 * ����ͷ��
	 * @param user
	 * @return
	 * 2013-5-1 ����10:59:34 2013
	 */
	public boolean updateUserAvatar(User user){
		return userDao.updateUserAvatar(user)>0;
	}
	
	/**
	 * ���������ѯ�û�
	 * @param userMail
	 * @return
	 * 2013-4-29 ����10:58:10 2013
	 */
	public User getUserByMail(String userMail){
		return userDao.getUserByUserMail(userMail);
	}
	
	/**
	 * ���ݵ绰��ѯ�û�
	 * @param phone
	 * @return
	 * 2013-5-10 ����2:07:25 2013
	 */
	public User getUserByPhone(String phone){
		return userDao.getUserByUserPhone(phone);
	}
	
	/**
	 * ������δ��¼,����Ϣ������cookie��
	 * @param phone
	 * @param location
	 * @param res
	 * 2013-4-29 ����3:44:34 2013
	 */
	public void addBuyerInfoToCookie(String phone, String location, HttpServletResponse res){
		Cookie buyerPhone, buyerLocation;
		buyerPhone = new Cookie(Cons.BUYER_PHONE, phone); 
		buyerPhone.setMaxAge(60 * 60 * 24 * 100);   //����Cookie��Ч��Ϊ100��
		res.addCookie(buyerPhone);
		buyerLocation = new Cookie(Cons.BUYER_LOCATION, location);
		buyerLocation.setMaxAge(60 * 60 * 24 * 100);
		res.addCookie(buyerLocation);
	}
	
	/**
	 * ����ղ�
	 * @param userId
	 * @param shopId
	 * @return
	 * 2013-4-29 ����11:20:52 2013
	 */
	public int addCollection(int userId, int shopId){
		List<Collection> collections = userDao.getMyCollections(userId);
		boolean isCollected = false;
		for(Collection collection:collections){   //�ж��Ƿ�������
			if(shopId == collection.getShopId()){
				isCollected = true;
			}
		}
		if(!isCollected){
			Collection collection = new Collection();
			collection.setDate(CustomDate.getStringDate());
			collection.setShopId(shopId);
			collection.setUserId(userId);
			userDao.addCollection(collection); 
			return 1;         //��ӳɹ�
		}else{
			return 0;          //�Ѿ�������
		}
	}
	
	/**
	 * ���ղص��̼�
	 * @param userId
	 * @return
	 * 2013-4-29 ����11:21:58 2013
	 */
	public List<Shop> getCollectedShops(int userId){
		return userDao.getMyCollectedShops(userId);
	}
	
	/**
	 * ȡ���ղص��̼�
	 * @param userId
	 * @param shopId
	 * @return
	 * 2013-4-29 ����11:33:11 2013
	 */
	public boolean cancleCollect(int userId, int shopId){
		return userDao.delCollection(userId, shopId)>0;
	}
	
	/**
	 * ����ҵ�����
	 * @param userId
	 * @return
	 * 2013-4-29 ����11:45:23 2013
	 */
	public List<User> getMyFriends(int userId){
		return userDao.getMyFriendsOfUser(userId);
	}
	
	/**
	 * ɾ������
	 * @param myId
	 * @param friendId
	 * @return
	 * 2013-4-29 ����11:46:06 2013
	 */
	public boolean delFriend(int myId, int friendId){
		return userDao.delFriend(myId, friendId)>0;
	}
}
