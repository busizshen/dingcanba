package com.dingcan.user.model;

/**
 * ����
 * @author Administrator
 *
 */
public class Friends {

	private int id;
	private int myId;
	private int friendId;
	private int state;     //0Ϊ��,1Ϊ��
	public int getMyId() {
		return myId;
	}
	public void setMyId(int myId) {
		this.myId = myId;
	}
	public int getFriendId() {
		return friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
