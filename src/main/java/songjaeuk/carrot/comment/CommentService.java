//package songjaeuk.carrot.comment;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import songjaeuk.carrot.post.Post;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//
//    public void addComment(Long pno, String contents, String username, String profileimage) {
//        Comment comment = new Comment();
//        Post post = new Post();
//        post.setId(pno);
//
//        comment.setPost(post);
//        comment.setContent(contents);
//        comment.setUsername(username);
//        comment.setLikecount(0L);
//        comment.setProfileimage(profileimage);
//
//
//
//        commentRepository.save(comment);
//    }
//
//    public List<CommentDto> getCommentList(Long pno) {
//        List<Comment> commentList =  commentRepository.GetCommentByPnoDesc(pno);
//
//        List<CommentDto> returnComment  = new ArrayList();
//        CommentDto dto = null;
//
//        if(!commentList.isEmpty()) {
//            for(int i=0;i<commentList.size();i++) {
//
//                dto = new CommentDto();
//                dto.setPno(commentList.get(i).getPost().getId());
//                dto.setCno(commentList.get(i).getCno());
//                dto.setUsername(commentList.get(i).getUsername());
//                dto.setContent(commentList.get(i).getContent());
//                dto.setLikecount(commentList.get(i).getLikecount());
//                dto.setProfileimage(commentList.get(i).getProfileimage());
//
//
//
//                returnComment.add(dto);
//
//            }
//            return returnComment;
//        }
//
//        return null;
//
//    }
//
//    public Long getCommentCount(Long pno) {
//        return commentRepository.GetCommentCountByPnoDesc(pno);
//
//    }
//
//
//    public void deleteReply(Long cno) {
//        commentRepository.deleteById(cno);
//    }
//
//    public void thumbsUp(Long cno) {
//        Comment comment =  commentRepository.findById(cno).get();
//        comment.setLikecount(comment.getLikecount()+1L);
//        commentRepository.save(comment);
//    }
//
//
//
//
//
//}