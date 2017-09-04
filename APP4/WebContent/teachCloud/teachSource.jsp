<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
   <meta charset="UTF-8">
	<title>Lezhi</title>
	<link rel="stylesheet" type="text/css" href="teachCloud/css/swiper-3.3.1.min.css"/>
	<link href="hplus/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/jquery.treeview.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/style.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/css.css">
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="teachCloud/css/pop.css">
	<style>
		
	</style>
</head>

<body>
<div class="mainwrap" style="width: 1200px;">
	<div class="ftab clearfix">
		<ul class="nav1 clearfix">
			<li class="active" id="text"></li>
		</ul>
		<div class="textbook res clearfix">
		
			<dl class="sub_dl1">
		    	<dt>年 级:</dt>
		        <dd>
		        	<ul id="gradeList">
						<li><a href="javascript:void(0)" class="now" GradeId="G04">一年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G05">二年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G06">三年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G07">四年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G08">五年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G09">六年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G10">七年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G11">八年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G12">九年级</a></li>
						<li><a href="javascript:void(0)" GradeId="G13">高一</a></li>
						<li><a href="javascript:void(0)" GradeId="G14">高二</a></li>
						<li><a href="javascript:void(0)" GradeId="G15">高三</a></li>
		            </ul>
		        </dd>
		    </dl>
		    <dl class="sub_dl1">
		    	<dt>课 目:</dt>
		        <dd>
		        	<ul id="courseList">
						<li><a href="javascript:void(0)" class="now" CourseId="K01">语文</a></li>
						<li><a href="javascript:void(0)" CourseId="K02">数学</a></li>
						<li><a href="javascript:void(0)" CourseId="K03">英语</a></li>
						<li><a href="javascript:void(0)" CourseId="K04">物理</a></li>
						<li><a href="javascript:void(0)" CourseId="K05">化学</a></li>
						<li><a href="javascript:void(0)" CourseId="K06">生物</a></li>
						<li><a href="javascript:void(0)" CourseId="K07">政治</a></li>
						<li><a href="javascript:void(0)" CourseId="K08">历史</a></li>
						<li><a href="javascript:void(0)" CourseId="K09">地理</a></li>
						<li><a href="javascript:void(0)" CourseId="K10">思想品德</a></li>
						<li><a href="javascript:void(0)" CourseId="K11">考前辅导</a></li>
						<li><a href="javascript:void(0)" CourseId="K12">音乐</a></li>
						<li><a href="javascript:void(0)" CourseId="K13">美术</a></li>
						<li><a href="javascript:void(0)" CourseId="K14">体育与健康</a></li>
						<li><a href="javascript:void(0)" CourseId="K15">信息技术</a></li>
						<li><a href="javascript:void(0)" CourseId="K16">通用技术（高中）</a></li>
						<li><a href="javascript:void(0)" CourseId="K17">体育</a></li>
						<li><a href="javascript:void(0)" CourseId="K18">科学</a></li>
						<li><a href="javascript:void(0)" CourseId="K19">品德与生活</a></li>
						<li><a href="javascript:void(0)" CourseId="K20">劳动技术</a></li>
		            </ul>
		        </dd>
		    </dl>
		    <dl class="sub_dl1">
		    	<dt>版 本:</dt>
		        <dd>
		        	<ul id="versionList">
		        		<li><a href="javascript:void(0)" class="now" VersionId="V02">北师大版</a></li>
						<li><a href="javascript:void(0)" VersionId="V01">人教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V03">中考通</a></li>
						<li><a href="javascript:void(0)" VersionId="V04">冀教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V05">苏教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V06">陕旅游</a></li>
						<li><a href="javascript:void(0)" VersionId="V07">湘教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V08">中图版</a></li>
						<li><a href="javascript:void(0)" VersionId="V09">华师版</a></li>
						<li><a href="javascript:void(0)" VersionId="V10">外语教研</a></li>
						<li><a href="javascript:void(0)" VersionId="V11">教科版</a></li>
						<li><a href="javascript:void(0)" VersionId="V12">人教新起点</a></li>
						<li><a href="javascript:void(0)" VersionId="V13">粤教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V14">中科版</a></li>
						<li><a href="javascript:void(0)" VersionId="V15">鲁教版（五四制）</a></li>
						<li><a href="javascript:void(0)" VersionId="V16">人教版新课标</a></li>
						<li><a href="javascript:void(0)" VersionId="V17">青岛版（五四制）</a></li>
						<li><a href="javascript:void(0)" VersionId="V18">西南师大版</a></li>
						<li><a href="javascript:void(0)" VersionId="V19">语文版</a></li>
						<li><a href="javascript:void(0)" VersionId="V20">语文S版</a></li>
						<li><a href="javascript:void(0)" VersionId="V21">人教版PEP</a></li>
						<li><a href="javascript:void(0)" VersionId="V22">人教版新版</a></li>
						<li><a href="javascript:void(0)" VersionId="V23">青岛版（六三制）</a></li>
						<li><a href="javascript:void(0)" VersionId="V24">鲁教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V25">外研社版（一年级起点）</a></li>
						<li><a href="javascript:void(0)" VersionId="V26">外研社版（三年级起点）</a></li>
						<li><a href="javascript:void(0)" VersionId="V29">语文A版</a></li>
						<li><a href="javascript:void(0)" VersionId="V30">鲁教版（六三制）</a></li>
						<li><a href="javascript:void(0)" VersionId="V31">鲁科版</a></li>
						<li><a href="javascript:void(0)" VersionId="V32">外研社版</a></li>
						<li><a href="javascript:void(0)" VersionId="V33">岳麓版</a></li>
						<li><a href="javascript:void(0)" VersionId="V34">星球版</a></li>
						<li><a href="javascript:void(0)" VersionId="V35">仁爱版</a></li>
						<li><a href="javascript:void(0)" VersionId="V37">浙教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V38">辽师大版（三年级起点）</a></li>
						<li><a href="javascript:void(0)" VersionId="V39">鄂教版</a></li>
						<li><a href="javascript:void(0)" VersionId="V40">长春版</a></li>
						<li><a href="javascript:void(0)" VersionId="V41">人民版</a></li>
						<li><a href="javascript:void(0)" VersionId="V42">川教版</a></li>
						
		            </ul>
		        </dd>
		    </dl>
		    
			<div class="textbook_content clearfix" style="clear: both;">
				<div class="textbook_right">
					<ul class="nav3 clearfix" id="source_format">
						<li class="active">资源</li> 
						<li id="source_format">
							<select class="form-control last" id="source_format" > 
							
							</select>
						</li>
					</ul>
					<table class="table" width=100%>
						<thead>
							<tr class="active">
								<th width="320">资源名称</th>
								<th>资源评分</th>
								<th>阅读量</th>
								<th>点赞</th>
								<th>大小</th>
								<th>上传时间</th>
								<th>推荐到班级</th>
							</tr>
						</thead>
						<tbody id="source_list">
							<tr>
								
							</tr>
							 
						</tbody>
					</table>
					 <div class= "pull-right">
						<ul id="page_pagintor"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="Popup" class="popup">
		<div class="content-box">
			<div class="title-top">
				<div class="class-list">班级列表</div>
				<div class="close-button">×</div>
			</div>
			<div class="class-name-all">
				<div class="class-name">
					<span class="class-tip "></span>
					<label id="classname"></label>
				</div>
				
			</div>
			<button class="confirm-submit" id="save">提交</button>
			<div class="message-tip" >留言区</div>
			<div class="dialog-remark" align="center" id="source_remark">
			<textarea class="message-box" name="remark" placeholder="|" onblur="if(this.value == ''){this.style.color = '#ACA899'; this.value = '请在此处填写留言内容'; }"
    onfocus="if(this.value == '请在此处填写留言内容'){this.value =''; this.style.color = '#000000'; }"
                                style="color:#ACA899;">请在此处填写留言内容</textarea>	
			</div>
		</div>
	</div>
