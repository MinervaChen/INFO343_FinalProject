// View

var View = {

	renderData: function(item, template) {
		var propVal;
		var targetElem;
		var targetTagName;
		for (prop in item) {
			targetElem = template.find('.' + prop);

			if (targetElem.length > 0) {
				propVal = item[prop];

				targetTagName = targetElem.prop('tagName').toLowerCase();

				if ('img' === targetTagName) {
					targetElem.attr('src', propVal);
				}
				else if ('a' == targetTagName) {
					targetElem.atrr('href', propVal);
				}
				else {
					targetElem.html(propVal);
				}
			}
		}
	},

	setMoreInfo: function(item, template) {
		//add button information: more info
		template.find('.more-info').attr('data-volunteer-id', item.id);
		template.find('.more-info').click(function() {
			var button = $(this);
			alert('More info for volunteer ' + button.attr('data-volunteer-id') + ' currently does nothing');
		});
	},

	setEdit: function(item, template) {
		var modal = $("#editInformation");
		template.find('.edit').attr('data-volunteer-id', item.id);
		template.find('.edit').click(function() {
			modal.find('.remove-button').show();
			var button = $(this);
			var id = button.attr('data-volunteer-id');
			modal.modal();
			modal.find('.finalSubmitButton').attr('data-volunteer-id', id);
			modal.find('.form-name').val(item.name);
			modal.find('.form-title').val(item.title);
			modal.find('.form-age').val(item.age);
			modal.find('.form-phone').val(item.phone);
			modal.find('.form-email').val(item.email);
		});	
	},

	onSubmit: function(model) {
		var modal = $("#editInformation");
		modal.find('.finalSubmitButton').click(function() {
			var button = $(this);
			var id = button.attr('data-volunteer-id');
			model.removeItem(id);
			model.addItem({
				id: id,
				pic: 'img/stearns.jpg',
				title: modal.find('.form-title').val(),
				name: modal.find('.form-name').val(),
				age: modal.find('.form-age').val(),
				phone: modal.find('.form-phone').val(),
				email: modal.find('.form-email').val()
			});
			model.postJSON();
			location.reload();
		});	
	},

	setAdd: function(model) {
		var modal = $("#editInformation");
		$('.add-volunteer').click(function(){
			modal.find('.remove-button').hide();
			var id = model.length + 1;
			modal.find('.finalSubmitButton').attr('data-volunteer-id', id);
			var button = $(this);
			modal.modal();
		});
	},

	setRemove: function(model) {
		var modal = $("#editInformation");
		modal.find('.remove-button').click(function() {
			var id = modal.find('.finalSubmitButton').attr('data-volunteer-id');
			model.removeItem(id);
			model.postJSON();
		});
	},

	setController: function(controller) {
		this.controller = controller;
	}
}

function createView(config) {
	var view = Object.create(View);
	var model = config.model;
	var controller;

	apply(config, view);

	return view;
}