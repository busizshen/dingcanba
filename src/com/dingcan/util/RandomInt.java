package com.dingcan.util;

public class RandomInt {

	/**
	 * ��ô�from��to���漴����
	 * @param from
	 * @param to
	 * @return
	 * 2013-5-1 ����1:53:12 2013
	 */
	public static int getRandomInt(int from, int to){
	    int i = (int) (Math.random()* to+from);//����0-10��˫���������   
	    return i;
	}
	
	public static void main(String args[]){
		System.out.println("default/avatar/w200_"+getRandomInt(50, 50)+".jpg");
//		System.out.println("default/logo/w200_"+getRandomInt(0, 10)+".jpg");
	}
}
