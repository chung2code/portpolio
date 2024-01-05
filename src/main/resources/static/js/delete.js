function delete_btn(element) {
    // 버튼의 data-id 속성에서 id를 가져옵니다.
    const postId = element.dataset.id;

    if (confirm('정말 삭제하시겠습니까?')) {
        fetch(`/post/delete/${postId}` , {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('게시물이 성공적으로 삭제되었습니다.');
                window.location.href = "/post/list";
            } else {
                alert('게시물 삭제에 실패했습니다.');
            }
        });
    }
}
