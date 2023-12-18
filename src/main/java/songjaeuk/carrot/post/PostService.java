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

    private String postPath = "c:\\CarrotImage\\";
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private AmazonS3 amazonS3;
    @Value("{cloud.aws.s3.bucket}")
    private String bucket="carrot-bucket";

    public String uploadImage(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();		// ObjectMetadata 를 통해 파일에 대한 정보를 추가
        objectMetadata.setContentLength(multipartFile.getSize());		// multipartFil 의 크기 설정 (byte)
        objectMetadata.setContentType(multipartFile.getContentType());	// multipartFil 의 컨텐츠 유형 설정

        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)		// 객체를 S3에 업로드
                    .withCannedAcl(CannedAccessControlList.PublicRead));		// 업로드된 객체에 대한 공개 읽기 권한을 설정
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        return amazonS3.getUrl(bucket, fileName).toString();		// 업로드된 객체(사진)의 URL을 반환
    }

    // 파일 이름 생성
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 확장자
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }




    //list
    @Transactional(rollbackFor = Exception.class)
    public List<Post> getPostList() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "Id"));
    }

    //add
    @Transactional(rollbackFor = Exception.class)
    public void addPost(PostDto dto) throws IOException {
        System.out.println("PostService's addPost");

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

        for (MultipartFile file : dto.getFiles()) {
            System.out.println("---------------------");
            System.out.println("FILE NAME:" + file.getOriginalFilename());
            System.out.println("FILE SIZE:" + file.getSize() + "Byte");
            System.out.println("----------------------");

            String imageUrl = uploadImage(file); // upload to S3 and get URL
            postlist.add(imageUrl);
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
