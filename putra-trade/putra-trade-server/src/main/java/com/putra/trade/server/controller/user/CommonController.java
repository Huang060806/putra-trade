package com.putra.trade.server.controller.user;

import com.putra.trade.common.exception.BusinessException;
import com.putra.trade.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传（本地磁盘，后期可切 OSS）
 */
@Slf4j
@RestController
@RequestMapping("/user/common")
@Tag(name = "用户端-通用")
public class CommonController {

    @Value("${putra.upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    @Operation(summary = "图片上传，返回可访问 URL")
    public Result<String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        String original = file.getOriginalFilename();
        String suffix = original != null && original.contains(".")
                ? original.substring(original.lastIndexOf(".")) : ".jpg";
        String filename = UUID.randomUUID() + suffix;

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(dir, filename));
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
        return Result.success("/upload/" + filename);
    }
}
