$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = modelURL;
	});

	$("#fileImage").change(function() {
		if(!checkFileSize(this)){
			return;
		}else{
			showImageThumbnail(this);
		}
	});
});

function checkFileSize(fileInput){
	fileSize = fileInput.files[0].size;
	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("You must choose an image less than 1MB!");
		fileInput.reportValidity();
		
		return false;
	} else {
		return true;
	}
}

function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};

	reader.readAsDataURL(file);
};

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorDialog(message) {
	showModalDialog("Error", message);
}
function showWarningDialog(message) {
	showModalDialog("Warning", message);
}