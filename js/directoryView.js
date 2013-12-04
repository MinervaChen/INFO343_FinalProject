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

		this.container.find('.edit-submit').click(function(){
			var button = $(this);
			var eventData =  button.attr('data-volunteer-id');
			view.trigger('volunteerEdit', eventData);
		});
	};// afterRender()

	// auto-render if we have a model
	if (config.model)
		view.render();

	return view;
}// createDirectoryView()