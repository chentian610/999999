var vote_result_url = '';
function bindClickEvent(){
	$("#votelist").delegate("button[data-action='edit-question']", "click", function(){
		emptyQuestionList();
		emptyQuestionEditBox();
		var voteid = $(this).parents('tr').attr('vote-id');
		var votetitle = $(this).parents('tr').find('td:first').html();
		loadQuestions(voteid, votetitle);
	});
	
	$("#votelist").delegate("button[data-action='edit-vote']", "click", function(){
		emptyVoteEditBox();
		var voteid = $(this).parents('tr').attr('vote-id');
		loadVote(voteid);
	});
	
	$("#votelist").delegate("button[data-action='sort-vote']", "click", function(){
		var tr = $(this).parents('tr');
		var statement = tr.attr('statement');
		if(statement == "2"){
			var pvoteid = tr.attr('p-vote-id');
			$("iframe[data-name='result-iframe']").attr('src', vote_result_url + pvoteid);
		}else{
			alert("请先发布该投票活动");
			return false;
		}
		
	});
	
	$("#votelist").delegate("button[data-action='publish-vote']", "click", function(){
		var voteid = $(this).parents('tr').attr('vote-id');
		swal({
			title : "您确定要发布这个投票主题吗？",
			text : "发布后该投票主题将生效，请谨慎操作",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "发布",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'wxVoteAction/publishVote',
				data:{voteId: voteid},
				datatype:'json',
				success:function(data){
					if(data.success){
						window.location.reload();
					}else{
						alert("发布失败");
					}
				}
			});
		});
	});
	
	$("#votelist").delegate("button[data-action='delete-vote']", "click", function(){
		var voteid = $(this).parents('tr').attr('vote-id');
		swal({
			title : "您确定要删除这个投票主题吗？",
			text : "删除后该投票主题将无法恢复，请谨慎操作",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'wxVoteAction/deleteVote',
				data:{voteId: voteid},
				datatype:'json',
				success:function(data){
					if(data.success){
						window.location.reload();
					}else{
						alert("删除失败");
					}
				}
			});
		});
	});
	
	$("button[data-action='add-vote']").click(function(){
		emptyVoteEditBox();
	});
}

function loadResultUrl(){
	$.myajax({
		url:'wxVoteAction/getResultUrl',
		datatype:'json',
		success:function(data){
			if(data.success && data.result.total > 0){
				vote_result_url = data.result.data;
			}
		}
	});
}

function loadVote(voteid){
	$.myajax({
		url:'wxVoteAction/getVote',
		datatype:'json',
		data:{voteId:voteid},
		success:function(data){
			if(data.success && data.result.total > 0){
				addVoteToEditBox(data.result.data);
			}
		}
	});
}

function loadVotes(){
	$.myajax({
		url:'wxVoteAction/getVotes',
		datatype:'json',
		success:function(data){
			if(data.success && data.result.total > 0){
				var votes = data.result.data;
				var htm = '';
				for(var i = 0; i < votes.length; i++){
                    var vote = votes[i];
                    var status = "<span style='color:#000;'>未发布</span>";
                    if(vote.statement == "1"){
                    	status = "<span style='color:#FFC125;'>发布中  <img src='wechat/images/uploading.gif' style='width:20px;'></span>";
                    }else if(vote.statement == "2"){
                    	status = "<span style='color:#008B00;'>已发布</span>";
                    }
                    htm    += '<tr vote-id="' + vote.vote_id + '" p-vote-id="' + vote.platform_main_id + '"statement="' + vote.statement + '">'
						   + '	<td>' + vote.title + '</td>'
						   + '	<td>' + vote.description + '</td>'
						   + '	<td>' + formatterDateTime(new Date(vote.begin_date)) + '</td>'
						   + '	<td>' + formatterDateTime(new Date(vote.valid_date)) + '</td>'
						   + '	<td data-lo="statusTxt">' + status + '</td>'
						   + '  <td>'
						   + '		<div class="col-sm-15 art-opt-btn">'
						   + '			<button type="button" data-action="edit-vote" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#vote-edit-modal">编辑</button>'
						   + '			<button type="button" data-action="edit-question" class="btn btn-info btn-sm" data-toggle="modal" data-target="#question-edit-modal">问题管理</button>'
						   + '			<button type="button" data-action="sort-vote" class="btn btn-success btn-sm" data-toggle="modal" data-target="#result-show-modal">查看结果</button>'
						   + '			<button type="button" data-action="publish-vote" class="btn btn-warning btn-sm">发布</button>'
						   + '			<button type="button" data-action="delete-vote" class="btn btn-danger btn-sm">删除</button>'
						   + '		</div>'
						   + '	</td>'
                           + '</tr>';
                } 
				$("#votelist").append(htm);
				setInterval(updatePublshJob, 2000);
			}
		}
	});
}

function formatterDateTime(date) {
    var datetime = date.getFullYear()
            + "-"
            + ((date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : "0"
                    + (date.getMonth() + 1))
            + "-"
            + (date.getDate() < 10 ? "0" + date.getDate() : date
                    .getDate())
            + " "
            + (date.getHours() < 10 ? "0" + date.getHours() : date
                    .getHours())
            + ":"
            + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
                    .getMinutes())
            + ":"
            + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
                    .getSeconds());
    return datetime;
}

function updatePublshJob(){
	//获取发布中的投票
	var tr = $("#votelist").find("tr[statement='1']");
	tr.each(function(){
		var voteid = $(this).attr("vote-id");
		var _tr = $(this);
		$.myajax({
			url:'wxVoteAction/getVote',
			datatype:'json',
			data:{voteId:voteid},
			success:function(data){
				if(data.success && data.result.total > 0){
					var statement = data.result.data.statement;
					if(statement == "0"){
						_tr.attr("statement", "0");
						_tr.find("td[data-lo='statusTxt']").html("<span style='color:#000'>未发布</span>");
					}else if(statement == "2"){
						_tr.attr("p-vote-id", data.result.data.platform_main_id);
						_tr.attr("statement", "2");
						_tr.find("td[data-lo='statusTxt']").html("<span style='color:#008B00'>已发布</span>");
					}
				}
			}
		});
	});
}
