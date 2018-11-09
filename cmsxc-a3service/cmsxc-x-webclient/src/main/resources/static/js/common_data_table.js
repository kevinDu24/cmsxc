/**
 * Created by qiaohao on 2018/1/10.
 */


var CheckBox = "checkBox";
var Radio = "radio";

/**
 * 将id设置为checkbok 可单多选
 * @param idName
 * @param idCheckBoxName
 * @returns {{title: string, data: string, render: render}}
 */
function defaultCheckBox(idName,idCheckBoxName){
    var dataName = replaceIdData(idName);
    var dataCheckBoxName = replaceIdData('ids',idCheckBoxName);
    return {title:'<label class="i-checks i-checks-sm m-b-none"><input type="checkbox" id="all_checked"><i></i></label>',
        data:dataName,
        width:'3%',
        render: function(data,type,row,meta){
            return '&nbsp;<label class="i-checks i-checks-sm m-b-none"><input type="checkbox"  value="'+data+'" name="'+dataCheckBoxName+'"><i></i></label>'
        }
    };
}



function defaultHandle(modifyName,detailName,deleteName,$compile,$scope) {
    return {
        title: '操作',
        data: 'id',
        width:'10%',
        render: function (data, type, row, meta) {
            return '<button class="btn m-b-xxs btn-xs btn-info text-center" ng-click="'+modifyName+'(\'' + data + '\')"><i class="icon-pencil"></i>编辑</button>&nbsp;' +
                '<button  class="btn m-b-xxs btn-xs btn-info text-center" ng-click="'+detailName+'(\'' + data + '\')"><i class="icon-eye"></i>查看</button>&nbsp;' +
                '<button class="btn m-b-xxs btn-xs btn-info text-center" ng-click="'+deleteName+'(\''+data+'\')"><i class="fa fa-trash-o"></i>删除</button>';
        },
        fnCreatedCell: function (nTd, sData, oData, iRow, iCol) {
            $compile(nTd)($scope);
        }
    }
}

function defaultDetail(rowName,detailFucName,title,width,$compile,$scope,idName){
    var dataName = replaceIdData(idName);
    return {
        title: title,
        data: dataName,
        width:width,
        render: function (data, type, row, meta) {
            return '<a class="a1" ng-click="'+detailFucName+'(\'' + data + '\')">'+row[rowName]+'</a>';
        },
        fnCreatedCell: function (nTd, sData, oData, iRow, iCol) {
            $compile(nTd)($scope);
        }
    }
}


/**
 * 创建 dataTable
 * @param dataTableProperties
 * @param dataTableParams
 * @param $scope
 * @returns {*|jQuery}
 */
