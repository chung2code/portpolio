package songjaeuk.carrot.post;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import songjaeuk.carrot.common.S3Uploader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PostService {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private S3Uploader s3Uploader;






    //list
    @Transactional(rollbackFor = Exception.class)
    public List<Post> getPostList() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "Id"));
    }

    //add
    @Transactional(rollbackFor = Exception.class)
    public void addPost(PostDto dto) throws IOException {
        System.out.println("PostService's addPost");

        // 데이터 유효성 검사 추가
        if(dto.getTitle() == null || dto.getDetails() == null || dto.getPrice() == null || dto.getPlace() == null || dto.getUsername() == null || dto.getCreatedAt() == null) {
            throw new IllegalArgumentException("Invalid input data");
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDetails(dto.getDetails());
        post.setPrice(dto.getPrice());
        post.setPlace(dto.getPlace());
        post.setUsername(dto.getUsername());
        post.setCreated_at(dto.getCreatedAt());

        postRepository.save(post);
        System.out.println("저장확인 ID : " + post.getId());

        List<String> postlist = new ArrayList<>();

        if(dto.getFiles() != null && dto.getFiles().length > 0) {
            for (MultipartFile file : dto.getFiles()) {
                System.out.println("---------------------");
                System.out.println("FILE NAME:" + file.getOriginalFilename());
                System.out.println("FILE SIZE:" + file.getSize() + "Byte");
                System.out.println("----------------------");

                String imageUrl = s3Uploader.upload(file, "images"); // upload to S3 and get URL
                postlist.add(imageUrl);
            }
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
    @Transactional(rollbackFor = Exception.class)
    public void updatePost(Long id, PostDto dto) throws IOException {
        System.out.println("PostService's updatePost");

        // 데이터 유효성 검사 추가
        if(dto.getTitle() == null || dto.getDetails() == null || dto.getPrice() == null || dto.getPlace() == null || dto.getUsername() == null || dto.getCreatedAt() == null) {
            throw new IllegalArgumentException("Invalid input data");
        }

        // 기존의 게시글을 찾아옵니다.
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 게시글 정보를 업데이트합니다.
        post.setTitle(dto.getTitle());
        post.setDetails(dto.getDetails());
        post.setPrice(dto.getPrice());
        post.setPlace(dto.getPlace());
        post.setUsername(dto.getUsername());
        post.setCreated_at(dto.getCreatedAt());

        // 이미지 리스트를 업데이트합니다.
        List<String> postlist = new ArrayList<>();
        if(dto.getFiles() != null && dto.getFiles().length > 0) {
            for (MultipartFile file : dto.getFiles()) {
                System.out.println("---------------------");
                System.out.println("FILE NAME:" + file.getOriginalFilename());
                System.out.println("FILE SIZE:" + file.getSize() + "Byte");
                System.out.println("----------------------");

                String imageUrl = s3Uploader.upload(file, "images"); // upload to S3 and get URL
                postlist.add(imageUrl);
            }
        }

        post.setFiles(postlist);
        postRepository.save(post);
    }


    //delete
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long id) {
        System.out.println("PostService's deletePost");

        // 기존의 게시글을 찾아옵니다.
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 게시글을 삭제합니다.
        postRepository.delete(post);
    }




}
