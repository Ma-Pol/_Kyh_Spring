package hello.upload.controller.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 클라이언트-서버 간 상품 데이터 전송용 폼
@Data
public class ItemForm {
    private Long itemId;
    private String itemName;
    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;
}
