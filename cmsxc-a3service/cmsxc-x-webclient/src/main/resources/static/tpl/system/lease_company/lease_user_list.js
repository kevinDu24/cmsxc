/**
 * Created by yuanzhenxia on 2018/1/9.
 */
app.controller('lease_user_list_controller', ['$scope', '$http', '$modal', 'toaster','$compile', function ($scope, $http, $modal, toaster,$compile) {
    /******************请求列表初始化开始*******************/
    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'lease_user/findLeaseUserByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'lease_company_table',
        //table的列
        dataTableColumn: [
            //操作 checkbox或radio
            defaultCheckBox(),
            {title:'公司简称',data:'leaseShortName',width:'20%'},
            {title:'公司全称',data:'leaseFullName',width:'25%'},
            {title:'联系人姓名',data:'contactName',width:'10%'},
            {title:'联系人电话',data:'contactPhone',width:'15%'}
            //操作按钮
            // defaultHandle('searchLeaseUserr','saveLeaseUser','deleteLeaseyUser',$compile,$scope)
        ],
        //列是单选还是多选 CheckBox 多选 Radio 单选
        dataTableSelectType: Radio
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.leaseShortName = $scope.leaseShortName;
        params.leaseFullName = $scope.leaseFullName;
        return params;
    }

    //创建dataTable 封装了datatable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);

    /******************请求列表初始化结束*******************/


    /******************请求列表查询开始*******************/
    $scope.searchLeaseUser = function(){
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    /******************请求列表查询结束*******************/


    $scope.saveLeaseUser = function(){
        var rtn = $modal.open({
            backdrop : 'static',
            //size:'lg',
            templateUrl: 'tpl/system/lease_company/create_lease_user.html?datetime='+getTimestamp(),
            controller: 'create_lease_user_controller',
            resolve:{
            }
        });
        rtn.result.then(function (status) {
            if(status == Response.successMark) {
                toaster_success('添加用户信息成功',toaster);
                $scope.dataTable.fnDraw(true);
            }
        },function(){
        });
    }
    $scope.deleteLeaseUser = function(){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键
        if(rowsIds.length < 1){
            modalAlert($modal,'请您选择需要删除的数据！');
        }else{
            var leaseId = {"rowsIds":rowsIds};
            modalConfirm($modal,function(){
                $http.post('lease_user/deleteLeaseUser',leaseId).success(function (data) {
                    if(data.code == Response.successCode) {
                        toaster_success('删除委托公司信息成功', toaster);
                        $scope.dataTable.fnDraw(true);
                    }else
                        modalAlert($modal,data.message);
                })
            },null,'您确定需要删除吗？');

        }
    }


    $scope.modifyLeaseUser = function(sysRoleId){
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

}])
;