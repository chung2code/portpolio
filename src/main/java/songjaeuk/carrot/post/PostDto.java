package songjaeuk.carrot.post;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class PostDto {

    private Long Id;
    private String username;
    @NotBlank
    private String title;
    private String details;
    @NotBlank
    private String place;
    private String price;
    private MultipartFile[] files;
//    private int count;



    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
