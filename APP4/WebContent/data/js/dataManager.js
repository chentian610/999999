var currentPage=1;
var limit=15;
var amr_to_mp3_action = localStorage.getItem('amr_to_mp3_action');
var file_upload_action = localStorage.getItem('file_upload_action');
var $this;
var $uploadPic;
var queryVO = {};
var $thisVO = {};
var updateVO = {};
var deteleVO = {};
var insertVO = {};
var contentList = {};
var BeanVO = '';
var columnTypeVO = {};

function initDataTitle() {
    $('#dataTitle').empty();
    //加载模板
    $.myajax({
        url:"systemAction/getHeaderDataByTableName",
        data:{table_name: getParameterByUrl('table_name'),user_id:localStorage.getItem('user_id')},//
        datatype:"json",
        success:function(data){
            var _tr = '';
            var result = data.result.data;
            _tr += '<th class="checkedAll checkboxALL display" title="选择当前显示的所有数据"><input id="checkboxALL" type="checkbox"/>全选</th>';
            for(var i in result) {
                var item = result[i];
                var column_name = item.column_name;
                var data_type;
                var column_type = item.column_type;
                var column_length = column_type.substring(column_type.indexOf('(')+1,column_type.indexOf(')'));
                if (item.column_key == 'PRI') data_type = 'PRIMARY-KEY';
                else data_type = item.data_type.toUpperCase();
                if (isIndexOf(column_name,'_url')) data_type = 'FILE';
                if (isIndexOf(column_name,'_resize_url')) data_type = 'FILE_RESIZE';
                if (isIndexOf(column_name,'is_')) data_type = 'BOOLEAN';
                if (isIndexOf(column_name,'_date') || (isIndexOf(column_name,'_time'))) data_type = 'DATE';
                columnTypeVO[column_name] = column_type.substring(0,column_type.indexOf('(')).toUpperCase();
                _tr += '<th><div class="text-css" data-column_name="'+column_name+'" data-column_type="'+column_type+'" data-type="'+data_type+'" data-column_length="'+column_length+'">'
                    +(isNotEmpty(item.column_comment)?item.column_comment:column_name)+'</div></th>';
            }
            $('#dataTitle').append(_tr);
            loadContent();
            bindcheckboxALLClick();
        }
    });
}

function bindcheckboxALLClick() {
    $('#checkboxALL').on('click',function () {
        var boolean = $(this).is(':checked');
        $(".checkbox input").each(function () {
            $(this).prop("checked", boolean);
        });
    });
}

function bindbtnLimitClickEvent() {
    $('#btnLimit').unbind('click').on('click',function () {
        if (isEmpty($('#limit').val())) return;
        limit = $('#limit').val();
        loadContent();
    });
}

function bindSpinnerClickEvent() {
    $('#Spinner').unbind('change').on('change',function () {
        loadContent();
    });
}

//加载内容列表
function loadContent(){
    queryVO.start_id = (currentPage-1)*limit;
    queryVO.limit = limit;
    queryVO.page = currentPage;
    queryVO.order_sql = $('#Spinner option:selected').val();
    queryVO.table_name = getParameterByUrl('table_name');
    queryVO.table_id = getParameterByUrl('table_id');
    $('#LoadAnimation').removeClass('display');
    $('#dateList').empty();
    $.myajax({
        url:'systemAction/getDataByTableName',
        data:queryVO,
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
                    loadContent();
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
        }
    });
}

function addToWeb(data) {
    if (isEmpty(data.result.data)){$('#LoadAnimation').addClass('display');return;}
    var _td = '';
    var result = data.result.data;
    for(var i in result) {
        var item = result[i];
        _td += CollatingData(item,i);
    }
    $('#dateList').empty().append(_td);
    $('#LoadAnimation').addClass('display');
    bindjPlayerPlayClickEvent();
    bindPRIMARYKEYClickEvent();
    bindcheckboxClickEvent();
}

