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
	<link rel="stylesheet" type="text/css" href="teachCloud/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/jquery.treeview.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/style.css">
	<link rel="stylesheet" type="text/css" href="teachCloud/css/css.css">
</head>

<body>
<div class="mainwrap" style="width: 1200px;">
	<div class="ftab clearfix">
		<ul class="nav1 clearfix">
			<li class="active">教材资源</li>
			<li>视频资源</li>
		</ul>
		<div class="textbook res clearfix">
			<dl class="sub_dl1">
		    	<dt>年 级:</dt>
		        <dd>
		        	<ul id="gradeList">
						<li><a href="javascript:void(0)" class="now">一年级</a></li>
						<li><a href="javascript:void(0)" >二年级</a></li>
						<li><a href="javascript:void(0)" >三年级</a></li>
						<li><a href="javascript:void(0)" >四年级</a></li>
						<li><a href="javascript:void(0)" >五年级</a></li>
						<li><a href="javascript:void(0)" >六年级</a></li>
						<li><a href="javascript:void(0)" >七年级</a></li>
						<li><a href="javascript:void(0)" >八年级</a></li>
						<li><a href="javascript:void(0)" >九年级</a></li>
						<li><a href="javascript:void(0)" >高一</a></li>
						<li><a href="javascript:void(0)" >高二</a></li>
						<li><a href="javascript:void(0)" >高三</a></li>
		            </ul>
		        </dd>
		    </dl>
		    <dl class="sub_dl1">
		    	<dt>课 目:</dt>
		        <dd>
		        	<ul id="courseList">
						<li><a href="javascript:void(0)" class="now">语文</a></li>
						<li><a href="javascript:void(0)" >数学</a></li>
						<li><a href="javascript:void(0)" >英语</a></li>
						<li><a href="javascript:void(0)" >音乐</a></li>
						<li><a href="javascript:void(0)" >美术</a></li>
						<li><a href="javascript:void(0)" >体育</a></li>
						<li><a href="javascript:void(0)" >科学</a></li>
						<li><a href="javascript:void(0)" >品德与生活</a></li>
						<li><a href="javascript:void(0)" >劳动技术</a></li>
						<li><a href="javascript:void(0)" >品德与社会</a></li>
		            </ul>
		        </dd>
		    </dl>
		    <dl class="sub_dl1">
		    	<dt>版 本:</dt>
		        <dd>
		        	<ul id="versionList">
						<li><a href="javascript:void(0)" class="now">人教版（新课标）</a></li>
						<li><a href="javascript:void(0)" >北师大版</a></li>
						<li><a href="javascript:void(0)" >冀教版</a></li>
						<li><a href="javascript:void(0)" >苏教版</a></li>
						<li><a href="javascript:void(0)" >湘教版</a></li>
						<li><a href="javascript:void(0)" >教科版</a></li>
						<li><a href="javascript:void(0)" >鲁教版（五四制）</a></li>
						<li><a href="javascript:void(0)" >西师大版</a></li>
						<li><a href="javascript:void(0)" >语文版</a></li>
						<li><a href="javascript:void(0)" >语文S版</a></li>
						<li><a href="javascript:void(0)" >语文A版</a></li>
						<li><a href="javascript:void(0)" >浙教版</a></li>
						<li><a href="javascript:void(0)" >鄂教版</a></li>
						<li><a href="javascript:void(0)" >长春版</a></li>
						<li><a href="javascript:void(0)" >苏教版</a></li>
		            </ul>
		        </dd>
		    </dl>
			<div class="textbook_content clearfix" style="clear: both;">
				<div class="textbook_left">
					<ul class="nav2 clearfix">
						<li class="active">上册</li>
						<li>下册</li>
					</ul>
					<ul id="tree" class="tree">
						<li><a>上册</a>
							<ul>
								<li><a>入学教育</a></li>
								<li><a>汉语拼音</a></a>
									<ul>
										<li><a>复习一</a></li>
										<li><a>复习二</a></li>
										<li><a>有趣的故事</a></li>
									</ul>
								</li>
								<li><a>课文一</a>
									<ul>
										<li><a>1.画</a></li>
										<li><a>2.四季</a></li>
										<li><a>2.四季</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
									</ul>
								</li>
							</ul>
						</li>
					</ul>
					<ul id="tree2" class="tree" >
						<li><a>下册</a>
							<ul>
								<li><a>入学教育</a></li>
								<li><a>汉语拼音</a></a>
									<ul>
										<li><a>复习一</a></li>
										<li><a>复习二</a></li>
										<li><a>有趣的故事</a></li>
									</ul>
								</li>
								<li><a>课文一</a>
									<ul>
										<li><a>1.画</a></li>
										<li><a>2.四季</a></li>
										<li><a>2.四季</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
										<li><a>语言园地二 我们的画</a></li>
									</ul>
								</li>
							</ul>
						</li>
					</ul>
				</div>
				<div class="textbook_right">
					<ul class="nav3 clearfix">
						<li class="active">全部</li>
						<li>随堂课件</li>
						<li>教学设计</li>
						<li>同步练习</li>
						<li>拓展资源</li>
						<li>
							<select class="form-control last">  
							    <option value='all'>资源格式</option>
							    <option value='doc'>DOC</option>
							    <option value='DOCX'>DOCX</option>
							    <option value='PPT'>PPT</option>
							    <option value='PPTX'>PPTX</option>
							    <option value='TXT'>TXT</option>
							    <option value='MP4'>MP4</option>
							    <option value='SWF'>SWF</option>
							    <option value='JPG'>JPG</option>  
							    <option value='XLS'>XLS</option>  
							</select>
						</li>
					</ul>
					<table class="table">
						<thead>
							<tr class="active">
								<th width="320">资源名称</th>
								<th>资源评分</th>
								<th>阅读量</th>
								<th>点赞</th>
								<th>大小</th>
								<th>上传时间</th>
								<th>下载</th>
								<th>收藏</th>
								<th>推荐到班级</th>
							</tr>
						</thead>
						<tbody>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon1.jpg" class="icon1" />上册 课件1</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="teachCloud/images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a></td> 
							
							</tr>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon2.jpg" class="icon1" />上册 课件2</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="teachCloud/images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a></td> 
							
							</tr>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon3.jpg" class="icon1" />上册 课件3</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="teachCloud/images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a></td> 
							
							</tr>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon1.jpg" class="icon1" />上册 课件4</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="teachCloud/images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a></td> 
							
							</tr>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon1.jpg" class="icon1" />上册 课件4</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="images/icon6.jpg"  class="icon2"/></a></td> 
							
							</tr>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon1.jpg" class="icon1" />上册 课件4</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="teachCloud/images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a></td> 
							
							</tr>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon1.jpg" class="icon1" />上册 课件4</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="teachCloud/images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a></td> 
							
							</tr>
							<tr >
								<td><a href="detail.jsp" class="resource"><img src="teachCloud/images/icon1.jpg" class="icon1" />上册 课件4</a></td>
								<td>
									<span>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-on"></b>
										<b class="ic ic-star-s-off"></b>
									</span>
								</td>
								<td>463</td>
								<td>3</td>
								<td>3.81MB</td>
								<td>2016-03-25</td>
								<td><a href="##"><img src="teachCloud/images/icon4.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon5.jpg"  class="icon2"/></a></td> 
								<td><a href="##"><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a></td> 
							</tr>
						</tbody>
					</table>
					<div class="pagination1">
						<ul class="pagination">
							<li>
								<a href="#" aria-label="Previous">
									<span aria-hidden="true">&laquo;</span>
								</a>
							</li>
							<li><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li>
								<a href="#" aria-label="Next">
									<span aria-hidden="true">&raquo;</span>
								</a>
							</li>
						</ul>
						<div class="pagin">
							跳到<input type="text" class="form-control pagin1" maxlength="3" />页
							<input class="btn btn-default pagin2" type="submit" value="确定">
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="video res" style="display: none;">
			
		    <dl class="sub_dl1">
		    	<dt>视频分类:</dt>
		        <dd>
		        	<ul id="videoType">
						<li><a href="javascript:void(0)" class="now">初中视频</a></li>
						<li><a href="javascript:void(0)" >高中视频</a></li>
						<li><a href="javascript:void(0)" >幼儿视频</a></li>
						<li><a href="javascript:void(0)" >黄冈课堂</a></li>
		            </ul>
		        </dd>
		    </dl>
		    <dl class="sub_dl1" id="classDiv">
		    	<dt>年 级:</dt>
		        <dd>
		        	<ul >
						<li><a href="javascript:void(0)"  class="now">七年级</a></li>
						<li><a href="javascript:void(0)" >八年级</a></li>
						<li><a href="javascript:void(0)" >九年级</a></li>
		            </ul>
		        </dd>
		    </dl>
		    <dl class="sub_dl1" id="kemuDL">
		    	<dt>课 目:</dt>
		        <dd>
		        	<ul>
						<li><a href="javascript:void(0)" class="now">语文</a></li>
						<li><a href="javascript:void(0)" >数学</a></li>
						<li><a href="javascript:void(0)" >英语</a></li>
						<li><a href="javascript:void(0)" >生物</a></li>
		            </ul>
		        </dd>
		    </dl>
			<div class="video_content clearfix">
				<div class="video_list">
					<a href="detail.jsp">
						<img src="teachCloud/images/pic01.jpg" class="video_img" />
						<div class="video_bottom">
							<div class="video_text">散步 <span>莫怀戚[人教 苏教]</span></div>
							<div class="video_play">
								<img src="teachCloud/images/play.jpg" />
								<span class="video_num">9492</span>
							</div>
						</div>
					</a>
				</div>
				<div class="video_list">
					<a href="detail.jsp">
						<img src="teachCloud/images/pic01.jpg" class="video_img" />
						<div class="video_bottom">
							<div class="video_text">散步 <span>莫怀戚[人教 苏教]</span></div>
							<div class="video_play">
								<img src="teachCloud/images/play.jpg" />
								<span class="video_num">9492</span>
							</div>
						</div>
					</a>
				</div>
				<div class="video_list">
					<a href="detail.jsp">
						<img src="teachCloud/images/pic01.jpg" class="video_img" />
						<div class="video_bottom">
							<div class="video_text">散步 <span>莫怀戚[人教 苏教]</span></div>
							<div class="video_play">
								<img src="teachCloud/images/play.jpg" />
								<span class="video_num">9492</span>
							</div>
						</div>
					</a>
				</div>
				<div class="video_list">
					<a href="detail.jsp">
						<img src="teachCloud/images/pic01.jpg" class="video_img" />
						<div class="video_bottom">
							<div class="video_text">散步 <span>莫怀戚[人教 苏教]</span></div>
							<div class="video_play">
								<img src="teachCloud/images/play.jpg" />
								<span class="video_num">9492</span>
							</div>
						</div>
					</a>
				</div>
			</div>
			<div class="pagination1">
				<ul class="pagination">
					<li>
						<a href="#" aria-label="Previous">
							<span aria-hidden="true">&laquo;</span>
						</a>
					</li>
					<li><a href="#">1</a></li>
					<li><a href="#">2</a></li>
					<li><a href="#">3</a></li>
					<li><a href="#">4</a></li>
					<li><a href="#">5</a></li>
					<li>
						<a href="#" aria-label="Next">
							<span aria-hidden="true">&raquo;</span>
						</a>
					</li>
				</ul>
				<div class="pagin">
					跳到<input type="text" class="form-control pagin1" maxlength="3" />页
					<input class="btn btn-default pagin2" type="submit" value="确定">
				</div>
			</div>
		</div>
	</div>
