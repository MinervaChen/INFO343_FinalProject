// View

var View = {

	render: function(items) {

		var idx;
		var item;

		var propVal;
		var targetElem;
		var targetTagName;

		for (idx = 0; idx < items.length; ++idx) {
			item = items[idx];
			var clonedTemplate = this.template.clone();

			//fill basic information
			for (prop in item) {
				targetElem = clonedTemplate.find('.' + prop);

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

			//add button information: more info
			clonedTemplate.find('.more-info').attr('data-volunteer-id', item.id);
			clonedTemplate.find('.more-info').click(function() {
				var button = $(this);
				alert('More info for volunteer ' + button.attr('data-volunteer-id') + ' currently does nothing');
			});

			//add button information: edit
			var self = this;
			clonedTemplate.find('.edit').attr('data-volunteer-id', item.id);
			clonedTemplate.find('.edit').click(function() {
				var button = $(this);
				var id = button.attr('data-volunteer-id');
				$("#editInformation").modal();
				$('#editInformation').find('.finalSubmitButton').attr('data-volunteer-id', id);

				var stuff = self.controller.model.getItem(id);

				$('#editInformation').find('.form-name').val(stuff.name);
				$('#editInformation').find('.form-title').val(stuff.title);
				$('#editInformation').find('.form-age').val(stuff.age);
				$('#editInformation').find('.form-phone').val(stuff.phone);
				$('#editInformation').find('.form-email').val(stuff.email);
			});

		clonedTemplate.removeClass('volunteer-template');
		this.container.append(clonedTemplate);

		}
		this.setSubmit();
		//setClose();
	},

	setSubmit: function() {
		var self = this;
		//add button information: submit
		//$('#editInformation').find('.finalSubmitButton').attr('data-volunteer-id', item.id);
		$('#editInformation').find('.finalSubmitButton').click(function() {
			var button = $(this);
			var id = button.attr('data-volunteer-id');
			self.controller.model.removeItem(id);
			self.controller.model.addItem({
				id: id,
				pic: 'img/stearns.img',
				title: $('#editInformation').find('.form-title').val(),
				name: $('#editInformation').find('.form-name').val(),
				age: $('#editInformation').find('.form-title').val(),
				phone: $('#editInformation').find('.form-age').val(),
				email: $('#editInformation').find('.form-email').val()
			});
			self.controller.model.postJSON();
			location.reload();
		});
	},


	setController: function(controller) {
		this.controller = controller;
	}
}

//function setSubmit() {
//	//add button information: submit
//	//$('#editInformation').find('.finalSubmitButton').attr('data-volunteer-id', item.id);
//	$('#editInformation').find('.finalSubmitButton').click(function() {
//		var button = $(this);
//		var id = button.attr('data-volunteer-id');
//		view.controller.model.removeItem(id);
//		view.controller.model.addItem(id);
//	});
//}

function setClose() {
	$('#editInformation').find('.close').click(function() {
	$('.address-form')[0].reset();
	});
}

function createView(config) {
	var view = Object.create(View);
	var controller;

	apply(config, view);

	return view;
}