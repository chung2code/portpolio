//package songjaeuk.carrot.comment;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import songjaeuk.carrot.post.PostService;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//public class CommentController {
//
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private PostService postService;
//
//
//    @GetMapping("/reply/add")
//    public void addComment(Long pno,String contents , String username,String profileimage){
//        log.info("GET /board/reply/add " + pno + " " + contents + " " + username);
//        commentService.addComment(pno,contents, username,profileimage);
//    }
//
//
//    @GetMapping("/reply/list")
//    public List<CommentDto> getListComment(Long pno){
//        log.info("GET /board/reply/list " + pno);
//        List<CommentDto> list =  commentService.getCommentList(pno);
//        return list;
//    }
//
//
//    @GetMapping("/reply/count")
//    public Long getCount(Long pno){
//        log.info("GET /board/reply/count " + pno);
//        Long cnt = commentService.getCommentCount(pno);
//
//        return cnt;
//    }
//}
