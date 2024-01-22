package songjaeuk.carrot.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
//import songjaeuk.carrot.comment.Comment;
import songjaeuk.carrot.common.TimeEntity;


import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String title;
    private String details;
    private String place;
    private String price;

//    private int count;

    @ElementCollection
    private List<String> files;

    private String imageUrl;

    private boolean isDeleted = false;

//    @OrderBy("id desc")
//    @JsonIgnoreProperties({"post"})
//    @OneToMany(mappedBy = "post",fetch=FetchType.EAGER,cascade = CascadeType.REMOVE)
//    private List<Comment> commentList;


    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void delete() {
        this.isDeleted = true;
    }

}
