//package songjaeuk.carrot.comment;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CommentRepository extends JpaRepository<Comment,Long>{
//
//    @Query("SELECT c FROM Comment c WHERE pno = :bno ORDER BY cno DESC")
//    List<Comment> GetCommentByPnoDesc(@Param("pno") Long pno);
//
//    @Query("SELECT COUNT(r) FROM Reply r WHERE bno = :bno")
//    Long GetCommentCountByPnoDesc(@Param("pno") Long pno);
//}
