// Controller

// Defines Controller 
var Controller = {

	onPageLoad: function() {
		var allItems = this.model.getAllItems();
		this.view.render(allItems);
	},

	onChange: function() {

	},

	onMoreInfo: function() {

	},

	onEdit: function(id) {
		
	}
}

// Constructs Controller

function createController(config) {
	var controller = Object.create(Controller);
	var model;
	var view;
	
	apply(config, controller);
	
	return controller;
}