/* 
    createDirectoryView()

    factory for a view class that knows how to render the
    directory model. Uses TemplateListView as prototype.
    Overrides render() in order to add event handlers for
    the add to cart buttons.
    */

    function createDirectoryView(config) {
    	config.templateView = createVolunteerView(config);
    	var view = createTemplateListView(config);

    	view.afterRender = function() {
		// add event handlers for more info button
		this.container.find('.more-info').click(function(){
			var button = $(this);
			var eventData = button.attr('data-volunteer-id');
			view.trigger('volunteerInfo', eventData);
		});

<<<<<<< HEAD
		this.container.find('.edit-submit').click(function(){
			var button = $(this);
			var eventData =  button.attr('data-volunteer-id');
			view.trigger('volunteerEdit', eventData);
=======
		this.container.find('.edit').click(function(){
 			$("#editInformation").modal();
>>>>>>> 1f25e2f9b9474e5f80bea3a86269da6f9e45a1c7
		});
	};// afterRender()

	// auto-render if we have a model
	if (config.model)
		view.render();

	return view;
}// createDirectoryView()