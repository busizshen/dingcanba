package com.dingcan.util;

import java.util.List;

/**
 * ��װ��ҳ��Ϣ�Ͳ���
 * @author Degree
 * 2012-8-12  ����10:29:38
 * @param <E>
 */
public class PageModel<E> {

	//�����
	private List<E> list;
	
	//��ѯ��¼��
	private int totalRecords;
	
	//ÿҳ����������
	private int pageSize;
	
	//�ڼ�ҳ
	private int pageNo;
	
	//����ҳ
	private int totalPage;
	
	//��ҳ
	private int topPageNo;
	
	//��һҳ
	private int nextPageNo;
	
	//��һҳ
	private int previousPageNo;
	
	//ĩҳ
	private int lastPageNo;
	
	//��ѯ�Ĺؼ���
	private String queryString;
	
	/**
	 * ��ҳ��
	 * @return
	 */
	public int getTotalPages() {
		return (totalRecords + pageSize - 1) / pageSize;
	}
	
	/**
	 * ȡ����ҳ
	 * @return
	 */
	public int getTopPageNo() {
		return 1;
	}
	
	/**
	 * ��һҳ
	 * @return
	 */
	public int getPreviousPageNo() {
		if (pageNo <= 1) {
			return 1;
		}
		return pageNo - 1;
	}
	
	/**
	 * ��һҳ
	 * @return
	 */
	public int getNextPageNo() {
		if (pageNo >= getBottomPageNo()) {
			return getBottomPageNo();
		}
		return pageNo + 1;	
	}
	
	/**
	 * ȡ��βҳ
	 * @return
	 */
	public int getBottomPageNo() {
		return getTotalPages();
	}
	
	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	//�������ܼ�¼ʱ,�ѷ�ҳͬʱ������
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		
		topPageNo = getTopPageNo();
		nextPageNo = getNextPageNo();
		previousPageNo = getPreviousPageNo();
		lastPageNo = getBottomPageNo();
		totalPage = getTotalPages();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getLastPageNo() {
		return lastPageNo;
	}

	public void setLastPageNo(int lastPageNo) {
		this.lastPageNo = lastPageNo;
	}

	public void setTopPageNo(int topPageNo) {
		this.topPageNo = topPageNo;
	}

	public void setNextPageNo(int nextPageNo) {
		this.nextPageNo = nextPageNo;
	}

	public void setPreviousPageNo(int previousPageNo) {
		this.previousPageNo = previousPageNo;
	}
	
}