function createTable(dataTableProperties,dataTableParams,$scope,trSelectedEvent){

    var dataTableAjax = dataTableProperties.dataTableAjax;
    var dataTableId = dataTableProperties.dataTableId;
    var properties = {
        ajax: function(data,callback,settings){
            $.ajax({
                url: dataTableAjax.url,
                type: dataTableAjax.type,
                data: $.extend(dataTableParams($scope),data),
                success: function(result) {
                    callback(result.data);
                }
            });
        },
        ordering : false,
        scrollX: true,
        aoColumns: dataTableProperties.dataTableColumn,
        serverSide : true,
        selectType: dataTableProperties.dataTableSelectType,
        language: {
            "sProcessing": "处理中...",
            "sLengthMenu": "每页显示 _MENU_ 条信息",
            "sZeroRecords": "没有匹配结果",
            "sInfo": ",共 _TOTAL_ 条信息<select class='form-control input-sm' style='visibility: hidden; ' />",
            "sInfoEmpty": "显示第 0 至 0 条信息，共 0 条",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            },

        },


        "sDom": "t<'row'><'row'<'col-md-6'<l><i>><'col-md-6 pull-right'p>>",
        "fnInitComplete": $.proxy(function (oSettings, json) {
            //行单选
            if (oSettings.oInit.selectType == Radio) {
                $('#' + dataTableId + ' tbody').on('click', 'tr', function (event) {
                    var ck = $(this).find('input:checkbox');
                    if (!ck.prop('checked')) {
                        $('#' + dataTableId).find('tr input:checkbox').prop('checked', false);
                        ck.prop('checked', true);
                        if(isNotUndefined(trSelectedEvent))
                            trSelectedEvent(ck.val(),$scope);
                    } else {
                        $(this).find('input:checkbox').prop('checked', false);
                    }
                    event.stopPropagation();
                });
            } else if (oSettings.oInit.selectType == CheckBox) {
                $('#' + dataTableId + ' tbody').on('click', 'tr', function () {
                    var ck = $(this).find('input:checkbox');
                    ck.prop('checked', !ck.prop('checked'));
                    if(ck.prop('checked')){
                        if(isNotUndefined(trSelectedEvent))
                            trSelectedEvent(ck.val(),$scope);
                    }

                    if ($('#'+ dataTableId +' [name=ids]:checkbox:checked').length == $('#'+ dataTableId +' [name=ids]:checkbox').length)
                        $("#"+dataTableId+"_wrapper [name=all_checked]").prop('checked', true);
                    else
                        $("#"+dataTableId+"_wrapper [name=all_checked]").prop('checked', false);
                })
            }

            if (oSettings.oInit.selectType == CheckBox) {
                $("#"+dataTableId+"_wrapper [name=all_checked]").prop('checked', false);
                $("#"+dataTableId+"_wrapper [name=all_checked]").click(function () {
                    $("#"+dataTableId+" [name=ids]:checkbox").prop('checked', this.checked);
                });
            } else {
                $("#"+dataTableId+"_wrapper [name=all_checked]").prop('disabled', true);
            }

            $('#' + dataTableId + ' [name=ids]:checkbox').click(function (event) {
                event.stopPropagation();
                return false;
            });
        }, this)
    }
    var dataTable = $("#"+dataTableId).dataTable(properties);

    dataTable.getRows = function(){
        var nTrs = dataTable.fnGetNodes();
        var rows = [];
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find('[name=ids]:checkbox').prop('checked')){
                rows.push(dataTable.fnGetData(nTrs[i]));
            }
        }
        return rows;
    }

    /**
     *
     * @param id id数据
     * @param idName id的名称  默认为id  针对于名称非id的拓展
     * @returns {*}
     */
    dataTable.getRow = function(id,idName){
        var dataName = replaceIdData(idName);
        var nTrs = dataTable.fnGetNodes();
        if(isNotUndefined(id) && isNotNull(id)) {
            for(var i = 0; i < nTrs.length; i++){
                getData = dataTable.fnGetData(nTrs[i]);
                if (getData[dataName] == id){
                    return getData
                }
            }
        }else{
            for(var i = 0; i < nTrs.length; i++){
                if($(nTrs[i]).find('[name=ids]:checkbox').prop('checked')){
                    return dataTable.fnGetData(nTrs[i]);
                }
            }
        }
        return null;
    }


    dataTable.getRowsIds = function(idName){
        var dataName = replaceIdData(idName);
        var nTrs = dataTable.fnGetNodes();
        var rows = [];
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find('[name=ids]:checkbox').prop('checked')){
                rows.push(dataTable.fnGetData(nTrs[i])[dataName]);
            }
        }
        return rows;
    }

    dataTable.getRowId = function(idName){
        var dataName = replaceIdData(idName);
        var nTrs = dataTable.fnGetNodes();
        var rows = [];
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find('[name=ids]:checkbox').prop('checked')){
                return dataTable.fnGetData(nTrs[i])[dataName];
            }
        }
        return null;
    }

    return dataTable;

}

function replaceIdData(newData){
    var dataName = 'id';
    if(isNotUndefined(newData) && isNotNull(newData))
        dataName = newData;
    return dataName;
}


function replaceData(oldData,newData){
    if(isNotUndefined(newData) && isNotNull(newData))
        oldData = newData;
    return oldData;
}

function retsetColumnCodeToDes(title,column,$compile,$scope) {
    return {
        title: title,
        data: 'id',
        width:'120px',
        render: function (data, type, row, meta) {
            return '<span translate="code.'+column+'.'+row[column]+'">'+row[column]+'</span>';
        },
        fnCreatedCell: function (nTd, sData, oData, iRow, iCol) {
            $compile(nTd)($scope);
        }
    }
}
