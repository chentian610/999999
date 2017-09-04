var storage = window.localStorage;
var dict_group = getParameterByUrl("dict_group");
var module_code = getParameterByUrl("module_code");
var file_upload_action = localStorage.getItem('file_upload_action');
var image_cut_action = localStorage.getItem('image_cut_action');
var school_news_action = localStorage.getItem('NEWS_TEMPLATE_ON');
var editor_html = '';
function initPage(){
	if (school_news_action=="TRUE"){
		$('#uploadPicOld').show();
		$('#zwf-news-yl').show();
		$('.zwf-news-ys').hide();
		$('.zwf-news-module').show();
		$('.zl-title-new').append($('#zwf-code-name').text());
	    $('#news-template-type-035010').hide();
	    $('#news-template-type-035015').hide();
	    $('#news-template-type-035020').hide();
		$('#news-template-type-035030').hide();
		$('#UploadFileBox').show();
		bindUploadLogoClickEvent();
        bindUploadPicOldClickEvent();
        bindOnPasteEvent();
        bindEditorOnLKeyUpEvent();
	} else {
        $('.col-sm-left').addClass('col-sm-11');
        $('.col-sm-left').removeClass('col-sm-8');
        $('.col-sm-right').addClass('col-sm-1');
        $('.col-sm-right').removeClass('col-sm-4');
        $('.sch-logo').show();
        return;
    }
}

function initKeyUpEvent(){
    $('.zwf-news-keyup').keyup(function(){
        $('.zwf-dept-name').empty().append($('#dept_name').val());
        $('.zl-new-title').empty().append($('#article_title').val());
        $('.zl-content-text').empty().append($('#editor').html());
		if ($('#article-module').val() == '035025') editor_html = $('#editor').html();
    });
}

function bindOnPasteEvent() {
    $("#editor").on("paste",function(e) {
		var pastedText = undefined;
		if (window.clipboardData && window.clipboardData.getData) { // IE
			pastedText = window.clipboardData.getData('Text');
		} else {
			pastedText = e.originalEvent.clipboardData.getData('Text');//e.clipboardData.getData('text/plain');
		}
		var range = window.getSelection().getRangeAt(0);
		//range.insertNode(document.createTextNode(pastedText));
        var selection=window.getSelection();
        var fragment = range.createContextualFragment(pastedText);
        var oLastNode = fragment.lastChild; //获得DocumentFragment的末尾位置
        range.insertNode(fragment);
        range.setEndAfter(oLastNode);//设置末尾位置
        range.collapse(false);//合并范围至末尾
        selection.removeAllRanges();//清除range
        selection.addRange(range);//设置range
        return false;
    });
}

function bindEditorOnLKeyUpEvent() {
    $("#editor").keydown(function () {
        if(event.keyCode==9){
			var range = window.getSelection().getRangeAt(0);
			var selection=window.getSelection();
			var nbsp = range.createContextualFragment("&nbsp;&nbsp;&nbsp;&nbsp;");
			var oLastNode = nbsp.lastChild; //获得DocumentFragment的末尾位置
			range.insertNode(nbsp);
			range.setEndAfter(oLastNode);//设置末尾位置
			range.collapse(false);//合并范围至末尾
			var html = $('blockquote').html();
			$('#editor blockquote').remove();
			$('#editor').append(html);
			$("#editor").focus();
			selection.removeAllRanges();//清除range
			selection.addRange(range);//设置range
			return false;
        }
    });
}

