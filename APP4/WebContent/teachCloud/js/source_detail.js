var map = {};
var storage = window.localStorage;
var currentPage=1;
var limit=10;
var GradeId ;
var CourseId ;
var VersionId ;
var start_id;

//初始化页面
function initSourcePage(){
	GradeId = $('#gradeList li a[class="now"]').attr('GradeId');
	CourseId = $('#courseList li a[class="now"]').attr('CourseId');
	VersionId = $('#versionList li a[class="now"]').attr('VersionId');
	loadSource(GradeId,CourseId,VersionId);
}


//加载年级
function bindGradeList(){
	$('#gradeList').on('click','li a',function(){
		GradeId =$(this).attr('GradeId');
		loadSource(GradeId,CourseId,VersionId);
	});
}
//加载科目
function bindCourseList(){
	$('#courseList').on('click','li a',function(){
		CourseId = $(this).attr('CourseId');
		loadSource(GradeId,CourseId,VersionId);
	});
}
//加载版本
function bindVersionList(){
	$('#versionList').on('click','li a',function(){
		VersionId = $(this).attr('VersionId');
		loadSource(GradeId,CourseId,VersionId);
	});
}

//加载资源列表
function loadSource(GradeId,CourseId,VersionId){
	$.myajax({
		url:'teachCloudAction/getTeachCloudSource',
		data:{start_id : (currentPage - 1) * limit,limit : limit,page : currentPage ,GradeId:GradeId,CourseId:CourseId,VersionId:VersionId},
		datatype:'json',
		type:'post',
		success:function(data){
//			alert(data);
			$('#source_list').empty();
			var result = data.result;
            var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
            addSourceList(data);
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
               	loadSource(map);
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
            }
	});
}
//加載資源列表
function addSourceList(data){
	var result = data.result.data;
	for(var i in result){
		var item = result[i];
		sourceListToWed(item);
	}
}
function sourceListToWed(item){
	$('#source_list').append('<tr value="'+item.source_id+'">' +
			'<td style="width:30%">'+'<a href="teachCloud/detail.jsp?id='+item.source_id+'" class="resource">'+item.source_name+'</a></td>' +
			'<td style="width:10%">'+
			'<span>'+'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
		'</span>'
			+'</td>' +
			'<td style="width:6%">'+item.browse_count+'</td>' +
			'<td style="width:6%">'+item.praise_count+'</td>' +
			'<td style="width:10%">'+item.resourceSize+'</td>' +
			'<td style="width:20%">'+getDateStr(item.updateTime,"day")+'</td>' +
			'<td style="width:6%">'+'<a href="http://118.180.8.123:8081/detailed!detail1.do?op=download&useraAccount=">'+'<img src="teachCloud/images/icon4.jpg"  class="icon2"/></a>'+'</td>' +
			'<td style="width:6%">'+'<a href="http://118.180.8.123:8081/detailed!detail1.do?op=fav&useraAccount=">'+'<img src="teachCloud/images/icon5.jpg"  class="icon2"/></a>'+'</td>' +
			'<td style="width:6%">'+'<a href="http://118.180.8.123:8081/ecp/resources_share?op=download&useraAccount=">'+'<img src="teachCloud/images/icon6.jpg"  class="icon2"/></a>'+'</td></tr>');
	
}
//资源格式
function bindSourceFormat(){
	$('#source_format').on('change',function(){
		
	});
}
