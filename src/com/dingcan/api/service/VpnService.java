package com.dingcan.api.service;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;


import org.springframework.stereotype.Service;

import com.dingcan.user.model.User;
import com.dingcan.util.ResizeImage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

@Service
public class VpnService{

	public User getUserInfo(String userName, String userPasw, String savePath) throws Exception{
		final WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(true);  

        // Get the first page
        final HtmlPage page1 = webClient.getPage("http://210.47.163.27:9004/");

        // Get the form that we are dealing with and within that form, 
        // find the submit button and the field that we want to change.
        final HtmlForm form = page1.getFormByName("loginForm");

        final HtmlElement button = page1.getElementById("btnSure");
        final HtmlTextInput textField = form.getInputByName("zjh");
        final HtmlPasswordInput passWord = form.getInputByName("mm");
//        final HtmlCheckBoxInput checkBox = form.getInputByName("fruit");
//        // Change the value of the text field
        textField.setValueAttribute(userName);
        passWord.setValueAttribute(userPasw);
//        checkBox.setDefaultValue("juzi");
//        // Now submit the form by clicking the button and get back the second page.
        final HtmlPage page2 = button.click();
        submittingForm(page2);//�ύ��,��½ϵͳ,������
        User user = new User();
        final HtmlPage pageXueji =  webClient.getPage("http://210.47.163.27:9004/xjInfoAction.do?oper=xjxx");
        String intro = "ѧԺ:"+pageXueji.getElementsByTagName("td").get(70).asText()+"</br>"+
        				"�༶:"+pageXueji.getElementsByTagName("td").get(78).asText()+"</br>"+
        				"��ѧ:"+pageXueji.getElementsByTagName("td").get(52).asText()+"";
        user.setIntro(intro);
        final HtmlPage packageListPage = webClient.getPage("http://210.47.163.27:9004/userInfo.jsp");
        final HtmlForm infoForm = packageListPage.getFormByName("frm");
        String imgName = packageListPage.getElementsByTagName("b").get(0).asText()+".jpg";
        user.setLocation(infoForm.getInputByName("txdz").getDefaultValue());
        user.setPhone(infoForm.getInputByName("dh").getDefaultValue());
        user.setUserMail(infoForm.getInputByName("email").getDefaultValue());
        user.setUserName(packageListPage.getElementsByTagName("b").get(0).asText());
        user.setPhoto("userimg"+savePath.split("userimg")[1]+imgName);
    	try{
           DataInputStream in = new DataInputStream(webClient.getPage("http://210.47.163.27:9004/xjInfoAction.do?oper=img").getWebResponse().getContentAsStream());
           /*�˴�Ҳ����BufferedInputStream��BufferedOutputStream*/
           DataOutputStream out = new DataOutputStream(new FileOutputStream(savePath+imgName));
           /*������savePath��������ȡ��ͼƬ�Ĵ洢�ڱ��ص�ַ��ֵ��out�������ָ���ĵ�ַ*/
           byte[] buffer = new byte[4096];
           int count = 0;
           while ((count = in.read(buffer)) > 0)/*�����������ֽڵ���ʽ��ȡ��д��buffer��*/
           {
               out.write(buffer, 0, count);
           }
           
           //����ͼƬ
           ResizeImage r = new ResizeImage(); 
           BufferedImage bi = javax.imageio.ImageIO.read(new File(savePath+imgName));
           String outputFolder = savePath+"/";
           r.writeHighQualityByName(r.changeImage(bi,200,260),outputFolder, "200w"+imgName);
           
           out.close();/*��������Ϊ�ر�����������Լ�������Դ�Ĺ̶���ʽ*/
           in.close();
       }catch (Exception e) {
            throw new Exception("�ļ�����ʧ��!");
       }finally{
    	   webClient.closeAllWindows();
       }
       return user;
	}
	
	
	/**
	 * ����
	 * @param user
	 * @param pasw
	 * @throws Exception
	 * Green Lei
	 * 2012-12-12 ����7:54:58 2012
	 */
	public void submittingForm(final HtmlPage page2) throws Exception {
		HtmlPage packageListPage = null;
		try{
			packageListPage = (HtmlPage) page2.getFrameByName("topFrame").getEnclosedPage();
		}catch(Exception e){
			e.printStackTrace();
		}
	  
//      System.out.println("helle123");
      //������̵�ҳ��
      final HtmlPage page3 = packageListPage.getAnchors().get(4).click();
      
      //�����ҳ����
      //System.out.println(page3.getWebResponse().getContentAsString());
      //�������
      //System.out.println(page3.getTitleText());
//     final HtmlTable table = page3.getHtmlElementById("user");
//      //����б�ҳ���е�img��ǩ
//      int n = 0;
//      HtmlPage page4;
//      for (final HtmlTableRow row : table.getRows()) {
//          System.out.println("Found row");           
//          for (final HtmlTableCell cell : row.getCells()) {
//              //System.out.println("   Found cell: " + cell.asText());
//          	//System.out.println("   Found cell: " + row.getCell(3).asText());
//          	if("��".equals(row.getCell(3).asText())){
//          		System.out.println("������");
//          		System.out.println(page3.getTitleText());
//          		//page4 = page3.getElementsByTagName("img").get(n).click();
//          		System.out.println(page3.getElementsByTagName("img").get(n));
//          		n++;
//          	}
//          }
//          
//          
//      }
      HtmlSelect htmlSelect = page3.getElementByName("pageSize");

      HtmlOption htmlOption = htmlSelect.getOption(7);
      final HtmlPage pagePiao = (HtmlPage) htmlSelect.setSelectedAttribute(htmlOption, true);;
      
      final List<HtmlImage> imgList = (List<HtmlImage>) pagePiao.getByXPath("//img[@title='����']");
      HtmlPage htmlPage4;
//      System.out.println("��Ҫ�����ʾ�!");
      for(HtmlImage htmlImage:imgList){
//      	System.out.println("����forѭ��!");
      	htmlPage4 = (HtmlPage) htmlImage.click();
//      	System.out.println("123");
      	
	        final List<HtmlRadioButtonInput> radioList = (List<HtmlRadioButtonInput>) htmlPage4.getByXPath("//input[@type='radio']");
	        int radioNum = 5;
	        for(Iterator iteratorRadio = radioList.iterator(); iteratorRadio.hasNext();radioNum++){
	        	HtmlRadioButtonInput radio = (HtmlRadioButtonInput) iteratorRadio.next();
//	        	System.out.println("��ʼ����");
	        	if(radioNum==5){
		        	radio.setChecked(true);	  
//		        	System.out.println(radioNum);
		        	radioNum = 0;
//		        	System.out.println("����һ��");
//		        	System.out.println(radio.getValueAttribute());
		        	
//		        	return;
	        	}
	        }
	        final HtmlTextArea pingjiaoText = htmlPage4.getElementByName("zgpj");
	        pingjiaoText.setText("��ʦ�Ͽκ�,��������,����̤ʵ,��ϲ��.");
	       
	        htmlPage4.executeJavaScript("window.document.StDaForm.submit()");    }
	}
}
