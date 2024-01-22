//package songjaeuk.carrot.comment;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import songjaeuk.carrot.post.Post;
//import songjaeuk.carrot.post.PostRepository;
//import songjaeuk.carrot.user.User;
//import songjaeuk.carrot.user.UserRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CommentServiceImpl implements CommentService{
//
//    private final UserRepository userRepository;
//    private final PostRepository postRepository;
//    private final CommentRepository commentRepository;
//
//    @Override
//    public Long writeComment(CommentRequestDTO commentRequestDTO, Long postId, String id) {
//        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("아이디가 존재하지 않습니다."));
//        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("게시물을 찾을 수 없습니다."));
//        Comment result = Comment.builder()
//                .content(commentRequestDTO.getContent())
//                .post(post)
//                .user(user)
//                .build();
//        commentRepository.save(result);
//
//        return result.getId();
//    }
//
//    @Override
//    public List<CommentResponseDTO> commentList(Long id) {
//        Post post = postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("게시물을 찾을 수 없습니다"));
//        List<Comment> comments = commentRepository.findByPost(post);
//
//        return comments.stream()
//                .map(comment -> CommentResponseDTO.builder()
//                        .comment(comment)
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void updateComment(CommentRequestDTO commentRequestDTO, Long commentId) {
//        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
//        comment.update(commentRequestDTO.getContent());
//        commentRepository.save(comment);
//    }
//
//    @Override
//    public void deleteComment(Long commentId) {
//        commentRepository.deleteById(commentId);
//    }
//}
