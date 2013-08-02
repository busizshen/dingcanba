package com.dingcan.message.model;

/**
 * �����Լ�վ���ŵȵ������Ϣ
 * @author Green Lei
 * 2012-12-2 ����9:47:58 2012
 */
public class Message {
	private int MessagesId;
	private int FromId;
	private int ToId;
	private String ContentText;
	private int Type;      //1������һ������,2:����, 3:�����һ����Ʒ,4:�����һ������������
	private String CreateDate;
	private String PictureUrl; //ͷ�������Ʒ����Ƭ
	private int CommentNum;    //����Ϣ�����۵Ĵ���
	private int ShareNum;		//����Ϣ�������������Ĵ���	
	
	public int getMessagesId() {
		return MessagesId;
	}
	public void setMessagesId(int messagesId) {
		MessagesId = messagesId;
	}
	public int getFromId() {
		return FromId;
	}
	public void setFromId(int fromId) {
		FromId = fromId;
	}
	public int getToId() {
		return ToId;
	}
	public void setToId(int toId) {
		ToId = toId;
	}
	public String getContentText() {
		return ContentText;
	}
	public void setContentText(String contentText) {
		ContentText = contentText;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getPictureUrl() {
		return PictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		PictureUrl = pictureUrl;
	}
	public int getCommentNum() {
		return CommentNum;
	}
	public void setCommentNum(int commentNum) {
		CommentNum = commentNum;
	}
	public int getShareNum() {
		return ShareNum;
	}
	public void setShareNum(int shareNum) {
		ShareNum = shareNum;
	}
}