function CollatingData(item,i) {
    var contentVO = {};
    var _td = '';
    _td += '<tr class="menuBar" data-index="'+i+'"><td class="checkedAll checkbox display"><input style="margin: 12px;" class="checkboxInput" name="checkbox" type="checkbox"/></td>';
    $('#dataTitle th').each(function () {
        if ($(this).hasClass('checkboxALL')) return;
        var key = $(this).find('div').attr('data-column_name');
        var value = item[$(this).find('div').attr('data-column_name')];
        var data_type = $(this).find('div').attr('data-type');
        contentVO[key] = value;
        value = checkHtml(value);
        if (data_type == 'PRIMARY-KEY') {
            _td += '<td class="PRIMARY-KEY"><div data-type="PRIMARY-KEY"  data-column_name="'+key+'">'+value+'</div></td>';
        } else if (data_type == 'BOOLEAN') {
            _td += '<td class="BOOLEAN"><div data-type="BOOLEAN"  data-column_name="'+key+'">'+value+'</div></td>';
        } else if (data_type == 'DATETIME'|| data_type == 'DATE') {
            if (isNotEmpty(value)) value = getDateStr(value,'second');
            else value = '  ';
            _td += '<td class="DATE"><div data-type="DATE"  data-column_name="'+key+'">'+value+'</div></td>';
        } else if (data_type == 'FILE' || data_type == 'FILE_RESIZE') {
            if (isNotEmpty(value)) {
                if (!isFile(value,true) && !isFile(value,false)) $(this).find('div').attr('data-type','HTML');
                if (value.length > 10) value = value.substring(0,20)+'...';
            } else value = '';
            _td += '<td class="'+data_type+'"><div  data-type="'+data_type+'" data-column_name="'+key+'">'+value+'</div></td>';
        } else {
            if (isNotEmpty(value)){
                if (value.length > 10) value = value.substring(0,20)+'...';
            } else value = '';
            _td += '<td class="TEXT"><div  data-type="'+data_type+'" data-column_name="'+key+'">'+value+'</div></td>';
        }
    });
    _td += '</tr>';
    contentList[i] = contentVO;
    return _td;
}

function bindcheckboxClickEvent() {
    $('.checkboxInput').unbind('click').on('click',function () {
        var boolean = true;
        $('.checkboxInput').each(function () {
            if (boolean == false) return;
            if ($(this).is(':checked') == false) {
                boolean = false;
                return;
            }
        });
        $('#checkboxALL').prop("checked",boolean);
    });
}

function checkHtml(htmlStr) {
    var str = '';
    var reg = /<[^>]*>/g;
    if (reg.test(htmlStr)) {
        for (var i in htmlStr) {
            var value = htmlStr.charAt(i);
            if (value == '<') value = '《';
            if (value == '>') value = '》';
            str += value;
        }
    } else str = htmlStr;
    return str;
}

function bindDeleteALLClickEvent() {
    $('#deleteALL').unbind('click').on('click',function () {
        $(this).addClass('display');
        $('#CheckBoxBtn').show();
        $('.checkboxALL').removeClass('display');
        $('.checkbox').removeClass('display');
    });
}

function bindcancelDeleteClickEvent() {
    $('#cancelDelete').unbind('click').on('click',function () {
        $('#CheckBoxBtn').hide();
        $('#deleteALL').removeClass('display');
        $('.checkboxALL').addClass('display');
        $('.checkbox').addClass('display');
    });
}

function bindDetermineClickEvent() {
    $('#Determine').unbind('click').on('click',function () {
        $('#CheckBoxBtn').hide();
        $('#deleteALL').removeClass('display');
        $('.checkboxALL').addClass('display');
        $('.checkbox').addClass('display');
        var tableID = '';
        $('.checkboxInput').each(function () {
            if ($(this).is(':checked') == false) return;
            var value = contentList[$(this).parent().parent().attr('data-index')][$(this).parent().parent().children('.PRIMARY-KEY').find('div').attr('data-column_name')];
            if (isNotEmpty(tableID)) tableID += ',' + value;
            else tableID = value;
        });
        if (isEmpty(tableID)) return;
        deteleVO.tableID = tableID;
        deleteData();
    });
}

