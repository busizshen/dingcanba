package com.dingcan.tag.model;

public class Tag{
	
	private int TagId;
	private int TagType;   //��ǩ���,Ĭ����0,0:��Ʒ���������ı�ǩ
	private String TagName;
	private String CreateDate;
	
	public int getTagId() {
		return TagId;
	}
	public void setTagId(int tagId) {
		TagId = tagId;
	}
	public int getTagType() {
		return TagType;
	}
	public void setTagType(int tagType) {
		TagType = tagType;
	}
	public String getTagName() {
		return TagName;
	}
	public void setTagName(String tagName) {
		TagName = tagName;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
}
