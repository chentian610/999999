var storage = window.localStorage;
var currentPage = 1;
var limit = 10;
var file_upload_action = localStorage.getItem('file_upload_action');

//关键信息搜索
function bindSearchButtonClickEvent(){
	$('#search-button').on('click', function(){
		loadContent($('#FameName').val(),$('#fame-keywords').val(),$('#graduationDate').val());
	});
}
//用回车键 触发搜索按钮.==13代表 键盘Q起  第13个按键
$("body").keyup(function () {  
    if (event.which == 13){  
        $("#search-button").trigger("click");  
    }  
}); 

// 加载内容列表
function loadContent(name, search, birthday) {
	$.myajax({
		url : 'fameAction/getFameListForWeb',
		data : {
			name:name,
			search : search,
			birthday : birthday,
			start_id : (currentPage - 1) * limit,
			limit : limit,
			page : currentPage
		},
		datatype : 'json',
		type : 'post',
		success : function(data) {		
			var result = data.result;			
			var pageCount = Math.ceil(result.total / limit); // 取到pageCount的值(把返回数据转成object类型)
			addToWeb(data);
			if (pageCount < 2) {
				$("#page_pagintor").hide();
				return;
			}
			var options = {
				bootstrapMajorVersion : 3, // 版本
				currentPage : currentPage, // 当前页数
				totalPages : pageCount, // 总页数
				alignment : "center",
				itemTexts : function(type, page, current) {
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
				},// 点击事件，用于通过Ajax来刷新整个list列表
				onPageClicked : function(event, originalEvent, type, page) {
					currentPage = page;
					loadContent(name);
				}
			};
			$("#page_pagintor").bootstrapPaginator(options);
			$("#page_pagintor").show();
		}
	});
}

// 加载文章列表
function addToWeb(data) {
	$('#fame-list').empty();
	var result = data.result.data;
	for ( var i in result) {
		var item = result[i];
		addFameToWed(item);
	}
	bindEditButtonClickEvent();
	bindDeleteButtonClickEvent();
}

function addFameToWed(FameVO) {
	$('#fame-list')
			.append(
					'<li value="'
							+ FameVO.fame_id
							+ '">'
							+ '<h3>'
							+ FameVO.name
							+ '</h3>'
							+ '<p><span>'
							+ FameVO.graduation_date
							+ '</span><span>'
							+ '<div class="art-opt-btn">'
							+ '<button type="button" class="btn btn-primary btn-sm edit" data-toggle="modal" data-target="#edit-modal">编辑</button>'
							+ '<button type="button" class="btn btn-primary btn-sm delete">删除</button>'
							+ '</div></li>');
}

// 添加图片的隐藏按钮被触发.向后台发送请求并返回数据
function bindUploadPicClickEventX() {
	$('#uploadFameLogo').change(function() {
		var formData = new FormData(document.getElementById("form-file"));
		formData.append("module_code", "009031");
		$.myajax({
			url : file_upload_action,
			cache : false,
			data : formData,
			contentType : false, // 告诉jQuery不要去设置Content-Type请求头
			processData : false, // 告诉jQuery不要去处理发送的数据
			type : 'POST',
			success : function(result) {
				var item = result.result.data;
				$('#showFameLogo').attr('src', item[0].file_url);// 给main_pic_url赋值
			}
		});
	});
}
// 触发添加图片的隐藏按钮
function bindUploadLogoClickEventY() {
	$('#uploadFamePic').on('click', function() {// 触发点击事件.
		$('#uploadFameLogo').click(); // 触发logo的点击事件
	});
}

// 编辑文章
function bindEditButtonClickEvent() {
	$('#fame-list').find('button').filter('.edit').unbind('click').on('click',
			function() {
				var _li = $(this).parent().parent();
				$.myajax({
					url : 'fameAction/getFameList',
					data : {
						fame_id : _li.val()
					},
					datatype : 'json',
					success : function(data) {
						var FameVO = data.result.data;
						addArticleToEditBox(FameVO);
						bindEditSaveButtonClickEvent(_li);
					}
				});
			});
}
/*// 把指定的文章添加到编辑框中
function addArticleToEditBox(FameVO) {
	$('#FameName').empty().val(FameVO.name);// 姓名
	$('#birthday').empty().val(FameVO.birthday);// 生日
	$('#graduationDate').empty().val(FameVO.graduation_date);// 毕业时间
	$('#createBy').empty().val(FameVO.create_by);// 发布日期
	$('#description').empty().val(FameVO.description);// 名人介绍
	 $('#createDate').empty().val(FameVO.create_date);//发布时间
	$('#showFameLogo').attr('src', FameVO.head_url);// 发布封面
}*/

function addArticleToEditBox(FameVO) {	
	$('#FameName').empty().val(FameVO.name);// 姓名
	$('#birthday').empty().val(FameVO.birthday);// 生日
	$('#graduationDate').empty().val(FameVO.graduation_date);// 毕业时间
	$('#createBy').empty().val(FameVO.create_by);// 发布人
	$('#description').empty().val(FameVO.description);// 名人介绍
	 $('#createDate').empty().val(FameVO.create_date);//发布时间
	$('#showFameLogo').empty().attr('src', FameVO.head_url);//发布封面
}

// 保存编辑
function bindEditSaveButtonClickEvent(_li) {
	$('#edit-save').unbind('click').on('click', function() {
		var fame_id = _li.val();
		var name = $('#FameName').val();
		var create_by = $('#createBy').val();
		var head_url = $('#showFameLogo').attr('src');
		var description = $('#description').val();				
		var create_date = $('#createDate').val();
		//验证生日日期格式
		var BIRTHDAY_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/;
		var birthday = $('#birthday').val();
		 if(!birthday.match(BIRTHDAY_FORMAT)){
			 swal({title : "请输入正确生日日期格式!",type : "warning",confirmButtonColor : "#DD6B55",});
				return;
		  }
		 //验证毕业时间日期格式
		 var GRADUTION_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/;
		var graduation_date = $('#graduationDate').val();
		if(!graduation_date.match(GRADUTION_FORMAT)){
			 swal({title : "请输入正确毕业时间格式!",type : "warning",confirmButtonColor : "#DD6B55",});
				return;
		  }	
		if (name == '') {
			alert('姓名不能为空....');
			return;
		}
		$.myajax({
			url : 'fameAction/updateFame',
			type : 'POST',
			data : {
				fame_id : fame_id,
				name : name,
				description : description,
				head_url : head_url,
				create_date : create_date,
				birthday : birthday,
				graduation_date : graduation_date
			},
			datatype : 'json',
			success : function(data) {
				swal({
					title : "编辑成功！"
				});
				_li.find('h3').text(name);
				_li.find('span').first().text(create_date);
				_li.find('span').eq(1).text(create_by);
				$('#edit-close').click();
			}
		});
	});
}
// 删除文章
function bindDeleteButtonClickEvent() {
	$('#fame-list').find('button').filter('.delete').unbind('click').on(
			'click', function() {
				var _li = $(this).parent().parent();
				swal({
					title : "您确定要删除这篇文章吗？",
					text : "删除后将无法恢复，请谨慎操作！",
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "删除",
					closeOnConfirm : false
				}, function() {
					$.myajax({
						url : 'fameAction/deleteFame',
						data : {
							fame_id : _li.val()
						},
						datatype : 'json',
						success : function(data) {
							_li.remove();
							swal("删除成功！", "您已经永久删除了这篇文章!", "success");
						}
					});
				});
			});
}