function bindPRIMARYKEYClickEvent() {
    $('.menuBar').mousedown(function (e) {
        if(3 == e.which) {
            $this = $(this);
            var xx = e.originalEvent.x || e.originalEvent.layerX || 0;
            var yy = e.originalEvent.y || e.originalEvent.layerY || 0;
            $('#nav').css({top: yy, left: xx});
            bindKeyBoardKeyUpkEvent();
            $('#nav').removeClass('display');
        }
    });
}

function ShieldMouseRightKey() {
    $(document).bind("contextmenu",function(e){
        return false;
    });
}

function bindKeyBoardKeyUpkEvent() {
    $(document).keyup(function (event) {
        var key =  event.which;
        if (key == 81 || (event.ctrlKey && key == 81)) $('#cancel').click();
        else if (key == 73 || (event.ctrlKey && key == 73)) $('#insert').click();
        else if (key == 83 || (event.ctrlKey && key == 83)) $('#See').click();
        else if (key == 85 || (event.ctrlKey && key == 85)) $('#update').click();
        else if (key == 68 || (event.ctrlKey && key == 68)) $('#delete').click();
    });
}

function bindCancelClickEvent() {
    $('#cancel').unbind('click').on('click',function () {
        $(document).unbind('keyup');
        $('#nav').addClass('display');
    });
}

function bindSeeClickEvent() {
    $('#See').unbind('click').on('click',function () {
        setTemplateContent();
    });
}

function bindUpdateClickEvent() {
    $('#update').unbind('click').on('click',function () {
        setTemplateContent();
    });
}

function bindDeleteClickEvent() {
    $('#delete').unbind('click').on('click',function () {
        deteleVO.tableID = contentList[$this.attr('data-index')][$this.children('.PRIMARY-KEY').find('div').attr('data-column_name')];
        deleteData();
    });
}

function deleteData() {
    swal({
        title: "您确定要删除这些信息吗",
        text: "删除后将无法恢复，请谨慎操作！",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "删除",
        closeOnConfirm: false
    }, function () {
        deteleVO.table_name = queryVO.table_name;
        deteleVO.table_id = queryVO.table_id;
        deteleVO.user_id = localStorage.getItem('user_id');
        deteleVO.user_type = localStorage.getItem('user_type');
        deteleVO.user_name = localStorage.getItem('user_name');
        deteleVO.phone = localStorage.getItem('phone');
        $.myajax({
            url:"systemAction/deleteTableData",
            data:deteleVO,
            datatype:"json",
            success:function(data){
                swal("删除失败！", "删除"+data.msg, "success");
                loadContent();
            }
        });
    });
}

function bindInsertClickEvent() {
    $('#insert').unbind('click').on('click',function () {
        setInsertContent();
    });
}

function bindInsertDataAClickEvent() {
    $('#insertDataA').unbind('click').on('click',function () {
        setInsertContent();
    });
}

