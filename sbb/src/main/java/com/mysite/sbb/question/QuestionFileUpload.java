package com.mysite.sbb.question;

import java.io.File;
import java.util.UUID;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class QuestionFileUpload {

	public static String fileUpload(RedirectAttributes redirectAttributes, MultipartFile file) throws Exception{
		
		//파일이 존재하지 않을 때
		if (file == null || file.isEmpty()) {
            return null;
        }
		
		//파일의 확장자 검사
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
		
		if(!extension.equals("jpg") && !extension.equals("png")
		&& !extension.equals("xls") && !extension.equals("hwp")
		&& !extension.equals("doc")	&& !extension.equals("ppt")
		&& !extension.equals("csv") && !extension.equals("txt")) {
			//redirectAttributes.addFlashAttribute("error", "파일의 확장자를 확인하십쇼.");
			throw new Exception("파일의 확장자가 잘못되었습니다.");
		}
		
		//파일 경로와 파일 이름을 통해 저장하는데 파일 이름은 UUID랜덤 + 파일 이름을 붙여서 난수화 시킴(파일 업로드 취약점 방지)
		String projectPath = System.getProperty("user.dir")+"\\src\\main\\webapp\\upload";
    	UUID uuid = UUID.randomUUID();
    	String fileName = uuid + "_"+file.getOriginalFilename();
    	File saveFile = new File(projectPath, fileName);
    	file.transferTo(saveFile);
		return fileName;
	}
}
