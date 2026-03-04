package com.main.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import org.springframework.ai.document.Document;

@SpringBootTest
class SpringApplicationTests {

    @Autowired
    private VectorStore vectorStore;

    @Test
    public void testVectorStore(){

        String filePath = "teach_manual.pdf";

        Resource resource = new FileSystemResource(filePath);

        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );

        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();

        // 3.写入向量库
        vectorStore.add(documents);

        // 4.搜索
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        SearchRequest request = SearchRequest.builder()
                .query("论文中教育的目的是什么")
                .topK(5) //选择得分最高的前5位
                .similarityThreshold(0.6) // 最低的相似度为0.6
                .filterExpression(b.eq("file_name", filePath).build()) // 只搜索来自这个PDF的内容
                .build();

        // 5.执行搜索
        List<Document> docs = vectorStore.similaritySearch(request);
        if (docs == null) {
            System.out.println("没有搜索到任何内容");
            return;
        }
        for (Document doc : docs) {
            System.out.println(doc.getId());
            System.out.println(doc.getScore());
            System.out.println(doc.getText());
        }
    }

}