function setInsertContent() {
    var _div = '';
    $('#dataTitle th').each(function () {
        if ($(this).hasClass('checkboxALL')) return;
        var key = $(this).find('div').attr('data-column_name');
        var title = $(this).find('div').text();
        var data_type = $(this).find('div').attr('data-type');
        var column_length = $(this).find('div').attr('data-column_length');
        $thisVO[key+'_type'] =data_type;
        $thisVO[key+'_length'] = column_length;
        if (data_type == 'FILE_RESIZE' || key == 'create_date' || key == 'update_date'|| key == 'create_by' || key == 'update_by' || key == 'version') {
            if (isNotEmpty(BeanVO) && data_type && key !=  'update_date' && key != 'update_by') BeanVO += ',' + key;
            else if (key != 'update_date' && key != 'update_by') BeanVO = key;
            return;
        }
        _div += '<div class="form-group"><label class="col-sm-3 control-label" style="margin-top: 7px;">'+title+'：</label>'
            +'<div class="col-sm-8 parameterInsert" data-column_name="'+key+'" data-type="'+data_type+'">';
        if (data_type == 'FILE') {
            _div += '<img id="'+key+'" width="150"data-type="FILE'+data_type+'" /><button data-column_name="'+key+'"  class="btn btn-primary" id="uploadPic">上传图片</button>';
        } else if (data_type == 'BOOLEAN') {
            _div += '<span class="span-css"><input class="judgeChecked"  data-type="BOOLEAN" name="'+key+'" type="radio" value="1"/>是</span>'
                +'<span class="span-css"><input class="judgeChecked" data-type="BOOLEAN" name="'+key+'" type="radio" value="0" checked/>否</span>';
        } else if (data_type == 'PRIMARY-KEY')
            _div += '<input id="'+key+'" placeholder="主键ID请根据表的实际情况决定是否填写" type="text" class="form-control Check-length" />';
        else if (data_type == 'DATE' || data_type == 'DATETIME')
            _div += '<input id="'+key+'" type="text" data-type="DATETIME" placeholder="'+title+'" class="form-control laydate laydate-icon"/>';
        else if (data_type == 'INT'|| data_type == 'BIGINT'|| data_type == 'FLOAT' || data_type == 'DOUBLE' || data_type == 'DECIMAL')
            _div += '<input id="'+key+'" type="number" data-type="NUMBER" placeholder="'+title+'" min="0" onpaste="return false;" class="form-control Check-length"/>';
        else {
            if (column_length < 80 && isNotEmpty(column_length))  _div += '<input id="'+key+'" placeholder="'+title+'" type="text" class="form-control Check-length" />';
            else _div += '<textarea id="'+key+'" placeholder="'+title+'" class="form-control Check-length" rows="2"></textarea>';
        }
        _div += '</div></div>';
        $('#dataDetail').empty().append(_div);
        $('#nav').addClass('display');
        $(document).unbind('keyup');
        $('#updateData').addClass('display');
        $('#insertData').removeClass('display');
        bindLaydateClickEvent();
        bindCheckLengthKeyDownEvent();
        bindCheckLengthKeyUpEvent();
        bindInsertDataClickEvent();
        bindUploadPicClickEvent();
    });
}

function bindInsertDataClickEvent() {
    $('#insertData').unbind('click').on('click',function () {
        insertVO.insert_field = '';
        var insert = {};
        $('.parameterInsert').each(function () {
            var key = $(this).attr('data-column_name');
            if ($(this).attr('data-type') == 'BOOLEAN') 
                insert[key] = $('.parameterInsert input[name="'+key+'"]:checked').val();
            else if ($(this).attr('data-type') == 'FILE') {
                insert[key] = $('#'+key+'').attr('src');
                insert['file_resize_url'] = $('#'+key+'').attr('data-resize_url');
            } else {
                if (columnTypeVO[key] == 'VARCHAR' && $(this).attr('data-type') == 'DATE') insert[key] = getDateStr($('#'+key+'').val(),'day');
                else insert[key] = $('#'+key+'').val();
            }
            if (isEmpty(insert[key])) return;
            if (isNotEmpty(insertVO.insert_field))  insertVO.insert_field += ',' + key;
            else insertVO.insert_field = key;
        });
        if (isEmpty(insertVO.insert_field)) return;
        if (isNotEmpty(BeanVO)) insertVO.insert_field += ',' + BeanVO;
        var keys = BeanVO.split(',');
        for (var i in keys) {
            var key = keys[i];
            if  (key == 'create_by') insert[key] = localStorage.getItem('user_id');
            else if (key == 'create_date') insert[key] = getDateStr((new Date()),'second');
            else if (key == 'version') insert[key] = 0
        }
        insertVO.data = JSON.stringify(insert);
        insertVO.table_name = queryVO.table_name;
        $.myajax({
            url:"systemAction/insertTableData",
            data:insertVO,
            datatype:"json",
            success:function(data){
                $('#btnClose').click();
                toastrMessage('插入'+data.msg);
                loadContent();
            }
        });
    });
}

