package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 파일 업로드(저장) 로직을 담당
@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // 파일 리스트 내 파일들을 서버에 업로드(저장)
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    // 단일 파일을 서버에 업로드(저장)
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        return new UploadFile(originalFilename, storeFilename);
    }

    // 실제 서버 저장용 파일명 생성
    private String createStoreFilename(String originalFilename) {
        String ext = extractExt(originalFilename); // 파일 확장자명 추출
        String uuid = UUID.randomUUID().toString();

        return uuid + ext;
    }

    // .확장자명 추출
    private String extractExt(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}
