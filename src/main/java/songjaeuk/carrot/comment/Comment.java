//package songjaeuk.carrot.comment;
//
//import lombok.*;
//import songjaeuk.carrot.common.TimeEntity;
//import songjaeuk.carrot.post.Post;
//import songjaeuk.carrot.user.User;
//import songjaeuk.carrot.user.UserDto;
//
//
//import javax.persistence.*;
//
//
//@Entity
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//public class Comment extends TimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length =500)
//    private String content;
//
//    @ManyToOne
//    @JoinColumn(name="postId")
//    private Post post;
//
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private UserDto user;
//
//
//    public void save(Post post, UserDto user){
//        this.post = post;
//        this.user = user;
//    }
//
//
//
//
//}