function initPreview(){
    $('.zwf-news-change').change(function(){
        $('#news-template-type-035005').hide();
        $('#news-template-type-035010').hide();
        $('#news-template-type-035015').hide();
        $('#news-template-type-035020').hide();
		$('#news-template-type-035030').hide();
		var editor = $("#editor").text();
		$("#editor").empty().append(editor);
		$('.zwf-img-preview').removeClass('zwf-border-radius');
		$('.zwf-img-preview').removeClass('border-radius');
		$('.zwf-news-ys').hide();
		$('#UpLoader').show();
		$('.zl-line-img').show();
		$('#uploadPicOld').show();
		var time = new Date();
        var src = $('#showLogo').attr('src')+'?d='+time+'';
        if (src == null || src == 'images/gzh_wx.jpg'||src == 'images/gzh_wx.jpg?d='+time+'') src = "article/images/imgPhoto.png";
		if ($('#article-module').val() == '035015') {
            $('#news-template-type-035015').show();
            $('.zl-new-title').empty().append($('#article_title').val());
            $('.zwf-dept-name').empty().append($('#dept_name').val());
            $('#school_name').append(localStorage.getItem('school_name'));
            $('.zl-content-text').empty().append($('#editor').html());
			$('#uploadPicOld').hide();
        } else if ($('#article-module').val() == '035010'){
            $('#news-template-type-035010').show();
            $('.zl-new-title').empty().append($('#article_title').val());
            $('.zwf-dept-name').empty().append($('#dept_name').val());
            $('.zl-content-text').empty().append($('#editor').html());
			$('#uploadPicOld').hide();
		}  else if ($('#article-module').val() == '035020'){
            $('#news-template-type-035020').show();
            $('.zl-new-title').empty().append($('#article_title').val());
            $('.zwf-dept-name').empty().append($('#dept_name').val());
            $('.zl-content-img').attr('src',src);
            $('.zl-content-text').empty().append($('#editor').html());
		} else if ($('#article-module').val() == '035030'){
			$('.zwf-img-preview').addClass('border-radius');
			$('#news-template-type-035030').show();
			$('.zl-new-title').empty().append($('#article_title').val());
			$('.zwf-dept-name').empty().append($('#dept_name').val());
			$('.zl-content-img').attr('src',src);
			$('.zl-content-text').empty().append($('#editor').html());
			$('.zwf-img-preview').addClass('border-radius');
		} else {
            $('#news-template-type-035005').show();
            $('.zl-new-title').empty().append($('#article_title').val());
            $('.zwf-dept-name').empty().append($('#dept_name').val());
            $('.zl-content-img').attr('src',src);
            $('.zl-content-text').empty().append($('#editor').html());
			if ($('#article-module').val()=='035035') {
				$('.zwf-img-preview').addClass('zwf-border-radius');
				$('#UpLoader').hide();
				$('.zl-line-img').hide();
			}
			if ($('#article-module').val() == '035025'){
				$("#editor").empty().append(editor_html);
				$('.zl-content-text').empty().append(editor_html);
				$("#editor").unbind('paste');
				$('.zwf-news-ys').show();
			}
		}
	});
}

//加载文章模板分类列表
function loadModuleClassifyList(){
	$.myajax({
		url:'dictAction/getDictionary',
		data:{dict_group:"035"},
		datatype:'json',
		success:function(data){
			$('#article-module').empty();
			var result = data.result.data;
			for(var i in result){
				var item = result[i];
				$('#article-module').append('<option value="'+item.dict_code+'">'+item.dict_value+'</option>');
			}
		}
	});
}

//加载文章模板分类列表
function loadArticleClassifyList(){
    $('#article-classify').show();
    $.myajax({
        url:'dictAction/getNewsDictionary',
        data:{dict_group:dict_group},
        datatype:'json',
        success:function(data){
            $('#article-classify').show();
            $('#article-classify').empty();
            $('#article-classify').append('<option>请选择发布栏目</option>');
            var result = data.result.data;
			for (var j in result){
				var obj = eval(result[j].news_code_list);
				for( var i = 0; i < obj.length; i++) {
					$('#article-classify').append('<option value="' + obj[i].dict_code + '">' + obj[i].dict_value + '</option>');
				}
				bindSelectClickEvent();
			}
        }
    });
}

function bindSelectClickEvent() {
	$('#article-classify').unbind('click').on('click',function () {
		$('.zl-title-new').empty().text($('#article-classify option:selected').text());
	});
}

function initSelectBtuClickEvent()	{
	$('#article-module').unbind('click').on('click',function(){
		var value = $('#article-module').val();
		if (value == "035010" || value == "035015"){
			$('.zwf-news-img').hide();
		} else $('.zwf-news-img').show();
	});
}

//绑定文件上传功能

