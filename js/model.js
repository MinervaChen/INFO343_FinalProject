// Model

var Model = {

	postJSON: function() {
		alert('this JSON would be sent somewhere if it had soemwhere to go: ' +
			JSON.stringify(this.items, null, 4));
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

	//addItemToParse: function(item) {
	//	var data = JSON.stringify(item);
	//	$.ajax({
	//		"type":"POST",
	//		"url":"https://api.parse.com/1/classes/volunteers",
	//		"data": data,
	//		"contentType":"application/json",
	//		"dataType":"json",
	//		"headers":this.headers,
	//		success:function(data, status, xhr) {
	//			log("data:" + JSON.stringify(data) + " status: " + status);
	//			if (status === 'success') {
	//				log('Data saved successfully');
	//				alert('Data saved successfully');
	//				setTimeout( function(){
	//					getData();
	//				}, 1000);
	//			} else {
	//				// Show error message:
	//				alert("Your data didn't save. Please check that you are online. Status: " + status);
	//			}
	//		},
//
	//		error:function(data, status, xhr) {
	//		// Show error message:
	//			alert("Your data didn't save. Please check that you are online. Status: " + status);
	//		}
	//	}); 
	//}, 

	// Get all records from Parse.com
	//getData: function() {
	//	$.ajax({
	//		"type":"GET",
	//		"url":"https://api.parse.com/1/classes/volunteer",
	//		"dataType":"json",
	//		"contentType":"application/json",
	//		"headers":headers,
	//		success:function(data, status, xhr) {
	//			// Var to store result string:
	//			var result = "";
//
	//			// We now step through all the returned records and add each to result string:
	//			for(var i = 0; i < data.results.length; i++) {
	//			// Output name and ages stored on a new line:
	//				result = result +""+ data.results[i].name +" is "+ data.results[i].age +"";
	//			}
	//		// Log & Output results:
	//		log(result);
	//		alert(result);
	//		}
	//	});
	//},

	removeItem: function(id) {
		delete this.items[id];
	},

	getItem: function(id) {
		return this.items[id];
	},

	getItems: function() {
		return this.items;
		//var itemList = [];
		//$.each(this.items, function(){
		//	// test if this refers to key or value
		//	itemList.push(this);
		//});
		//return itemList;
	},

	setController: function(controller) {
		this.controller = controller;
	}
}

function createModel(config) {
	var model = Object.create(Model);
	var controller;
	var headers = {"X-Parse-Application-Id": "o6jNbniL23Of8iukgR0goPcdYfIxP0X08CAZyfYH", "X-Parse-REST-API-Key":"TnxnQh7vNs9p9OkHgBT7bc5yPPEMgQebWiGtMkTW"};

	apply(config, model);

	model.items = model.items || {};

	model.build(model.url);
	return model;
}

var Volunteer = {

}