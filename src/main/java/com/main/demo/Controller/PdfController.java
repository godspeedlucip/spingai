package com.main.demo.Controller;

import com.main.demo.Repository.FileService;
import com.main.demo.Service.ChatRecordService;
import com.main.demo.entity.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/pdf")
public class PdfController {
    // 管理chatId和文件的对应关系，以及文件的存储
    @Autowired
    private FileService fileService;

    // 负责管理聊天的
    @Autowired
    @Qualifier("pdfChatClient") //存在多个client时，去进行区分。
    private ChatClient pdfChatClient;

    // 保存对话历史的
    @Autowired
    private ChatRecordService chatRecordService;

    /**
     * 文件上传接口
     */
    @RequestMapping("/upload/{chatId}")
    public Result uploadPdf(@PathVariable String chatId, @RequestParam("file") MultipartFile file) {
        try {
            // 1. 校验文件是否为PDF格式
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return Result.fail("只能上传PDF文件！");
            }
            // 2.保存文件
            boolean success = fileService.save(chatId, file.getResource());
            if (!success) {
                return Result.fail("保存文件失败！");
            }
            return Result.ok();
        } catch (Exception e) {
            log.error("Failed to upload PDF.", e);
            return Result.fail("上传文件失败！");
        }
    }

    /**
     * 文件下载接口
     */
    @GetMapping("/file/{chatId}")
    public ResponseEntity<Resource> download(@PathVariable("chatId") String chatId) throws IOException {
        // 1.读取文件
        Resource resource = fileService.getFile(chatId);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        // 2.文件名编码，写入响应头
        String filename = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8);
        // 3.返回文件
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    /**
     * 聊天接口
     * @param prompt
     * @param chatId
     * @return
     */
    @GetMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    public Flux<String> chatWithPdf(@RequestParam String prompt, @RequestParam String chatId) {
        // 1.读取文件
        Resource resource = fileService.getFile(chatId);

        // 2. 将当前对话写入
        Long user_id = 1L;
        chatRecordService.saveRecord(user_id, chatId, "pdf"); // 这里的type就由给定的网络路径确定

        // 3. 调用ChatClient进行聊天，设定会话id和文件，让顾问知道用哪个PDF文件来回答问题
        return pdfChatClient
                .prompt(prompt)
                .advisors(advisorSpec -> advisorSpec
                        .param(ChatMemory.CONVERSATION_ID, chatId) // 设定会话id，让顾问知道用哪个PDF文件来回答问题
                )
                .advisors(advisorSpec -> advisorSpec
                        .param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "file_name=='"+resource.getFilename()+"'") // 设定过滤条件，让顾问从向量库中找到对应文件的内容来
                )
                .stream() // 流式调用
                .content();
    }
}
