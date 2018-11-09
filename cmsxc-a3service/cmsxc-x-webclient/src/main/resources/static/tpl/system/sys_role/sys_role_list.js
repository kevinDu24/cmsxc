/**
 * Created by qiaohao on 2018/1/9.
 */
app.controller('sys_role_list_controller', ['$scope', '$http', '$modal', 'toaster','$compile', function ($scope, $http, $modal, toaster,$compile) {

    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_role/findSysRoleByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_role_table',
        //table的列
        dataTableColumn: [
            defaultCheckBox(),
            {title:'角色名称',data:'roleName',width:'400px'},
        ],
        //列是单选还是多选 CheckBox多选 Radio单选
        dataTableSelectType: CheckBox
    }

    //请求的参数
     function dataTableParams($scope){
        params = {};
        params.roleName = $scope.roleName;
        return params;
    }

    //创建dataTable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);


    $scope.searchSysRole = function(){
        $scope.dataTable.fnDraw(true);
    }

    $scope.resetSysRole = function(){
        $scope.roleName="";
        $scope.dataTable.fnDraw(true);//框架内部方法
    }


    $scope.saveSysRole = function(){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_role/sys_role_save.html',
            controller: 'sys_role_save_controller',
            resolve:{
            }
        });
        rtn.result.then(function (status) {
            if(status == Response.successMark) {
                toaster_success('新增角色信息成功',toaster);
                $scope.dataTable.fnDraw(true);
            }
        },function(){
        });
    }



    $scope.deleteSysRole = function(){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键
        if(rowsIds.length < 1){
            modalAlert($modal,'请您选择需要删除的数据！');
        }else{
            var del = {"rowsIds":rowsIds};
            modalConfirm($modal,function(){
                $http.post('sys_role/deleteSysRole',del).success(function (data) {
                    if(data.code == Response.successCode) {
                        toaster_success('删除角色信息成功', toaster);
                        $scope.dataTable.fnDraw(true);
                    }else
                        modalAlert($modal,data.message);
                })
            },null,'您确定需要删除吗？');

        }
    }


    $scope.modifySysRole = function(sysRoleId){

        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键

        if(rowsIds.length < 1)
            modalAlert($modal,'请您选择需要修改的数据！');
        else if(rowsIds.length > 1)
            modalAlert($modal,'只能同时修改一条数据！');
        else{
            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/sys_role/sys_role_modify.html',
                controller: 'sys_role_modify_controller',
                resolve:{
                    sysRoleId : function (){ return rowsIds[0] }
                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    toaster_success('编辑角色信息成功',toaster);
                    $scope.dataTable.fnDraw(true);
                }
            },function(){
            });
        }

    }


    $scope.detailSysRole = function(sysRoleId){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_role/sys_role_detail.html',
            controller: 'sys_role_detail_controller',
            resolve:{
                sysRoleId : function (){ return sysRoleId }
            }
        });
        rtn.result.then(function (status) {
            if(status == 'SUCCESS') {
                $scope.pop('success', '', '新增角色信息成功');
                $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
            }
        },function(){
        });
    }


    $scope.batchDelSysRole = function(){
        var rowsIds =  $scope.dataTable.getRowsIds();
        if(rowsIds.length < 1){
            alert('请选择要删除的数据');
        }else{
            $http.post('sys_role/deleteSysRoleByIds',rowsIds).success(function (data) {
                if(data.code == Response.successCode) {
                    toaster_success('删除角色信息成功', toaster);
                    $scope.dataTable.fnDraw(true);
                }
                else
                    alert(data.message);
            })
        }
    }


}])
;