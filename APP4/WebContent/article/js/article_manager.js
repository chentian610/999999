var storage = window.localStorage;
var currentPage=1;
var limit=10;
var file_upload_action = localStorage.getItem('file_upload_action');
var school_news_action = localStorage.getItem('NEWS_TEMPLATE_ON');
var image_cut_action = localStorage.getItem('image_cut_action');

//加载文章分类列表
function bindArticleDictList(){
	$('#article-dict').on('change',function () {
		var dict_group = $('#article-dict').val();
		$.myajax({
			url:'dictAction/getNewsDictSchoolList',
			data:{dict_group:dict_group},
			datatype:'json',
			success:function(data){
				$('#article-classify').empty();
				$('#article-classify').append('<option value="'+dict_group+'">请选择栏目分类</option>');
				var result = data.result.data;
				for(var i in result){
					var item = result[i];
					$('#article-classify').append('<option value="'+item.dict_code+'">'+item.dict_value+'</option>');
				}
				bindClassifyChangeEvent();
			}
		});
	});
}

//加载文章分类列表
function loadArticleClassifyList(){
		var news_group = dict_group;
		if(news_group.length<4){news_group='';}
		$.myajax({
			url:'dictAction/getNewsDictSchoolList',
			data:{dict_group:news_group},
			datatype:'json',
			success:function(data){
				$('#article-classify').empty();
				$('#article-classify').append('<option value="'+dict_group+'">请选择栏目分类</option>');
				var result = data.result.data;
				for(var i in result){
					var item = result[i];
					$('#article-classify').append('<option value="'+item.dict_code+'">'+item.dict_value+'</option>');
				}
				bindClassifyChangeEvent();
			}
		});
}

//加载文章分类列表
function loadArticleDictSchoolList(){
	$.myajax({
		url:'dictAction/getNewsDictionary',
		data:{dict_group:dict_group},
		datatype:'json',
		success:function(data){
			$('#article-dict').empty();
			var result = data.result.data;
			for(var i in result){
				var item = result[i];
				$('#article-dict').append('<option value="'+item.dict_code+'">'+item.dict_value+'</option>');
			}
			bindDictSchoolChangeEvent();
			loadContent( result[0].dict_code);
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
				$('.zwf-news-module').show();
			}
		}
	});
}

function bindModuleChange() {
	$('#article-module').on('click',function(){
		var value = $('#article-module').val();
		if (value == "035010" || value == "035015"){
			$('.zwf-news-img').hide();
		} else $('.zwf-news-img').show();
		if ($('#article-module').val() == '035025'){
			$('.zwf-news-ys').show();
		}
	});
}