function bindUploadLogoClickEvent(){
    $('#uploadPicLogin').unbind("click").on('click',function(){
        if ($('#showLogoOld').attr('src')=='images/gzh_wx.jpg'||$('#showLogoOld').attr('src')=='article/images/imgPhoto.png') {bindMessage("请选择封面图片......"); return;}
        $('#uploadPicOld').append('<button id="cancelCut" data-cancel_cut="false" type="button" class="btn btn-danger btn-outline btn-upload" style="margin-left: 3px;">取消裁剪</button>');
        $('#uploadPicOld').append('<button id="confirmCut" data-confirm_cut="true" type="button" class="btn btn-primary btn-outline btn-upload" style="margin-left: 3px;">确认裁剪</button>');
		$('#uploadPicLogin').hide();
        $('#selectedPhoto').hide();
        $('.cropper-container').show();
        $('#showPhoto').hide();
        var printscreen = $('#zwf-code-name').attr('data-printscreen');
        $('#showLogoOld').cropper('destroy');
        initCutPhoto(printscreen,$('#showLogoOld'));
        bindConfirmCutPhotoClick();
        bindCancelCutPhotoClick()
	});
}

function bindCancelCutPhotoClick() {
    $('#cancelCut').unbind('click').on('click',function () {
        $('#cancelCut').remove();
        $('#confirmCut').remove();
        $('#uploadPicLogin').show();
        $('.cropper-container').hide();
        $('#showPhoto').show();
        $('#selectedPhoto').show();
    });
}

function bindConfirmCutPhotoClick() {
    $('#confirmCut').unbind('click').on('click',function () {
        var cut_data = '{width:'+$('.cropper-crop-box').css('width')+',height:'+$('.cropper-crop-box').css('height')+',left:'+$('.cropper-crop-box').css('left')+',top:'+$('.cropper-crop-box').css('top')+'}';
        var file_name = $('#showLogoOld').attr('src').substring($('#showLogoOld').attr('src').indexOf('news/')+5);
        var image = new Image();
        image.src = $('#showLogoOld').attr('src');
        image.onload = function () {
            var resize_rate = $(".cropper-canvas").width()/image.width;
            var time = $(".cropper-canvas").width() * resize_rate;
            $.myajax({
                url: image_cut_action,//file_upload_action
                dataType : "JSON",
                data: {cut_data:cut_data,module_code:"009018",file_name:file_name,resize_rate:resize_rate,school_id:localStorage.getItem("school_id")},
                success: function (result) {
                    var item = result.result.data;
                    $('.cropper-container').hide();
                    $('#showPhoto').attr('src',$("#showLogoOld").attr("src")+'?d='+time+'');
                    $('#cancelCut').remove();
                    $('#confirmCut').remove();
                    $('#uploadPicLogin').show();
                    $('#showPhoto').show();
                    $('#showLogoOld').attr('src',$("#showLogoOld").attr("src")+'?d='+time+'');
                    $('.cropper-container img').attr('src',$('#showPhoto').attr('src')+'?d='+time+'');
                    $('#selectedPhoto').show();
					$('.zwf-img-preview img').attr('src',$('#showPhoto').attr('src')+'?d='+time+'');
                    if ($('#article-module').val()=='035035'||$('#article-module').val()=='035030') $('.zwf-img-preview').addClass('border-radius');
                }
            });
        }
    });
}

//触发按钮点击事件
function bindUploadPicClickEvent() {
	$('#uploadPic').unbind('click').on('click', function(){
		$('#uploadLogo').click();//触发隐藏的from事件
		bindSelectedPhoto();
	});
}

//触发按钮点击事件
function bindUploadPicOldClickEvent() {
	$('#selectedPhoto').unbind('click').on('click', function(){
		$('#uploadLogo').click();//触发隐藏的from事件，
		bindSelectedPhoto();
	});
}

