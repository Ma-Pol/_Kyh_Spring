package hello.upload.controller;

import hello.upload.controller.form.ItemForm;
import hello.upload.domain.Item;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import hello.upload.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(
            @ModelAttribute ItemForm form,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        // 첨부 파일 1개 저장
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        // 이미지 파일 여러개 저장
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        // 데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);

        return "item-view";
    }

    // 이미지 파일 다운로드
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    // 첨부 파일 다운로드
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFilename = item.getAttachFile().getStoreFilename();
        String uploadFilename = item.getAttachFile().getUploadFilename();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFilename));

        log.info("uploadFilename={}", uploadFilename);

        String encodedUploadFilename = UriUtils.encode(uploadFilename, UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFilename + "\"";

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
