package songjaeuk.carrot.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import songjaeuk.carrot.config.auth.PrincipalDetails;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/post")
public class PostController {
    @Autowired
  private PostService postService;

    @GetMapping("list")
    public void readAll(String reset, Model model) {
        log.info("GET /post/list");
        List<Post> list = postService.getPostList();
        System.out.println(list);
        list.stream().forEach(item-> System.out.println(item));

        for (Post post : list) {
            if (!post.getFiles().isEmpty()) {
                String filename = post.getFiles().get(0);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/post/")
                        .path(post.getUsername())
                        .path("/")
                        .path(post.getId().toString())
                        .path("/")
                        .path(filename)
                        .toUriString();
                post.setImageUrl(fileDownloadUri);
            }
        }

        model.addAttribute("list",list);


    }

    @GetMapping("/add")
    public void add(){

    }
    @PostMapping("/add")
    public ResponseEntity<?> add_post(
            @RequestParam("title") String title,
            @RequestParam("details") String details,
            @RequestParam("price") String price,
            @RequestParam("place") String place,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            Authentication authentication) throws IOException {

        // 유효성 검사 추가
        if(title == null || details == null || price == null || place == null || authentication == null || files == null || files.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("POST /post/add " + files);

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setDetails(details);
        dto.setPrice(price);
        dto.setPlace(place);
        dto.setFiles(files);
        dto.setCreatedAt(LocalDateTime.now());

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        if (principal != null) {
            dto.setUsername(principal.getUsername());
            postService.addPost(dto);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/read")
    public void read(Long id,Model model){
        System.out.println("GET /post/read.."+id);
        Post post = postService.getPost(id);

        System.out.println(post);
        List<String> files = post.getFiles();
        System.out.println(files);
        model.addAttribute("post",post);
        model.addAttribute("files",files);
    }
    //update
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update_post(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("details") String details,
            @RequestParam("price") String price,
            @RequestParam("place") String place,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            Authentication authentication) throws IOException {

        // 유효성 검사 추가
        if(id == null || title == null || details == null || price == null || place == null || authentication == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("PUT /post/update " + files);

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setDetails(details);
        dto.setPrice(price);
        dto.setPlace(place);
        dto.setFiles(files);
        dto.setCreatedAt(LocalDateTime.now());

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        if (principal != null) {
            dto.setUsername(principal.getUsername());
            postService.updatePost(id, dto);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    //delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete_post(
            @PathVariable("id") Long id,
            Authentication authentication) {

        // 유효성 검사 추가
        if(id == null || authentication == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("DELETE /post/delete " + id);

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        if (principal != null) {
            postService.deletePost(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}