function bindEditorPicBtnClickEvent() {
	$("#picturebtn").unbind('click').on("click", function() {
		$("#upload_pic").click();
	});
	$('#upload_pic').unbind("change").on('change',function(){
		$("#Form").submit();
	});
	$("#Form").ajaxForm({
		dataType: "json",
		success: function(data){
			var item = data.result.data;
			for(var i in item){
                var file_url = item[0].file_resize_url == null || item[0].file_resize_url == ''?item[0].file_url:item[0].file_resize_url;
				$('#editor').append('<img src="'+file_url+'"><div><br></div>&nbsp;&nbsp;&nbsp;&nbsp;');
				$('.zl-content-text').empty().append($('#editor').html());
				editor_html = $('#editor').html();
				$('#upload_pic').val('');
				set_focus();
			}
		}
	});
}

//设置光标到文本末尾
function set_focus() {
	var el=document.getElementById('editor');
	el.focus();//重点:想要设置光标到文本末尾必须先聚焦
	if($.support.msie) {
		var range = document.selection.createRange();
		this.last = range;
		range.moveToElementText(el);
		range.select();
		document.selection.empty(); //取消选中
	} else {
		var range = document.createRange();
		range.selectNodeContents(el);
		range.collapse(false);
		var sel = window.getSelection();
		sel.removeAllRanges();
		sel.addRange(range);
	}
}
//添加文章
function bindAddButtonClickEvent(){
	$('#addButton').unbind('click').on('click', function(){
		var dict_group = $('#zwf-code-name').attr('data-news_group');
		var news_code = $('#zwf-code-name').attr('data-news_code');
		var title = $('#article_title').val();
		var content = $('#editor').html();
		var content_text = $('#editor').text().trim();
		var dept_name = $('#dept_name').val();
		//判断用户是否上传图片.如果没上传，取消默认图片，将文本的第一张图片上传作为封面。
		var is_main = "0";
		var main_pic_url;
		var itemlist = new Array();
		var template_type = $('#article-module').val();
		var deploy_date = $('#deploy_date').val();
		var file_list = "";
		if(title == '') return bindMessage("标题不能为空！");
		if (news_code == ''||news_code == null||dict_group==''||dict_group == null) return bindMessage("请选择栏目分类......");
		if(dept_name == '') return bindMessage("发件人不能为空！");
		if ($('#showLogoOld').attr('src') == 'article/images/imgPhoto.png') main_pic_url = "";
		else main_pic_url =$('#showLogoOld').attr('src');
		if (template_type=="035010"||template_type=="035015"){
			main_pic_url = '';
		} else {
			if (main_pic_url == null || main_pic_url == '' || main_pic_url == undefined) return bindMessage("图片不能为空,如果不需要图片,请选择模板类型......");
			else file_list = '[{"file_url":"' + main_pic_url + '"}]';
		}
		if(content_text == ""|| content_text==null||content_text == undefined) return bindMessage("内容不能为空！");
		//以下将所有子项存入item_list begin
		var itemVO = {};
		itemVO.content=content;
		itemVO.content_text=content_text;
		itemlist.push(itemVO);
		//end
		$.myajax({
			url:'newsAction/addNews',
			type: 'POST',
			data:{news_code:news_code, title:title, content:content, is_main:is_main,main_pic_url:main_pic_url,
				  content_text:content_text, dept_name:dept_name,module_code:module_code, deploy_date:deploy_date,dict_group:dict_group,template_type:template_type,item_list:JSON.stringify(itemlist),file_list:file_list},
			datatype:'json',
			success:function(data){
				bindMessage("新闻添加成功......");
				//清除添加框内容
				initDeployPage();
			}
		});
	});
}

function initDeployPage(){
	editor_html = '';
	$('#article_title').val('');
	$('#showLogo').val('');
	$('#dept_name').val('');
	$('#deploy_date').val('');
	$('#article_title option').eq(0).prop('selected', true);
	$('#editor').empty();
	$('#showPhoto').attr('src','article/images/imgPhoto.png');
	$('#showLogoOld').attr('src','article/images/imgPhoto.png');
	$('#hiddenDiv').show();
	$('.zl-new-title').empty();
	$('.zwf-dept-name').empty();
	$('.zl-content-img').attr('src','article/images/imgPhoto.png');
	$('.zl-content-text').empty();
}

