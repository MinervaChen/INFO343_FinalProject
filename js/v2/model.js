// Model

var Model = {

	postJSON: function() {
		alert(JSON.stringify(this.items, null, 4));
	},

	build: function(url) {
		var self = this;
		$.getJSON(url, function(items) {
			$.each(items, function(idx, item) {
				if (item.id != undefined)
					self.items[item.id] = item;
			});
		});
	},

	addItem: function(item) {
		if (!this.items.hasOwnProperty(item.id)) {
			this.items[item.id] = item;
		} else {
			alert('An item with ID ' + item.id + ' already exists.');
		}
	},

	removeItem: function(id) {
		delete this.items[id];
	},

	getItem: function(id) {
		return this.items[id];
	},

	getAllItems: function() {
		var itemList = [];
		$.each(this.items, function(){
			// test if this refers to key or value
			itemList.push(this);
		});
		return itemList;
	},

	setController: function(controller) {
		this.controller = controller;
	}
}

function createModel(config) {
	var model = Object.create(Model);
	var controller;

	apply(config, model);

	model.items = model.items || {};

	model.build(model.url);
	return model;
}

var Volunteer = {

}