function bindUploadPicClickEvent() {
    $('#uploadPic').unbind('click').on('click',function () {
        $uploadPic = $(this);
        $('#uploadLogo').click();
    });
}

//绑定文件上传功能
function bindSelectedPhoto(){
    $('#uploadLogo').unbind('change').change(function() {
        var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
        if (mb > 1) toastrMessage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
        $uploadPic.parent().find('img').attr('src','data/images/LoadAnimation.gif');
        var formData = new FormData(document.getElementById("form-file"));
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
                $uploadPic.parent().find('img').attr('src',item[0].file_url);
                $uploadPic.parent().find('img').attr('data-resize_url',item[0].file_resize_url);
                if ($uploadPic.attr('data-is_reszie')) $('.FILE-RESIZE-URL').attr('src',item[0].file_resize_url);
                $uploadPic.parent().attr('is_update',true);
                $('#uploadLogo').val();
            }
        });
    });
}

function setTemplateContent() {
    var _div = '';
    var thisVO = contentList[$this.attr('data-index')];
    $('#dataTitle th').each(function () {
        if ($(this).hasClass('checkboxALL')) return;
        var key = $(this).find('div').attr('data-column_name');
        var title = $(this).find('div').text();
        $thisVO[key] = isEmpty(thisVO[key])?'':thisVO[key];
        $thisVO[key+'_type'] = $(this).find('div').attr('data-type');
        $thisVO[key+'_length'] = $(this).find('div').attr('data-column_length');
        _div += '<div class="form-group"><label class="col-sm-3 control-label" style="margin-top: 7px;">'+title+'：</label>'
            +'<div class="col-sm-8 parameterInput" data-column_name="'+key+'" data-type="'+$thisVO[key+'_type']+'">';
        if ($thisVO[key+'_type'] == 'FILE' || $thisVO[key+'_type'] == 'HTML' || $thisVO[key+'_type'] == 'FILE_RESIZE') {
            if (isEmpty($thisVO[key])) $thisVO[key] = 'notice/images/error.png';
            if (isFile($thisVO[key],true))
                _div += '<img id="'+key+'" width="300" src="'+ $thisVO[key]+'"/>'
                    +(isIndexOf($thisVO[key],'RESIZE')?'':'<button data-column_name="'+key+'"  class="btn btn-primary FILE-RESIZE-URL" data-is_reszie="'+isIndexOf($thisVO[key],'RESIZE')+'" id="uploadPic">上传图片</button>')+'';
            else  if (isFile($thisVO[key],false))
                _div += '<div id="'+key+'"><button class="jPlayer-play btn btn-primary">play</button></div>';
            else _div += '<input id="'+key+'" placeholder="'+title+'" date-type="HTML" class="form-control Check-length" value="'+$thisVO[key]+'"/>';
        } else if ($thisVO[key+'_type'] == 'VARCHAR' || $thisVO[key+'_type'] == 'LONGTEXT' || $thisVO[key+'_type'] == 'TEXT') {
            var rows = accDiv(($thisVO[key]+'').length,40);
            rows = 1 > rows?'1':Math.round(rows);
            if (isNotEmpty( $thisVO[key+'_length'])) _div += '<input id="'+key+'" type="text" placeholder="'+title+'" class="form-control Check-length" value="'+$thisVO[key]+'"/>';
            else _div += '<textarea id="'+key+'" style="max-height: 400px;" placeholder="'+title+'" class="form-control Check-length" rows="'+rows+'">'+$thisVO[key]+'</textarea>';
        } else if ($thisVO[key+'_type'] == 'BOOLEAN') {
            _div += '<span class="span-css"><input class="judgeChecked" name="'+key+'" type="radio" value="1" '+($thisVO[key]==1?'checked':'')+'/>是</span>'
                +'<span class="span-css"><input class="judgeChecked" name="'+key+'" type="radio" value="0" '+($thisVO[key]==0?'checked':'')+'/>否</span>';
        } else if ($thisVO[key+'_type'] == 'DATE' || $thisVO[key+'_type'] == 'DATETIME')
            _div += '<input id="'+key+'" type="text" placeholder="'+getDateStr($thisVO[key],'second')+'" value="'+getDateStr($thisVO[key],'second')+'" class="form-control laydate laydate-icon"/>';
        else if ($thisVO[key+'_type'] == 'INT'|| $thisVO[key+'_type'] == 'BIGINT'|| $thisVO[key+'_type'] == 'FLOAT' || $thisVO[key+'_type'] == 'DOUBLE' || $thisVO[key+'_type'] == 'DECIMAL')
            _div += '<input id="'+key+'" type="number" placeholder="'+title+'" min="0" onpaste="return false;" class="form-control Check-length" value="'+$thisVO[key]+'"/>';
        else
            _div += '<input id="'+key+'" type="text" placeholder="'+title+'" class="form-control Check-length" value="'+$thisVO[key]+'" '+($thisVO[key+'_type'] == 'PRIMARY-KEY'?'disabled="true"':'')+'/>';
        _div += '</div></div>';
    });
    $('#dataDetail').empty().append(_div);
    $('#nav').addClass('display');
    $(document).unbind('keyup');
    bindLaydateClickEvent();
    bindCheckLengthKeyDownEvent();
    bindCheckLengthKeyUpEvent();
    bindUpdateDataClickEvent();
    bindJudgeCheckedClickeEvent();
    bindUploadPicClickEvent();
}

