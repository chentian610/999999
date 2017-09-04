var store = window.localStorage;
var currentPage=1;
var limit=8;

function initPaginator() {
     $.myajax({
    	 url:"schoolAction/getSchoolApplyList",
         datatype: 'json',
         data: {start_id:(currentPage-1)*limit,limit:limit,page:currentPage},
         success: function (data) {
        	 var result = data.result;
        	 addToWeb(data);
             var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
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
                	 initPaginator();
                 }
             };
             $("#page_pagintor").bootstrapPaginator(options);
             $("#page_pagintor").show();
             }
         });
 };



function addToWeb(data){
	$('#school-apply-list').empty();
	var result = data.result.data;
	for(var i in result){
		var item = result[i];
		$('#school-apply-list').append('<div id="'+item.school_id+'" class="col-xs-6 col-sm-3 placeholder">\
						                <a href="javascript:void(0);">\
						                <img src="'+item.organize_pic_url+'" alt="Generic placeholder thumbnail" width=200 height=200>\
							            </a>\
							            <h4>'+item.school_name+'</h4>\
							            <span class="text-muted">申请日期：'+getDateStr(item.create_date,'day')+'</span>\
							        </div>');
	}
	bindClickEvent();
}
//点击跳转详情页
function bindClickEvent(){
	$('#school-apply-list').find('a').unbind('click').on('click',function(){
		var school_id = $(this).parents('div').attr("id");
		window.location.href = 'audit/m/auditDetail.html?school_id='+school_id;
	});
}