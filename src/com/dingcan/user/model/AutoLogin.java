package com.dingcan.user.model;

/**
 * �����Զ���¼����Ϣ
 * @author Administrator
 *
 */
public class AutoLogin {
	private int id;
	private String UserId;
	private String SessionId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getSessionId() {
		return SessionId;
	}
	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}
}
