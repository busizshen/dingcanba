package com.dingcan.user.model;

public class UserThird {

	private int UserId;
	private String ThirdUserId;
	private int LoginType;      //1:���˵�¼,2,���˵�¼,3,qq��¼
	private String AccessToken;  
	private String invalid;     //ʧЧ����
	
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public String getThirdUserId() {
		return ThirdUserId;
	}
	public void setThirdUserId(String thirdUserId) {
		ThirdUserId = thirdUserId;
	}
	public int getLoginType() {
		return LoginType;
	}
	public void setLoginType(int loginType) {
		LoginType = loginType;
	}
	public String getAccessToken() {
		return AccessToken;
	}
	public void setAccessToken(String accessToken) {
		AccessToken = accessToken;
	}
	public String getInvalid() {
		return invalid;
	}
	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}
}
