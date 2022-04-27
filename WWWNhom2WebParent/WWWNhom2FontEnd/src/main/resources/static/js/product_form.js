var extraImageCount = 0;
$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = modelURL;
	});

	$("input[name='extraImage']").each(function(index) {
		extraImageCount++;
		$(this).change(function() {
			showExtraImageThumbnail(this, index);
		});
	});
	
	$("a[name='linkRemoveExtraImage']").each(function(index){
		$(this).click(function(){
			removeExtraImage(index);
		});	
	});
	
	$("a[name='linkRemoveExtraDetail']").each(function(index){
		$(this).click(function(){
			removeDetailByIndex(index);
		});	
	});
});

function showExtraImageThumbnail(fileInput, index) {
	var file = fileInput.files[0];
	
	fileName = file.name;
	
	imageNameHiddenField = $("#imageName" + index);
	if(imageNameHiddenField.length){
		imageNameHiddenField.val(fileName);
	}
	
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#extrathumbnail" + index).attr("src", e.target.result);
	};

	reader.readAsDataURL(file);

	if (index >= extraImageCount - 1) {
		addNextExtraImageSection(index + 1);
	}
};

function addNextExtraImageSection(index) {
	html = `
		<div class="col border m-3 p-2" id="divExtraImage${index}">
				<div id="extraImageHeader${index}">
					<label>Extra Images #${index + 1}:</label>
				</div>
				<div class="m-2">
					<img id="extrathumbnail${index}" class="img-fluid" th:src="${defaultImageUrl}"
					
					/>
				</div>
				<div>
					<input type="file" name="extraImage" onchange="showExtraImageThumbnail(this, ${index})">
				</div>
			</div>
	`;


	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark float-right" 
		href="javascript:removeExtraImage(${index - 1})"></a>
	`;

	$("#divProductImages").append(html);
	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
	extraImageCount++;
}

function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();
}

function addNewDetail() {
	allDivDetail = $("[id^='divDetail']");
	divDetailCount = allDivDetail.length;

	html = `
		<div class="form-inline" id="divDetail${divDetailCount}">
			<input type="hidden" name="detailIDs" value="0">
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25" name="detailNames" maxlength="255" />
			<label class="m-3">Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255" />
		</div>
	`;

	$("#divProductDetail").append(html);
	previousDetail = allDivDetail.last();
	previousDetailId = previousDetail.attr("id");
	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark " 
		href="javascript:removeDetail('${previousDetailId}')"></a>
	`;

	previousDetail.append(htmlLinkRemove);
}

function removeDetail(id){
	$("#" + id).remove();
}

function removeDetailByIndex(index){
	$("#divDetail" + index).remove();
}