//文章分类搜索
function bindClassifyChangeEvent(){
	$('#article-classify').on('change',function(){
		currentPage = 1;
		loadContent($('#article-dict').val(),$(this).val(), $('#article-keywords').val(), $('#article-date').val());
	});
}
//文章分类搜索
function bindDictSchoolChangeEvent(){
	$('#article-dict').on('change',function(){
		currentPage = 1;
		loadContent($(this).val(),'', $('#article-keywords').val(), $('#article-date').val());
	});
}
//关键字搜索
function bindSearchButtonClickEvent(){
	$('#search-button').on('click', function(){
		loadContent($('#article-dict').val(),$('#article-classify').val(), $('#article-keywords').val(), $('#article-date').val());
	});
}
//日期搜索
function searchByDate(date){
	loadContent($('#article-dict').val(),$('#article-classify').val(), $('#article-keywords').val(), date);
}
//加载内容列表
function loadContent(dict_code,news_code,search,deploy_date){
	$.myajax({
		url:'newsAction/getNewsList',
		data:{news_code:news_code,dict_group:dict_code, search:search, deploy_date:deploy_date,start_id:(currentPage-1)*limit,limit:limit,page:currentPage},
		datatype:'json',
		type:'post',
		success:function(data){
			var result = data.result;
            var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
            addToWeb(data);
            if (pageCount<2) {
            	$("#page_pagintor").hide();
            	return;
            }
            var options = {
                bootstrapMajorVersion: 3, //版本
                currentPage: currentPage, //当前页数
                totalPages: pageCount, //总页数
                alignment:"center",
                itemTexts: function (type, page, current) {
                    switch (type) {
                        case "first":
                            return "首页";
                        case "prev":
                            return "上一页";
                        case "next":
                            return "下一页";
                        case "last":
                            return "末页";
                        case "page":
                            return page;
                    }
                },//点击事件，用于通过Ajax来刷新整个list列表
                onPageClicked: function (event, originalEvent, type, page) {
               	currentPage=page;
               	loadContent(dict_code,news_code);
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
            }
	});
}

//加载文章列表
function addToWeb(data){
	$('#article-list').empty();
	var result = data.result.data;
	for(var i in result){
		var item = result[i];
		addNewsToWed(item);
	}
	bindEditButtonClickEvent();
	bindDeleteButtonClickEvent();
}


function addNewsToWed(newsVO){
	$('#article-list').append('<li value="'+newsVO.news_id+'">' +
 		'<h3>' + newsVO.title + '</h3>' +
 		'<p><span>'+newsVO.deploy_date+'</span><span>'+newsVO.dept_name+'</span></p>'+
 		'<div class="art-opt-btn">'+
    		'<button type="button" class="btn btn-primary btn-outline btn-sm edit" data-toggle="modal" data-target="#edit-modal" style="">编辑</button>'+
    		'<button type="button" class="btn btn-danger btn-outline btn-sm delete">删除</button>'+
 		'</div></li>'
	);
}

//添加图片的隐藏按钮被触发.向后台发送请求并返回数据
function bindUploadPicClickEventX(){
	$('.cut-photo').on('click',function(){
        var cut_data = '{width:'+$('.cropper-crop-box').css('width')+',height:'+$('.cropper-crop-box').css('height')+',left:'+$('.cropper-crop-box').css('left')+',top:'+$('.cropper-crop-box').css('top')+'}';
        var file_name = $('#showLogo').attr('src').substring($('#showLogo').attr('src').indexOf('news/')+5);
        var image = new Image();
        image.src = $('#showLogo').attr('src');
        image.onload = function () {
            var resize_rate = $(".cropper-canvas").width() / image.width;
            var time = $(".cropper-canvas").width() * resize_rate;
            $.myajax({
                type: "POST",
                url: image_cut_action,
                dataType: "JSON",
                data: {cut_data: cut_data, module_code: "009018", file_name: file_name, resize_rate: resize_rate,school_id:localStorage.getItem("school_id")},
                success: function (result) {
                    var item = result.result.data;
                    $('#showLogo').attr('src', $('#showLogo').attr('src') + '?d=' + time + '');//给main_pic_url赋值
                    $('#showLogo').attr('data-file_src',$('#showLogo').attr('src') + '?d=' + time + '');
                    var printscreen = $('#zwf-code-name').attr('data-printscreen');
                    $('#showLogo').cropper('destroy');
                    initCutPhoto($('#showLogo').attr('dara-printscreen'), $('#showLogo'));
                }
            });
        }
	});
}

//绑定文件上传功能
function bindSelectedPhoto(){
    $('#uploadLogo').unbind("change").on('change',function() {
		var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
		if (mb > 1) bindMessage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
		var formData = new FormData(document.getElementById("form-file"));
		if ($('#uploadLogo').val()==null||$('#uploadLogo').val()==''||$('#uploadLogo').val()==undefined) return;
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
					if (item[0].file_url==null||item[0].file_url==''||item[0].file_url==undefined) return;
					$('#showLogo').attr('src', item[0].file_url);
					$('#showLogo').cropper('destroy');
					initCutPhoto( $('#showLogo').attr('dara-printscreen'),$('#showLogo'));
					$('.cropper-canvas img').attr('src',item[0].file_url);
					$('.cropper-view-box img').attr('src',item[0].file_url);
				}
			}
		});

    });
}

//触发添加图片的隐藏按钮
function bindUploadLogoClickEventY(){
	$('#uploadPic').on('click', function(){//触发点击事件.
		$('#uploadLogo').click();          //触发logo的点击事件
	});
}
//编辑文章
function bindEditButtonClickEvent(){
	$('#article-list').find('button').filter('.edit').unbind(
			'click').on('click', function() {
		var _li = $(this).parent().parent();
		$.myajax({
			url:'newsAction/getNews',
			data:{news_id: _li.val()},
			datatype:'json',
			success:function(data){
				var newsVO = data.result.data;				
				addArticleToEditBox(newsVO);
				bindEditSaveButtonClickEvent(_li);
			}
		});
	});
}
//把指定的文章添加到编辑框中
function addArticleToEditBox(newsVO){
	$('#article_title').empty().val(newsVO.title);//标题
	$('#editor').empty().append(newsVO.content);//内容
	$('#dept_name').empty().val(newsVO.dept_name);//发布部门
	$('#deploy_date').empty().val(newsVO.deploy_date);//发布日期
	$('#showLogo').attr('src',newsVO.main_pic_url);//发布封面
    var cssVO = eval('('+newsVO.css_list+')');
    var printscreen = cssVO!=null||cssVO!=undefined||cssVO != "" ? cssVO.printscreen:1;
    $('#showLogo').cropper('destroy');
    initCutPhoto(printscreen,$('#showLogo'));
    $('#showLogo').attr('dara-printscreen',printscreen);

	//设置编辑框栏目项
	var _classifiy = $('#article-classify-edit');
	_classifiy.empty().html($('#article-classify').children().clone());
	var index = 0;
	_classifiy.find('option').each(function(){
		if($(this).val() == newsVO.news_code){
			$(this).prop('selected',true);
			return false;
		}
	});
	// $('#article-classify-edit').val(newsVO.news_code);
	$('#article-module').val(newsVO.template_type);


}