function bindJudgeCheckedClickeEvent() {
    $('.judgeChecked').unbind('click').on('click',function () {
        var key = $(this).parent().parent().attr('data-column_name');
        var this_value = $('.parameterInput input[name="'+key+'"]:checked').val();
        var column_value = $thisVO[key];
        if (this_value != column_value && isNotEmpty(this_value)) $(this).parent().parent().attr('is_update',true);
        else $(this).parent().parent().attr('is_update',false);
    });
}

function bindUpdateDataClickEvent() {
    $('#updateData').unbind('click').on('click',function () {
        updateVO.table_name = queryVO.table_name;
        updateVO.table_id = queryVO.table_id;
        updateVO.tableID = $thisVO[queryVO.table_id];
        updateVO.user_id = localStorage.getItem('user_id');
        updateVO.user_type = localStorage.getItem('user_type');
        updateVO.user_name = localStorage.getItem('user_name');
        updateVO.phone = localStorage.getItem('phone');
        updateVO.column_list = setUpdateColumnList();
        $.myajax({
            url: "systemAction/updateTableData",
            data: updateVO,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                toastrMessage("修改"+data.msg);
                $('#btnClose').click();
                loadContent();
            }
        });
    });
}

function setUpdateColumnList() {
    var update_list = '';
    $('.parameterInput').each(function () {
        if ($(this).attr('is_update') == false || isEmpty($(this).attr('is_update'))) return;
        var key = $(this).attr('data-column_name');
        var data_type = $(this).attr('data-type');
        var value = $(this).find('input').val();
        if (data_type == 'FILE') value = $(this).find('img').attr('src');
        else if (data_type == 'BOOLEAN') value = $('.parameterInput input[name="'+key+'"]:checked').val();
        if (isEmpty(key) || isEmpty(value)) return;
        var update = '{"column_name":"'+key+'","column_value":"'+value+'"}';
        if (isNotEmpty(update_list)) update_list += ','+ update;
        else  update_list = update;
    });
    if (isEmpty(update_list)) return '';
    return '['+update_list+']';
}

