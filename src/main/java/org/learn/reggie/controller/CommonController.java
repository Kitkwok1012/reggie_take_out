package org.learn.reggie.controller;


import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

//    @Value("${reggie.path}")
//    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();//abc.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //Use UUID
        String fileName = UUID.randomUUID().toString() + suffix;//dfsdfdfd.jpg



        ApplicationHome h = new ApplicationHome(getClass());
        File dir = h.getSource();
        System.out.println("Get Root: " + dir.getParentFile().toString());
        //File dir = new File(basePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            //file.transferTo(new File(basePath + fileName));
            //项目运行的根目录
            file.transferTo(new File(dir.getParentFile().toString() + "\\" +fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        ApplicationHome h = new ApplicationHome(getClass());
        File dir = h.getSource();
        System.out.println("Get Root: " + dir.getParentFile().toString());
        try {
            //输入流，通过输入流读取文件内容
            //FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            FileInputStream fileInputStream = new FileInputStream(new File(dir.getParentFile().toString() + "\\" + name));

            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