<script src="teachCloud/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="teachCloud/js/jquery.cookie.js" type="text/javascript" charset="utf-8"></script>
<script src="teachCloud/js/jquery.treeview.min.js" type="text/javascript" charset="utf-8"></script>
<script src="teachCloud/js/source_manager.js?d=${time}"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="hplus/js/bootstrap-wysiwyg.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="teachCloud/js/functionUtil.js"></script>
<script src="teachCloud/layer/layer.js"></script>
<script src="js/functionUtil.js"></script>
<script type="text/javascript">
	var source_type = getParameterByUrl("source_type");
	var module_code = getParameterByUrl("module_code");
	var GradeId ;
	var CourseId ;
	var VersionId ;
	if (source_type==1) {//<1表示教材资源，2表示视频资源>
		document.getElementById("text").innerHTML="教材资源";
		loadExtensionName();//教材资源格式
	} else if (source_type==2) {
		document.getElementById("text").innerHTML="视频资源";
		loadVideoExtensionName();//视频资源格式
	}
	$(function() {
		$(".mainwrap").on("click" ,".nav3 li",function(){
			$(".nav3 li").removeClass("active");
			$(this).addClass("active");
		}).on("click" ,".recommend",function(){
			_setJob(this);
		}).on("click",".icon2",function(){
			
			$('.popup').removeClass('popup-hid');
		});
		
		$(".popup").on("click",".close-button",function(){
		
			$('.popup').addClass('popup-hid');
		});
		
	});
		initSourcePage();// 初始化加载
		bindGradeListClickEvent();
		bindCourseListClickEvent();
		bindVersionListClickEvent();
		bindExtensionNameChangeEvent();
		submit();//tijiao发送推荐
</script>
</body>

</html>