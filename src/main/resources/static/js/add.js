document.addEventListener("DOMContentLoaded", function () {
    const formData = new FormData(); // 폼 관련 저장

    const uploadBox_el = document.querySelector(".upload-box");

    // dragenter / dragover /dragleave / drop 이벤트 핸들링
    uploadBox_el.addEventListener("dragenter", function (e) {
        e.preventDefault();
        console.log("dragenter...");
    });

    uploadBox_el.addEventListener("dragover", function (e) {
        e.preventDefault();
        uploadBox_el.style.opacity = "0.5";
        console.log("dragover..");
    });

    uploadBox_el.addEventListener("dragleave", function (e) {
        e.preventDefault();
        uploadBox_el.style.opacity = "1";
        console.log("dragleave...");
    });

    uploadBox_el.addEventListener("drop", function (e) {
        e.preventDefault();

        // 유효성 체크 - 이미지 파일만 처리
        const imageFiles = Array.from(e.dataTransfer.files).filter((file) =>
            file.type.startsWith("image/")
        );
        if (imageFiles.length === 0) {
            alert("이미지 파일만 가능합니다");
            return;
        }

        // 미리보기 생성
        const file = imageFiles[0];
        formData.append("files", file); // formData에 저장
        console.log(formData);

        const reader = new FileReader();

        reader.readAsDataURL(file);
        reader.onload = function (event) {
            const preview = document.getElementById("preview");
            const imgEl = document.createElement("img");
            imgEl.setAttribute("src", event.target.result);
            preview.appendChild(imgEl);
        };
    });

    document.querySelector(".add_product-btn").addEventListener('click', function (event) {
        var titleValue = document.getElementById('title').value;
        var detailsValue = document.getElementById('details').value;
        var priceValue = document.getElementById('price').value;
        var placeValue = document.getElementById('place').value;
        var files = document.getElementById('files').files;

        // 유효성 검사 추가
        if(!titleValue || !detailsValue || !priceValue || !placeValue || files.length == 0) {
            alert('모든 필드를 채워주세요.');
            return;
        }

        var formData = new FormData();
        formData.append("title", titleValue);
        formData.append("details", detailsValue);
        formData.append("price", priceValue);
        formData.append("place", placeValue);

        for (var i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
        }
        console.log('formData', formData);

        axios({
            method: "post",
            url: " post/add",
            data: formData,
            headers: { "Content-Type": "multipart/form-data" },
        })
        .then(function (response) {
            alert("성공적으로 게시물이 추가되었습니다.");
            window.location.href = "post/list";
        })
        .catch(function (error) {
            alert("게시물 추가 실패 : " + error);
            console.error(error);
        });
    });
});