//保存编辑
function bindEditSaveButtonClickEvent(_li){
	$('#edit-save').unbind('click').on('click', function(){
		var news_id = _li.val();
		var news_code = $('#article-classify-edit').val();
		var title = $('#article_title').val();
		var content = $('#editor').html();
		var is_main = "1";
		var main_pic_url;
        var content_text = $('#editor').text().trim();
		var dept_name = $('#dept_name').val();
		var deploy_date = $('#deploy_date').val();
		var itemlist = new Array();
		//以下将所有子项存入item_list begin
		var itemVO = {};
		itemVO.content=content;
		itemVO.content_text=content_text;
		itemlist.push(itemVO);
		//end
		var file_list
		var template_type = $('#article-module').val();
		if (template_type=='035010'||template_type=='035015'){
			file_list = "";
			main_pic_url = "";
		} else{
			main_pic_url =$('#showLogo').attr('src');
			file_list = '[{"file_url":"'+main_pic_url+'"}]';
		}
		if(title == ''){
			bindMessage('标题不能为空....'); return;
		}
		$.myajax({
			url:'newsAction/updateNews',
			type: 'POST',
			data:{news_id:news_id, news_code:news_code, title:title, content:content, is_main:is_main, main_pic_url:main_pic_url,
				  content_text:content_text, dept_name:dept_name, deploy_date:deploy_date,dict_group:dict_group,item_list:JSON.stringify(itemlist),file_list:file_list,template_type:template_type},
			datatype:'json',
			success:function(data){
				swal({title : "编辑成功！"});
				_li.find('h3').text(title);
				_li.find('span').first().text(deploy_date);
				_li.find('span').eq(1).text(dept_name);
                $('#showLogo').cropper('destroy');
				$('#edit-close').click();
			}
		});  
	});
}

//删除文章
function bindDeleteButtonClickEvent(){
	$('#article-list').find('button').filter('.delete').unbind(
			'click').on('click', function() {
		var _li = $(this).parent().parent();
		swal({
			title : "您确定要删除这篇文章吗？",
			text : "删除后将无法恢复，请谨慎操作！",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'newsAction/deleteNews',
				data:{news_id: _li.val(),dict_group:dict_group},
				datatype:'json',
				success:function(data){
					_li.remove();
					swal("删除成功！", "您已经永久删除了这篇文章!", "success");
				}
			});
		});
	});
}
//文本编辑器的上传文件至服务器方法
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
        		var imageNode = document.createElement('img');
            	imageNode.setAttribute('src', item[i].file_url);
            	$(el).summernote('insertNode', imageNode);
        	}
        }
    });
}

function bindEditorPicBtnClickEvent() {
	$("#picturebtn").on("click", function() {
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
				$('#editor').append('<img src="'+item[i].file_url+'">');
			}
		} 
	});
}

function bindMessage(content) {
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

// function getPhoto(file) {
//     var url = null;
//     if (window.createObjectURL != undefined) {
//         url = window.createObjectURL(file)
//     } else if (window.URL != undefined) {
//         url = window.URL.createObjectURL(file)
//     } else if (window.webkitURL != undefined) {
//         url = window.webkitURL.createObjectURL(file)
//     }
//     return url;
// }
//
// function cacheExternalImage(url){
//     var img = new Image,
//         src = url,
//         cvs = document.createElement('canvas'),
//         ctx = cvs.getContext('2d');
//     img.crossOrigin = "Anonymous";
//     img.onload = function() {
//         //ctx.drawImage( img, 0, 0 );
//     }
//     img.src = src;
//     if ( img.complete || img.complete === undefined ) {
//         img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
//         img.src = src;
//     }
//     return img.src;
// }