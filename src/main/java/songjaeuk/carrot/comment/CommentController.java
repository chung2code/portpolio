//package songjaeuk.carrot.comment;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import songjaeuk.carrot.config.auth.PrincipalDetails;
//
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class CommentController {
//
//    private final CommentService commentService;
//
//    @PostMapping("/post/{postId}/comment")
//    public void save(@PathVariable Long postId,
//                     @RequestBody Comment comment,
//                     @AuthenticationPrincipal PrincipalDetails principalDetails){
//        commentService.commentSave(postId,comment,principalDetails.getUser());
//    }
//
//    @DeleteMapping("/post/{postId}/comment/{commentId}")
//    public void delete(@PathVariable Long commentId){
//        commentService.commentDelete(commentId);
//    }
//}
