package com.dingcan.util;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * �ļ��ϴ��ı�
 * @author Administrator
 *
 */
public class FileUploadForm {

	private List<MultipartFile> files;
	
	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
}