//重写文本编辑器的文件上传
function sendFile(file, el) {
    var formData = new FormData();
    formData.append("photo", file);
    formData.append("module_code", "009018");
	formData.append("school_id", localStorage.getItem("school_id"));
    $.myajax({
    	 type: "POST",
         url: file_upload_action,
         cache: false,
         dataType : "JSON",
         data: formData,
         contentType: false, // 告诉jQuery不要去设置Content-Type请求头
         processData: false, // 告诉jQuery不要去处理发送的数据
        success: function (result) {
        	var item = result.result.data;
        	for(var i in item){
                var file_url = item[0].file_resize_url == null || item[0].file_resize_url == ''?item[0].file_url:item[0].file_resize_url;
        		var imageNode = document.createElement('img');
            	imageNode.setAttribute('src', file_url);
            	$(el).summernote('insertNode', imageNode);
        	}
        }
    });
}

function bindIsSelectClickEvent() {
	$('.zwf-news-div').unbind('click').on('click',function () {
		var none = $('#btnSelect').attr('data-is-hide');
		var width = $('.zwf-news-div').css('width');
		var width1 = (width.split('px')[0] -3)+'px';
		var width2 = (width.split('px')[0] -22)+'px';
		$('.updata-width2').css('width',width2);
		$('.updata-width').css('width',width1);
		if (none == 'none') {
			$('#news-code-list').show();
			$('#btnSelect').attr('data-is-hide','');
			$('.zwf-news-div').css('border','1px solid #57cebf');
		} else {
			$('#news-code-list').hide();
			$('#btnSelect').attr('data-is-hide','none');
			$('.zwf-news-div').css('border','1px solid #e5e5e5');
		}
	});
}

function bindDivIsShowClickEvent() {
	$('.zwf-news-select').unbind('click').on('click',function () {
		var none = $(this).attr('data-is-hide');
		var news_group = $(this).attr('data-news_group');
		if (none == 'none') {
			$('.zwf-news-group'+news_group+'').show();
			$('.zwf-news-select').attr('data-is-hide','');
		} else {
			$('.zwf-news-group'+news_group+'').hide();
			$('.zwf-news-select').attr('data-is-hide','none');
		}
	});
}

function bindGetCodeNameClickEvent() {
	$('.zwf-news-code').unbind('click').on('click',function () {
		if (localStorage.getItem('NEWS_TEMPLATE_ON')=='TRUE') $('#selectedPhoto').attr('title','请您上传封面图片').text('请您上传封面图片');
		else $('#uploadPic').attr('title','请您上传封面图片').text('请您上传封面图片');
		var news_code = $(this).attr('data-news_code');
		var news_group = $(this).parent().attr('data-news_group');
		var code_name = $(this).text();
		$('#zwf-code-name').attr('data-news_code',news_code);
		$('#zwf-code-name').attr('data-news_group',news_group);
		$('#zwf-code-name').empty().text(code_name);
		$('#zwf-code-name').trigger('click');
		$('.zl-title-new').empty().text(code_name);
        if ($(this).attr('data-printscreen') == '2'){
            $('#zwf-code-name').attr('data-printscreen',2);
			if ($("#showLogoOld").hasClass('cropper-hidden')) $('#showLogoOld').cropper('setAspectRatio',2);
        } else {
            $('#zwf-code-name').attr('data-printscreen',1);
			if ($("#showLogoOld").hasClass('cropper-hidden')) $('#showLogoOld').cropper('setAspectRatio',1);
        }
	});
}

