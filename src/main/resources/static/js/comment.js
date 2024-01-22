'use strict';

let commentIndex = {
    init: function(){
        $("#comment-btn-save").on("click", ()=>{
            this.commentSave();
        });
    },

    commentSave: function(){
        content:$("postId").val(),
    }
    let postId = $("#postId").val();
    console.log(data);
    console.log(postId);

     $.ajax({
                type: "POST",
                url: `/post/${postId}/comment`,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "text"
            }).done(function (res) {
                alert("댓글작성이 완료되었습니다.");
                location.href = `/post/${postId}`;
            }).fail(function (err) {
                alert(JSON.stringify(err));
            });
        },

        replyDelete: function (postId, commentId) {
                $.ajax({
                    type: "DELETE",
                    url: `/post/${postId}/comment/${commentId}`,
                    dataType: "text"
                }).done(function (res) {
                    alert("댓글삭제가 완료되었습니다.");
                    location.href = `/board/${boardId}`;
                }).fail(function (err) {
                    alert(JSON.stringify(err));
                });
            },

}

commentIndex.init();