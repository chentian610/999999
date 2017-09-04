<div class="modal-dialog">
	<div class="ibox-content" >
		<div class="form-group">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span></button>
       	</div>
		<form class="form-horizontal" id="voteForm">
			<input type="hidden" name="vote_id"/>
			<div class="form-group">
				<label class="col-sm-2 control-label">主题名称</label>
				<div class="col-sm-9" >
					<input type="text" class="form-control"	name="title">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">描述</label>
				<div class="col-sm-9">
					<input type="text" class="form-control"	id="description" name="description">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">开始时间</label>
				<div class="col-sm-9">
					<input type="text" class="form-control layer-date" name="begin_date" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">结束时间</label>
				<div class="col-sm-9">
					<input type="text" class="form-control layer-date" name="valid_date" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-11">
		            <button type="button" class="btn btn-primary" data-action="save-vote">保存</button>
		            <button type="button" class="btn btn-primary" data-dismiss="modal" id="edit-close">关闭</button>
				</div>
			</div>
		 </form>
	</div>
</div>
<script type="text/javascript">
$(function(){
	$("button[data-action='save-vote']").click(function(){
		var vForm = $("#voteForm");
		var title = vForm.find("input[name='title']").val();
		var begin_date = vForm.find("input[name='begin_date']").val();
		var valid_date = vForm.find("input[name='valid_date']").val();		
		if(title == ''){
			alert('主题名称不能为空'); 
			vForm.find("input[name='title']").focus();
			return false;
		}
		
		if(begin_date == ''){
			alert('开始时间不能为空'); 
			return false;
		}
		
		if(valid_date == ''){
			alert('结束时间不能为空'); 
			return false;
		}
		
		var data = vForm.serialize();
		$.myajax({
			url:'wxVoteAction/saveVote',
			data:data,
			datatype:'json',
			success:function(data){
				if(data.success){
					window.location.reload();
				}else{
					alert("投票主题保存失败");
				}
			}
		});
	});
});


function emptyVoteEditBox(){
	var vForm = $("#voteForm");
	vForm.find("input[name='vote_id']").val("");
	vForm.find("input[name='title']").val("");
	vForm.find("input[name='description']").val("");
	vForm.find("input[name='begin_date']").val("");
	vForm.find("input[name='valid_date']").val("");
}

function addVoteToEditBox(vo){
	var vForm = $("#voteForm");
	vForm.find("input[name='vote_id']").val(vo.vote_id);
	vForm.find("input[name='title']").val(vo.title);
	vForm.find("input[name='description']").val(vo.description);
	vForm.find("input[name='begin_date']").val(formatterDateTime(new Date(vo.begin_date)));
	vForm.find("input[name='valid_date']").val(formatterDateTime(new Date(vo.valid_date)));
}

</script>
  