//加载文章分类列表
function loadDictSchoolList(){
	$('.zwf-news-div').show();
	$.myajax({
		url:'dictAction/getNewsDictionary',
		data:{dict_group:dict_group},
		datatype:'json',
		success:function(data){
            var div = '';
            $('.zwf-news-div').show();
			$('#news-code-list').empty();
			var result = data.result.data;
			for(var i in result){
				var item = result[i];
				var width = $('.zwf-news-div').css('width');
				var width1 = (width.split('px')[0] -3)+'px';
				var width2 = (width.split('px')[0] -22)+'px';
				div += '<div style="width: '+width1+';" class="updata-width"> <div class="zwf-news-select" data-is-hide="none"  data-news_group="'+item.dict_code+'"><span ><img style="margin-top: 2px; float: left;" src="article/images/selectArrow.png"/>'
					+'</span><span class="sb-xf updata-width2" style="display: block;width: '+width2+';height: 20px; margin-left: 20px;">'+item.dict_value+'</span></div><div style="display: none;" data-news_group="'+item.dict_code+'" class="zwf-news-group'+item.dict_code+'">';
				var obj = eval(item.news_code_list);
				for( var i = 0; i < obj.length; i++){
                    var cssVO = eval('('+obj[i].css_list+')');
					div += '<div data-news_code="'+obj[i].dict_code+'" data-printscreen="'+cssVO.printscreen+'" class="zwf-news-code zwf-news-code-css sb-xf">'+obj[i].dict_value+'</div>';
				}
				div += '</div></div>';
			}
            $('#news-code-list').empty().append(div);
			bindIsSelectClickEvent();
			bindDivIsShowClickEvent();
			bindGetCodeNameClickEvent();
			bindMouseSuspend();
		}
	});
}

function  bindMouseSuspend() {
		$(".sb-xf").hover(function(){
			$(this).css('background','#63B8FF');
		},function(){
			$(this).css('background','');
		});
}

//绑定文件上传功能
function bindSelectedPhoto(){
    $('#uploadLogo').unbind('change').change(function() {
		var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
		if (mb > 1) bindMessage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
		if ($('#uploadPicLogin').text() == null|| $('#uploadPicLogin').text()==''||$('#uploadPicLogin').text()==undefined) $('#uploadPicOld').append('<button id="uploadPicLogin" data-is_cut="true" type="button" class="btn btn-outline btn-success btn-upload">裁剪图片</button>');
		var formData = new FormData(document.getElementById("form-file"));
		if ($('#uploadLogo').val()==null||$('#uploadLogo').val()==''||$('#uploadLogo').val()==undefined) return;
		formData.append("module_code", "009018");
		formData.append("school_id", localStorage.getItem("school_id"));
		$.myajax({
			type: "POST",
			url: file_upload_action,//file_upload_action
			cache: false,
			dataType : "JSON",
			data: formData,
			contentType: false, // 告诉jQuery不要去设置Content-Type请求头
			processData: false, // 告诉jQuery不要去处理发送的数据
			success: function (result) {
				var item = result.result.data;
				var file_url = item[0].file_resize_url == null || item[0].file_resize_url == ''?item[0].file_url:item[0].file_resize_url;
				$("#showLogoOld").attr("src", file_url); //将图片路径存入src中，显示出图片
				$('#showLogo').attr('src', file_url);
				$('#hiddenDiv').hide();
                $('#showPhoto').attr('src', file_url);
				if ($("#showLogoOld").hasClass('cropper-hidden'))  $('#showPhoto').show();
				else $('#showPhoto').hide();
                $('.cropper-canvas img').attr('src',file_url);
                $('.cropper-view-box img').attr('src',file_url);
                $('.zwf-img-preview img').attr('src',file_url);
				$('#uploadLogo').val('');
			}
		});
		bindUploadLogoClickEvent();
    });
}

function isNotEmpty(str) {
    return str !== null && str !== ''&& str !== ""&& str !== '""'&& str !== undefined && str !== '[]'&& str !== '{}'&&str.length !=0;
}

function isEmpty(str) {
    return str === null || str === ''|| str === ""|| str === '""'|| str === undefined || str === '[]'||str === '{}'|| str.length ==0;
}

function bindMessage(content) {
    if (isEmpty(content)) return;
	toastr.options = {
		"closeButton": true,
		"debug": false,
		"progressBar": true,
		"positionClass": "toast-top-center",
		"onclick": null,
		"showDuration": "400",
		"hideDuration": "1000",
		"timeOut": "1500",
		"extendedTimeOut": "1000",
		"showEasing": "swing",
		"hideEasing": "linear",
		"showMethod": "fadeIn",
		"hideMethod": "fadeOut"
	};
	toastr.info(content,"提示");
}