</div>
<script src="teachCloud/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="teachCloud/js/jquery.cookie.js" type="text/javascript" charset="utf-8"></script>
<script src="teachCloud/js/jquery.treeview.min.js" type="text/javascript" charset="utf-8"></script>
<script>
	$("#tree").treeview();
	$("#tree2").treeview();
	$("#tree2").hide();
	$(function() {
		$(".mainwrap").on("click" ,".nav1 li",function(){
			$(".nav1 li").removeClass("active");
			$(this).addClass("active");
			$(".res").hide().eq($(".nav1 li").index(this)).show();
		}).on("click ","hover" ,".textbook #gradeList a",function(){
			$(".textbook #gradeList a").removeClass("now");
			$(this).addClass("now");
		}).on("click" ,".textbook #courseList a",function(){
			$(".textbook #courseList a").removeClass("now");
			$(this).addClass("now");
		}).on("click" ,".textbook #versionList a",function(){
			$(".textbook #versionList a").removeClass("now");
			$(this).addClass("now");
		}).on("click" ,".nav2 li",function(){
			$(".nav2 li").removeClass("active");
			$(this).addClass("active");
			$(".tree").hide().eq($(".nav2 li").index(this)).show();
		}).on("click" ,".nav3 li",function(){
			$(".nav3 li").removeClass("active");
			$(this).addClass("active");
		}).on("click" ,".video #videoType a",function(){
			$(".video #videoType a").removeClass("now");
			$(this).addClass("now");
		}).on("click" ,".video #classDiv a",function(){
			$(".video #classDiv a").removeClass("now");
			$(this).addClass("now");
		}).on("click" ,".video #kemuDL a",function(){
			$(".video #kemuDL a").removeClass("now");
			$(this).addClass("now");
		}).on("click" ,".tree  a",function(){
			$(".tree  a").removeClass("now");
			$(this).addClass("now");
		})
	});
</script>
</body>

</html>