function bindCheckLengthKeyDownEvent() {
    $('.Check-length').keydown(function (event) {
        var key =  event.which;
        if (key == 189) return false;
        if (key == 187) return false;
        if (key == 109) return false;
        if (key == 107) return false;
        if (key == 229) return false;
    });
}

function bindCheckLengthKeyUpEvent(){
    $('.Check-length').keyup(function (event) {
        var value = $(this).val();
        var length = value.length;
        var thisLimit =  $thisVO[$(this).parent().attr('data-column_name')+'_length'];
        //假设长度限制为10
        if(length>thisLimit && isNotEmpty(thisLimit)){
            //截取前10个字符
            value = value.substring(0,thisLimit);
            $(this).val(value);
            toastrMessage('输入字符串或者数字超过数据库长度限制...');
        }
        if (value == $thisVO[$(this).parent().attr('data-column_name')]) $(this).parent().attr('is_update',false);
        else $(this).parent().attr('is_update',true);
    });
}

function bindjPlayerPlayClickEvent() {
    $('.jPlayer-play').unbind('click').on('click',function () {
        if ($(this).hasClass('jp-play')) {
            $(this).text('play');
            $(this).removeClass('jp-play');
            $("#jPlayer").jPlayer('pause');
        } else {
            $(this).text('pause');
            var file_url = AmrToMp3($(this).parent().attr('data-content'));
            $(this).addClass('jp-play');
            initjPlayer(file_url);
        }
    });
}

function bindLaydateClickEvent() {
    $('.laydate').on('click',function(e){
        laydate({istime: true, format: 'YYYY-MM-DD hh:MM:ss',min: laydate.now()});
        var column_value = (new Date($thisVO[$(this).parent().attr('data-column_name')])).getTime();
        var this_value = (new Date($(this).val())).getTime();
        if (this_value != column_value && isNotEmpty(this_value)) $(this).parent().attr('is_update',true);
    });
}

function AmrToMp3(str) {
    if (isEmpty(str)) return;
    var file_type = str.substring(accSub(str.length, 3), str.length);
    if (file_type == 'amr'|| file_type == 'AMR') {
        $.myajax({
            url: amr_to_mp3_action,
            data: {amrUrl: str},
            datatype: 'json',
            type: 'post',
            success: function (data) {
                if (data.success == true) console.log('转换成功...');
                else console.log('转换失败...');
            }
        });
        return str.replace('amr', 'mp3');
    } else return str;
}

function isIndexOf(str,str1) {
    var is = str.indexOf(str1);
    if (is >= 0) return true;
    else return false;
}

function initjPlayer(str) {
    $("#jPlayer").jPlayer({
        ready: function () {
            $(this).jPlayer("setMedia", {
                mp3: str
            });
        },
        swfPath: "/js",
        supplied: "mp3"
    }).jPlayer('play');
}

function isFile(str,type) {//true是图片/false是音频
    var flag = false; //状态
    var SuffixNames = [];
    if (type) SuffixNames = ["jpg","png","gif","JPG","PNG","GIF","BMP","JPEG","TIFF","bmp","jpeg","tiff"];
    else SuffixNames = ["mp3","mp4","amr","MP3","MP4","AMR"];
    for(var i in SuffixNames) {
        if (isIndexOf(str,SuffixNames[i])) {
            flag = true;
            break;
        }
    }
    return flag;
}

function isNotEmpty(str) {
    return str !== null && str !== ''&& str !== ""&& str !== '""'&& str !== undefined && str !== '[]'&& str !== '{}'&&str.length !=0;
}

function isEmpty(str) {
    return str === null || str === ''|| str === ""|| str === '""'|| str === undefined || str === '[]'||str === '{}'|| str.length ==0;
}

function toastrMessage(content) {
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