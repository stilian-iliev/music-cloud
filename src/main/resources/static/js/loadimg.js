window.addEventListener('load', previewPic)

function previewPic() {
    let input = document.getElementById("imageUpload");
    input.addEventListener("change", changePic)
}

function changePic(e) {
    var input = e.target;
    if (input.files && input.files[0]) {
        var file = input.files[0];
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function(e) {
            document.querySelector("#imagePreview").style.backgroundImage = 'url('+ reader.result +')';
        }
    }
}