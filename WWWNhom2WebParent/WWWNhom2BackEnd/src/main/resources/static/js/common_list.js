function cleanFilter() {
	window.location = moduleURL;
}

function showDeleteConfirModal(link, entityName) {
	entityId = link.attr("entityId");

	$("#yesBtn").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure you want to delete this: " + entityName + " have Id: " + entityId + "?");

	$("#deleteConfirmModal").modal();
}