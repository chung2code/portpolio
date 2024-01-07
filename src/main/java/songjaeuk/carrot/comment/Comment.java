//package songjaeuk.carrot.comment;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import songjaeuk.carrot.common.TimeEntity;
//import songjaeuk.carrot.post.Post;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//public class Comment extends TimeEntity {
//
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @Column(name = "comment_id")
//    private Long cno;
//    @ManyToOne
//    @JoinColumn(name = "pno",foreignKey = @ForeignKey(name = "FK_comment_post",
//            foreignKeyDefinition = "FOREIGN KEY (pno) REFERENCES post(id) ON DELETE CASCADE ON UPDATE CASCADE") ) //FK설정\
//    private Post post;
//    private String username;
//    private String content;
//    private Long likecount;       //좋아요 Count
//    private String profileimage;
//
//
//
//}
