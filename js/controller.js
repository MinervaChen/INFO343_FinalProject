/* controller.js
	Controller for directory.html
*/

$(function(){
	var directoryModel = createDirectoryModel({
		url: 'json/volunteers.json'
	});

	var directoryView = createDirectoryView({
		model: directoryModel,
		template: $('.volunteer-template'),
		container: $('.voluneers-container')
	});

	// refresh to get new volunteers from server
	directoryModel.refresh();

	// when the directory view triggers 'moreInfo'
	// open volunteer's information page
	directoryView.on('volunteerInfo', function(data) {
		alert("This currently does nothing!");
	});

	directoryView.on('volunteerEdit', function(data) {
		var volunteer = volunteerModel.getIetm(data.volunteerID)
		if (!volunteer)
			throw 'Invalid volunteer "' + volunteerID + '"!';

		volunteerModel.addItem({
	        pic: volunteer.pic,
	        name: volunteer.name,
	        title: volunteer.title,
	        age: volunteer.age,
	        email: volunteer.email,
	        phone: volunteer.phone
	    });

	})


});