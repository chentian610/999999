<div class="modal-dialog" style="width: 1000px;">
	<div class="ibox-content" >
		<div class="form-group">
             <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
             </button>
       	</div>
		<div class="form-horizontal" id="questionbox">
			<div class="form-group">
				<div class="col-sm-12" style="text-align:center">
					<h2><span id="vote-title"></span></h2>
				</div>
			</div>
			<div class="ss-box input-group" style="width:98%">
				<b>问题列表</b>
	            <span class="input-group-btn"> 
	            	<button type="button" class="btn btn-success btn-sm" data-action="add-question">
	            		<span class="glyphicon glyphicon-plus-sign"></span> 添加问题
	            	</button>
	            </span>
			</div> 
			<div class="ibox-content" >
				<div class="ftab">
					<div class="ftab-cont">
						<table class="table table-bordered" id="question-table">
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
				
		<div class="hide" id="question-edit">
			<b>问题编辑</b>
			<div class="ibox-content" >
				<form class="form-horizontal" id="questionForm">
					<input name="vote_id" type="hidden"/>
					<input name="question_id" type="hidden">
					<div class="form-group">
						<label class="col-sm-2 control-label">题目</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="title">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">题目描述</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="description">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">题目类型</label>
						<div class="col-sm-9">
							<select class="form-control m-b" name="type">
				                <option value="1">单选</option>
				                <option value="2">多选</option>
				                <option value="3">填空</option>
            				</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">排序</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="seq" style="width:50px;">
						</div>
					</div>
					<div class="form-group" id="option-edit">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-12">
							<ul class="nav nav-tabs" role="tablist">
							  <li role="presentation" class="active"><a href="javascript:void(0);">问题选项</a></li>
							</ul>
						 	<div class="ss-box input-group" style="width:100%;text-align:right;border-left:1px solid #EBEBEB;border-bottom:1px solid #EBEBEB;">
					            <span class="input-group-btn"> 
					            	<button type="button" class="btn btn-primary btn-sm" data-action="add-option">
					            		<span class="glyphicon glyphicon-plus-sign"></span> 添加
					            	</button>
					            	<button type="button" class="btn btn-danger btn-sm" data-action="remove-option">
					            		<span class="glyphicon glyphicon-remove-sign"></span> 删除
					            	</button> 
					            </span>
					        </div> 
							<table class="table table-bordered" id="option-table">
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-6 col-sm-15">
                            <button type="button" class="btn btn-primary" data-action="save-question">保存</button>
						</div>
					</div>
			   </form>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function(){
		$("button[data-action='add-question']").click(function(){
			emptyQuestionEditBox();
			$("#question-edit").removeClass('hide');
		});
		
		$("#question-edit").find("select[name='type']").change(function(){
			var type = $(this).val();
			if(type == 3){
				$("#option-edit").addClass('hide');
			}else{
				$("#option-edit").removeClass('hide');
			}
		});
		
		$("button[data-action='add-option']").click(function(){
			var tr = '<tr>'
				   + '	<td style="width:3%"><input type="checkbox" data-name="options"></td>'
				   + '  <td><input type="text" data-name="title" class="form-control"></td>'
				   + '</tr>';
			$("#option-table").find('tbody').append(tr);
		});
		
		$("button[data-action='remove-option']").click(function(){
			$("input[data-name='options']:checked").each(function(){
				$(this).parents('tr').remove();
			})
		});
		
		$("#question-table").delegate("button[data-action='edit-question']", "click", function(){
			emptyQuestionEditBox();
			var questionid = $(this).parents("tr").attr("question-id");
			loadQuestion(questionid);
			$("#question-edit").removeClass('hide');
		});
		
		$("#question-table").delegate("button[data-action='remove-question']", "click", function(){
			var tr = $(this).parents("tr");
			var questionId = tr.attr('question-id');
			swal({
				title : "您确定要删除这个问题吗？",
				text : "删除后将无法恢复，请谨慎操作",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "删除",
				closeOnConfirm : false
			},function(){
				$.myajax({
					url:'wxVoteAction/deleteQuestion',
					data:{questionId: questionId},
					datatype:'json',
					success:function(data){
						if(data.success){
							tr.remove();
							if($("#questionForm").find("input[name='question_id']").val() == questionId){
								emptyQuestionEditBox();
							}
							swal("删除成功！", "您已经永久删除了这个问题!", "success");
						}else{
							alert("删除失败");
						}
						
					}
				});
			});
		});
		
		$("button[data-action='save-question']").click(function(){
			var qForm = $("#questionForm");
			var title = qForm.find("input[name='title']").val();
			if(title == ''){
				alert("题目不能为空");
				qForm.find("input[name='title']").focus();
				return false;
			}
			var seq = qForm.find("input[name='seq']").val();
			if(seq == '' || isNaN(seq)){
				alert("排序值必须是有效数字");
				qForm.find("input[name='seq']").focus();
				return false;
			}
			var type = qForm.find("select[name='type']").val();
			
			//非填空类型，保存对应选项
			if(type != 3){
				var ind = 0;
				var optionLines = $("#option-table").find("tr");
				for(var i = 0; i < optionLines.length; i++){
					var optionTD = $(optionLines[i]).find('td').eq(1);
					var inpTitle = optionTD.find("input[data-name='title']");
					if(typeof(inpTitle.val()) == "undefined" || inpTitle.val() == ''){
						alert("选项名称不能为空");
						inpTitle.focus();
						return false;
					}
					inpTitle.attr("name","options[" + ind + "].title");
					var inpId = optionTD.find("input[data-name='id']");
					if(typeof(inpId) != "undefined"){
						inpId.attr("name","options[" + ind + "].option_id");
					}
					ind++;
				}
			}
			var data = qForm.serialize();
			$.myajax({
				url:'wxVoteAction/saveQuestion',
				data:data,
				datatype:'json',
				success:function(data){
					if(data.success){
						swal("保存成功！", "", "success");
						var voteid = $("#questionForm").find("input[name='vote_id']").val();
						var votetitle = $("#questionbox").find("#vote-title").html();
						loadQuestions(voteid, votetitle);
						var question = data.result.data;
						$("#questionForm").find("input[name='question_id']").val(question.question_id);
					}else{
						alert("问题信息保存失败");
					}
				}
			});
		});
	});
	
	function loadQuestions(voteid, votetitle){
		$("#questionForm").find("input[name='vote_id']").val(voteid);
		$("#questionbox").find("#vote-title").html(votetitle);
		$.myajax({
			url:'wxVoteAction/getQuestionsByVote',
			data:{voteId:voteid},
			datatype:'json',
			success:function(data){
				if(data.success && data.result.total > 0){
					var questions = data.result.data;
					var htm = '';
					for(var i = 0; i < questions.length; i++){
	                    var question = questions[i];
	                    htm  +=	'<tr question-id="' + question.question_id + '" index="' + (i+1) + '">'
	                    	 +	'	<td style="width:5%">' + (i+1) + '</td>' 
	                    	 +	'	<td style="width:70%">' + question.title + '</td>'
						     +	'	<td>'
							 +	'		<div class="col-sm-10 art-opt-btn">'
							 +	'	 		<button type="button" data-action="edit-question" class="btn btn-primary btn-sm">编辑</button>'
							 +	'	  		<button type="button" data-action="remove-question" class="btn btn-danger btn-sm">删除</button>'
							 +	'	  	</div>'
							 +	'	</td>'
							 +	'</tr>';
	                } 
					$("#question-table").find('tbody').html(htm);
				}
			}
		});
	}
	
	function loadQuestion(questionid){
		$.myajax({
			url:'wxVoteAction/getQuestionsById',
			data:{questionId:questionid},
			datatype:'json',
			success:function(data){
				if(data.success && data.result.total > 0){
					addQuestionToEditBox(data.result.data);
				}
			}
		});
	}
	
	function emptyQuestionList(){
		$("#question-table").find('tbody').html("");
	}
	
	function emptyQuestionEditBox(){
		var qForm = $("#questionForm");
		qForm.find("input[name='question_id']").val("");
		qForm.find("input[name='title']").val("");
		qForm.find("input[name='description']").val("");
		qForm.find("select[name='type']").find("option[value='1']").prop('selected',true);
		$("#option-edit").removeClass('hide');
		qForm.find("input[name='seq']").val("");
		$("#option-table").find('tbody').html("");
		$("#question-edit").addClass('hide');
	}
	
	function addQuestionToEditBox(vo){
		var qForm = $("#questionForm");
		qForm.find("input[name='question_id']").val(vo.question_id);
		qForm.find("input[name='title']").val(vo.title);
		qForm.find("input[name='description']").val(vo.description);
		qForm.find("select[name='type']").find("option[value='" + vo.type + "']").prop('selected',true);
		qForm.find("input[name='seq']").val(vo.seq);
		
		if(vo.type == 3){
			$("#option-edit").addClass('hide');
		}else{
			$("#option-edit").removeClass('hide');
		}
		
		var optionHtml = "";
		var options = vo.options;
		for(var i = 0; i < options.length; i++){
			var option = options[i];
			optionHtml += '<tr>'
					   +  '  <td style="width:3%"><input type="checkbox" data-name="options"></td>'
				       +  '	 <td>'
					   +  '	 	<input type="text" class="form-control" data-name="title" value="' + option.title + '">'
					   +  '	 	<input type="hidden" data-name="id" value="' + option.option_id + '">'
					   +  '  </td>'
					   +  '</tr>';
		}
		$("#option-table").find('tbody').html(optionHtml);
	}

</script>