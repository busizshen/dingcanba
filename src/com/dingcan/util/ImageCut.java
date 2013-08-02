package com.dingcan.util;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import javax.imageio.ImageIO;

public class ImageCut {
	
	/** 
     * ͼ���и�ģ�     * 
     * @param srcImageFile            Դͼ���ַ
     * @param dirImageFile            ��ͼ���ַ
     * @param x                       Ŀ����Ƭ���x����
     * @param y                      Ŀ����Ƭ���y����
     * @param destWidth              Ŀ����Ƭ���
     * @param destHeight             Ŀ����Ƭ�߶�
     */
    public static void abscut(String srcImageFile,String dirImageFile, int x, int y, int destWidth,
            int destHeight) {
        try {
            Image img;
            ImageFilter cropFilter;
            // ��ȡԴͼ��
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getWidth(); // Դͼ���
            int srcHeight = bi.getHeight(); // Դͼ�߶�          
            if (srcWidth >= destWidth && srcHeight >= destHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // �Ľ����뷨:�Ƿ���ö��̼߳ӿ��и��ٶ�
                // �ĸ������ֱ�Ϊͼ���������Ϳ��
                // ��: CropImageFilter(int x,int y,int width,int height)
                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
                img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(destWidth, destHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null); // ������С���ͼ
                g.dispose();
                // ���Ϊ�ļ�
                ImageIO.write(tag, "JPEG", new File(dirImageFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
	/**
	 * ����ͼ��
	 * 
	 * @param srcImageFile       Դͼ���ļ���ַ
	 * @param result             ���ź��ͼ���ַ
	 * @param scale              ���ű���
	 * @param flag               ����ѡ��:true �Ŵ�; false ��С;
	 */
	public static void scale(String srcImageFile, String result, int scale,
			boolean flag) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // �����ļ�
			int width = src.getWidth(); // �õ�Դͼ��
			int height = src.getHeight(); // �õ�Դͼ��
			if (flag) {
				// �Ŵ�
				width = width * scale;
				height = height * scale;
			} else {
				// ��С
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // ������С���ͼ
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// ������ļ���
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �������ɰ�ָ����Ⱥ͸߶ȵ�ͼ��
	 * @param srcImageFile       Դͼ���ļ���ַ
	 * @param result             �µ�ͼ���ַ
	 * @param _width             �����µ�ͼ����
	 * @param _height            �����µ�ͼ��߶�
	 * @throws IOException 
	 */
	public static void scale(String srcImageFile, String result, int _width,int _height) throws IOException {	
		BufferedImage bi = javax.imageio.ImageIO.read(new File(srcImageFile));
		BufferedImage im = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);  
		im.getGraphics().drawImage(bi.getScaledInstance(_width, _height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);  
		try {  
        	File saveFile=new File(result);  
        	ImageIO.write(im,"JPEG",saveFile); 
        } catch (Exception e) {  
        }  
	}
	
	public static void scale(String srcImageFile, String result, int _width,int _height,int x,int y) {
		try {
			
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // �����ļ�
			
			int width = src.getWidth(); // �õ�Դͼ��
			int height = src.getHeight(); // �õ�Դͼ��
			
			if (width > _width) {
				 width = _width;
			}
			if (height > _height) {
				height = _height;
			}			
			Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, x, y, null); // ������С���ͼ
			g.dispose();			
			ImageIO.write(tag, "JPEG", new File(result));// ������ļ���
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ͼ������ת�� GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
	 */
	public static void convert(String source, String result) {
		try {
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, "JPG", new File(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ɫתΪ�ڰ�
	 * 
	 * @param source
	 * @param result
	 */
	public static void gray(String source, String result) {
		try {
			BufferedImage src = ImageIO.read(new File(source));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, "JPEG", new File(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
     * �����ļ����ļ���׺�� 
     * 
     * @param fileName 
     * @return 
     */  
    public static String getExtension(String fileName) {  
        try {  
            return fileName.split("\\.")[fileName.split("\\.").length - 1];  
        } catch (Exception e) {  
            return null;  
        }  
    } 

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//�Ρ�������ɶ����...
		//cut("c:/images/ipomoea.jpg", "c:/images/t/ipomoea.jpg", 200, 150);
		//ok
		//gray("c:/images/ipomoea.jpg", "c:/images/t/ipomoea.jpg");
		//convert("c:/images/ipomoea.jpg", "c:/images/t/ipomoea.gif");
		//scale("c:/images/5105049910001020110718648723.jpg", "c:/images/t/5105049910001020110718648725.jpg",154,166,157,208);
		//scale("c:/images/rose1.jpg", "c:/images/t/rose1.jpg",154,166,157,208);
//		scale("c:/images/rose1.jpg", "c:/images/t/rose2.jpg",154,166,10,10);
		
		
//		File[] fileList = new File("D:/workspace_dingcanba20130418/DingCanBa5.3/WebContent/default/avatar").listFiles();  
//		System.out.println(fileList.length);
//	    Map<String,Boolean> map = new HashMap<String, Boolean>();  
//        for(String s : new String[]{"jpg","png"}) {  
//            map.put(s,true);   
//        }  
//        for (File f : fileList) {  
//            if(f.length() == 0)  
//                continue;  
//            if(map.get(getExtension(f.getName())) == null)  
//                continue;  
//	    	System.out.println("F:/image_user/default/avatar/"+f.getName());
//	    	System.out.println("F:/image_user/default/avatar/"+f.getName().replace("w200", "w50"));
//	    	ImageCut.scale("F:/image_user/userimg/user/20130612/"+f.getName(), "F:/image_user/userimg/user/20130612/w50/"+f.getName().replace("200w", "50w"),50,50);
//        }
		File[] fileList = new File("C:/Documents and Settings/Administrator/����/user/122/defaultavatar/").listFiles();  
		for (File f : fileList) {  
			File oldFile = f;
			  if(!oldFile.exists())
			  {
			   oldFile.createNewFile();
			  }
			  System.out.println("�޸�ǰ�ļ������ǣ�"+oldFile.getName());
			  String rootPath = oldFile.getParent();
			  System.out.println("��·���ǣ�"+rootPath);
			  File newFile = new File(rootPath +"/"+ oldFile.getName().replace("_", "w_"));
			  System.out.println("�޸ĺ��ļ������ǣ�"+newFile.getName());
			  if (oldFile.renameTo(newFile)) 
			  {
			   System.out.println("�޸ĳɹ�!");
			  } 
			  else 
			  {
			   System.out.println("�޸�ʧ��");
			  }
		}
		
		
	}
}