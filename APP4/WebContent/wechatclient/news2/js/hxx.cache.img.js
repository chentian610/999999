var img_translate = "images/translate.png";

var taskArr = new Array();
var isStartTask = 0;

function loadAll(fun) {
	var objs = document.querySelectorAll('.hxx-cache-image');
	for(var i = 0; i < objs.length; i++) {
		objs[i].index = i;
		load(objs[i], fun);
	}
};

function load(obj, fun) {
	var image_url = obj.getAttribute('data-img-url');
	if(!image_url) return;
	taskArr.push(obj);
	startTask(fun);
}

function startTask(fun) {
	if(taskArr.length == 0) {
		isStartTask = false;
		fun&&fun();
		return;
	}
	var obj = taskArr.shift();
	var image_url = obj.getAttribute('data-img-url');
	obj.style.backgroundImage = "url(" + image_url + ")";	
}
