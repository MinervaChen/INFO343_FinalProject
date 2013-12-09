/*
    TemplateView

    Simple template merge view object. This can merge
    a model with a given HTML template by matching model
    property names to element class names.
    
    Used as prototype by createTemplateView()
*/
var TemplateView = {
	render: function() {
		var propVal;	    // current property value
		var targetElem;     // target element in cloned template
		var targetTagName;  // tag name of target element

		var clonedTemplate = this.template.clone();

		// iterate over the properties of the model
		// look for descendant element with the same
		// style class name
		for (prop in this.model) {

			targetElem = clonedTemplate.find('.' + prop);
			if (targetElem.length > 0) {
				// get the property value
				propVal = this.model[prop];

				// get the tag name for the target element
				targetTagName = targetElem.prop('tagName').toLowerCase();

				if ('img' === targetTagName) {
					targetElem.attr('src', propVal);
				} 
				else if ('a' == targetTagName) {
					targetElem.attr('href', propVal);
				} 
				else {
					targetElem.html(propVal);
				}
			}
		}// for each prop in model object

		// add cloned and populated template to container
		clonedTemplate.removeClass('volunteer-template');
		this.container.append(clonedTemplate);

		// call the afterRender method if there is one
		// (can be used by derived objects to add things
		// or register event handlers after the render is complete)
		if (this.afterRender)
			this.afterRender(clonedTemplate, this.model);

		return clonedTemplate;
	}// render()
}// TemplateView

/*  
    createTemplateView()

    Creates a simple single-object template view, which knows
    how to merge a single model object (POJO) with an HTML
    template block, matching property names to element style
    class names.

    The config parameter should be an object with the following properties:
    
    model (object) the model for the view
    template (jQuery object) reference to the HTML template for the view
    container (jQuery object) refernece to the container into which
                            the view should append the merged template    
*/

function createTemplateView(config) {
	var view = Object.create(TemplateView);

	// apply the config properties to view
	apply(config, view);

	// enable this to raise events
	view = makeEventSource(view);

	return view;
}// createTemplateView()

/*
    TemplateListView

    Simple view object that iterates the items in a ListModel
    and merges each with a TemplateView

    Used as prototype by createTemplateListView()
*/

var TemplateListView = {
	render: function(){
		var templateView = this.templateView || createTemplateView({
			template: this.template,
			container: this.container
		});

		var items = this.model.getItems();
		var idx;

		this.container.empty();

		if (items && items.length && items.length > 0) {
			for (idx = 0; idx < items.length; ++idx) {
				templateView.model = items[idx];
				templateView.render();
			}
		}

		// call the afterRender moethod if there is one
		// (can be used by derived objects to add things or
		// register event handlers after the render is complete)
		if (this.afterRender)
			this.afterRender();
	}// render()
}

/*  
    createTemplateListView()

    Creates a simple template list view, which works with a ListModel
    This iterates over the items and uses a single-object template view 
    to render each model object in the array.

    The config parameter should be an object with the following properties:
    
    - model (ListModel) the model for the view
    - template (jQuery object) reference to the HTML template for the view
    - container (jQuery object) refernece to the container into which
                            the view should append the merged template

    the config may also include these optional properties:

    - templateView (object) instance of a template view to use for each object

*/

function createTemplateListView(config) {
	var view = Object.create(TemplateListView);

	// apply config properties to view
	apply(config, view);

	// enable this to raise events
	view = makeEventSource(view);

	// listen for change event on ListModel
	// and re-render
	view.model.on('change', function(){
		view.render();
	}, view);

	return view;
}// createTemplateListView()