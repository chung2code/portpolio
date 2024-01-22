//package songjaeuk.carrot.comment;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Getter
//@NoArgsConstructor
//public class CommentResponseDTO {
//
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private Long id;
//    private String content;
//    private String username;
//    private String email;
//    private String imageUrl;
//
//    @Builder
//    public CommentResponseDTO(Comment comment){
//        this.createdAt = comment.getCreated_at();
//        this.updatedAt = comment.getUpdated_at();
//        this.id = comment.getId();
//        this.content = comment.getContent();
//        this.username = comment.getUser().getName();
//        this.email = comment.getUser().getEmail();
//        //this.imageUrl = comment.getUser().getImage().getUrl();
//
//    }
//}
