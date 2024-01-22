//package songjaeuk.carrot.comment;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import songjaeuk.carrot.post.Post;
//import songjaeuk.carrot.post.PostRepository;
//import songjaeuk.carrot.user.UserDto;
//
//import java.util.List;
//
//
//public interface CommentService {
//
//    //댓글작성
//  Long writeComment(CommentRequestDTO commentRequestDTO,Long postId,String email);
//
//
//  //댓글 조회
//  List<CommentResponseDTO> commentList(Long id);
//
//  //댓글 수정
//  void updateComment(CommentRequestDTO commentRequestDTO, Long commentId);
//
//  //댓글 삭제
//    void deleteComment(Long commentId);
//}