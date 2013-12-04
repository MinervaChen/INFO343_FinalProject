/*
    createDirectoryModel()

    Creates a model for the list of volunteers.
    This uses the ListModel as the prototype, but adds 
    a few specific methods.

    The config parameter should contain the following properties:
    - url (string) URL from which we should fetch movie JSON
*/

function createDirectoryModel(config) {
	var model = createListModel(config);

	model.refresh = function() {
		if (!this.url)
			throw new 'No url property supplied in config!';

		$.getJSON(this.url, function(volunteers){
			// set the items
			// trigger change event
			model.setItems(volunteers);
		});
		
	};// refresh

	return model;
}// createDirectoryModel()