/*
    createVolunteerView()

    Factory for a view that can render a given volunteer model.
    Uses TemplateView as its prototype and overrides render()
    to create the add to cart buttons for the various formats.

    Note: this should probably be done with a nested view/model
    but this works for now.
*/

function createVolunteerView(config) {
	var view = createTemplateView(config);

	view.afterRender = function(clonedTemplate) {
		// add alt attr to pic
		clonedTemplate.find('.pic').attr('alt', 'Photo of ' + this.model.name);

		// add More Info button
		clonedTemplate.find('.more-info').attr('data-volunteer-id', this.model.id);
	};
	
	return view;
}