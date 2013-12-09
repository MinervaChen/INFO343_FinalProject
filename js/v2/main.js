// Main
$(function(){
	Parse.initialize("o6jNbniL23Of8iukgR0goPcdYfIxP0X08CAZyfYH", "rBs9dEbTlY6DPv8Vxyhb4szyVYDlHLMswvISzRio");

	$.ajaxSetup({async: false});

	var myModel = createModel({
		url: 'json/volunteers.json'
	});
	
	var myView = createView({
		template: $('.volunteer-template'),
		container: $('.volunteers-container')
	});

	myController = createController({
		model: myModel,
		view: myView
	});

	myModel.setController(myController);
	myView.setController(myController);

	myController.onPageLoad();
});