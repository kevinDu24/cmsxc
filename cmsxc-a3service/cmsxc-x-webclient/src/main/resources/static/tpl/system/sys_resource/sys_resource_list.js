/**
 * Created by qiaohao on 2018/1/9.
 */
app.controller('sys_resource_list_controller', ['$scope', '$http', '$modal', 'toaster','$compile', function ($scope, $http, $modal, toaster,$compile) {

    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_resource/findSysResByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_resource_table',
        //table的列
        dataTableColumn: [
            defaultCheckBox(),
            {title:'菜单名称',data:'description',width:'82%'},
        ],
        //列是单选还是多选 CheckBox多选 Radio单选
        dataTableSelectType: CheckBox
    }

    //请求的参数
     function dataTableParams($scope){
        params = {};
        params.description = $scope.description;
        return params;
    }

    //创建dataTable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);


    $scope.searchSysResource = function(){
        $scope.dataTable.fnDraw(true);
    }

    $scope.resetSysResource = function(){
        $scope.description="";
        $scope.dataTable.fnDraw(true);//框架内部方法
    }


    $scope.saveSysResource = function(){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_resource/sys_resource_save.html'+getCacheTime(),
            controller: 'sys_resource_save_controller',
            resolve:{
            }
        });
        rtn.result.then(function (status) {
            if(status == Response.successMark) {
                toaster_success('添加菜单信息成功',toaster);
                $scope.dataTable.fnDraw(true);
            }
        },function(){
        });
    }

    $scope.modifySysResource = function(sysResourceId){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键

        if(rowsIds.length < 1)
            modalAlert($modal,'请您选择需要修改的数据！');
        else if(rowsIds.length > 1)
            modalAlert($modal,'只能同时修改一条数据！');
        else{

            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/sys_resource/sys_resource_modify.html'+getCacheTime(),
                controller: 'sys_resource_modify_controller',
                resolve:{
                    sysResourceId : function (){ return rowsIds[0] }
                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    toaster_success('编辑菜单信息成功', toaster);
                    $scope.dataTable.fnDraw(true);
                }
            },function(){
            });

        }

    }


    $scope.detailSysResource = function(sysResourceId){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_resource/sys_resource_detail.html'+getCacheTime(),
            controller: 'sys_resource_detail_controller',
            resolve:{
                sysRes : function (){ return $scope.dataTable.getRow(sysResourceId) }
            }
        });
        rtn.result.then(function (status) {

        },function(){
        });
    }

    $scope.deleteSysResource = function(){
        var rowsIds =  $scope.dataTable.getRowsIds();
        if(rowsIds.length < 1){
            alert('请选择要删除的数据');
        }else{

            modalConfirm($modal,function(){
                $http.post('sys_resource/deleteSysResByIds',rowsIds).success(function (data) {
                    if(data.code == Response.successCode) {
                        toaster_success('删除菜单信息成功', toaster);
                        $scope.dataTable.fnDraw(true);
                    }
                    else
                        alert(data.message);
                })
            },null,'您确定需要删除吗？')

        }
    }

}])
;