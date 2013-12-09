// Main

$(function(){
	Parse.initialize("o6jNbniL23Of8iukgR0goPcdYfIxP0X08CAZyfYH", "rBs9dEbTlY6DPv8Vxyhb4szyVYDlHLMswvISzRio");

	$.ajaxSetup({async: false});

	var model = createModel({
		url: 'json/volunteers.json'
	});
	
	var view = createView({
		model: model,
		template: $('.volunteer-template'),
		container: $('.volunteers-container')
	});

	onPageLoad(model, view);
	view.onSubmit(model);
	view.setAdd(model);
	view.setRemove(model);
});

//function onSubmit(model) {
//	var modal = $("#editInformation");
//	modal.find('.finalSubmitButton').click(function() {
//		var button = $(this);
//		var id = button.attr('data-volunteer-id');
//		model.removeItem(id);
//		model.addItem({
//			id: id,
//			pic: 'img/stearns.img',
//			title: modal.find('.form-title').val(),
//			name: modal.find('.form-name').val(),
//			age: modal.find('.form-title').val(),
//			phone: modal.find('.form-age').val(),
//			email: modal.find('.form-email').val()
//		});
//		model.postJSON();
//		location.reload();
//	});	
//}


function onPageLoad(model, view) {
	items = model.getItems();
	$.each(items, function(idx, item) {
		var clonedTemplate = view.template.clone();
		view.renderData(item, clonedTemplate);
		view.setMoreInfo(item, clonedTemplate);
		view.setEdit(item, clonedTemplate);
		clonedTemplate.removeClass('volunteer-template');
		view.container.append(clonedTemplate);
	});
}

//function doOnSubmit(newItem) {
//	model.removeItem(newItem[id]);
//	model.addItem(newItem);
//	model.postJSON();
//	onRefresh();
//}
