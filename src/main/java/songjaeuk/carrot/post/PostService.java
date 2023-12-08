package songjaeuk.carrot.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostService {

    private String postPath = "c:\\CarrotImage\\";
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    //list
    @Transactional(rollbackFor = Exception.class)
    public List<Post> getPostList() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "Id"));
    }

    //add
    @Transactional(rollbackFor = Exception.class)
    public void addPost(PostDto dto)throws IOException {
        System.out.println("PostService's addPost");

        //DB저장 후 처리

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDetails(dto.getDetails());
        post.setPrice(dto.getPrice());
        post.setPlace(dto.getPlace());
        post.setUsername(dto.getUsername());
       // post.setCreatedAt(dto.getCreatedAt());

        postRepository.save(post);
        System.out.println("저장확인 ID : " + post.getId());

        //저장 폴더 지정

        String uploadPath = postPath + File.separator + dto.getUsername() + File.separator + post.getId();
        File dir = new File(uploadPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        //게시물당 파일은 5장까지


        List<String> postlist = new ArrayList<>();

        for(MultipartFile files : dto.getFiles()) {
            System.out.println("---------------------");
            System.out.println("FILE NAME:" + files.getOriginalFilename());
            System.out.println("FILE SIZE:" + files.getSize() + "Byte");
            System.out.println("----------------------");
            postlist.add(files.getOriginalFilename());
            System.out.println(dto.getFiles());

            File fileobj = new File(uploadPath, files.getOriginalFilename());
            files.transferTo(fileobj);

        }
        post.setFiles(postlist);
        postRepository.save(post);
    }

    //read
    @Transactional(rollbackFor = Exception.class)
    public Post getPost(Long Id){
        Optional<Post> optionalPost = postRepository.findById(Id);
        if (!optionalPost.isEmpty()){
            return optionalPost.get();
        }
        return null;
    }

    //update

    //delete




}
