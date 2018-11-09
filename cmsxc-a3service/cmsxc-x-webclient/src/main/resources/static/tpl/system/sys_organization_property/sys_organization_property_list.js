/**
 * Created by qiaohao on 2018/2/1.
 */
app.controller('sys_organization_property_list_controller', ['$scope', '$http', '$modal', 'toaster','$compile', function ($scope, $http, $modal, toaster,$compile) {

    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_organization_property/findSysOrganizationPropertyByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_organization_property_table',
        //table的列
        dataTableColumn: [
            defaultCheckBox(),
            defaultDetail('orgPropertyName','detailSysOrganizationProperty','属性名称','40%',$compile,$scope),
            {title:'属性编码',data:'orgPropertyCode',width:'40%'},
        ],
        //列是单选还是多选 CheckBox多选 Radio单选
        dataTableSelectType: CheckBox
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.orgPropertyName = $scope.orgPropertyName;
        params.orgPropertyCode = $scope.orgPropertyCode;
        return params;
    }

    //创建dataTable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);


    $scope.searchSysOrganizationProperty = function(){
        $scope.dataTable.fnDraw(true);
    }

    $scope.resetSysOrganizationProperty = function(){
        $scope.orgPropertyName = "";
        $scope.orgPropertyCode = "";
        $scope.dataTable.fnDraw(true);//框架内部方法
    }


    $scope.saveSysOrganizationProperty = function(){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_organization_property/sys_organization_property_save.html'+getCacheTime(),
            controller: 'sys_organization_property_save_controller',
            resolve:{
            }
        });
        rtn.result.then(function (status) {
            if(status == Response.successMark) {
                toaster_success('添加组织机构属性信息成功',toaster);
                $scope.dataTable.fnDraw(true);
            }
        },function(){
        });
    }

    $scope.modifySysOrganizationProperty = function(sysOrgProId){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键

        if(rowsIds.length < 1)
            modalAlert($modal,'请您选择需要修改的数据！');
        else if(rowsIds.length > 1)
            modalAlert($modal,'只能同时修改一条数据！');
        else{

            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/sys_organization_property/sys_organization_property_modify.html'+getCacheTime(),
                controller: 'sys_organization_property_modify_controller',
                resolve:{
                    sysOrgProId : function (){ return rowsIds[0] }
                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    toaster_success('编辑组织机构属性信息成功', toaster);
                    $scope.dataTable.fnDraw(true);
                }
            },function(){
            });

        }

    }


    $scope.detailSysOrganizationProperty = function(sysOrgProId){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_organization_property/sys_organization_property_detail.html'+getCacheTime(),
            controller: 'sys_organization_property_detail_controller',
            resolve:{
                sysOrgPro : function (){ return $scope.dataTable.getRow(sysOrgProId) }
            }
        });
        rtn.result.then(function (status) {

        },function(){
        });
    }

    $scope.deleteSysOrganizationProperty = function(){
        var rowsIds =  $scope.dataTable.getRowsIds();
        if(rowsIds.length < 1){
            modalAlert($modal,'请选择要删除的数据');
        }else{

            modalConfirm($modal,function(){
                $http.post('sys_organization_property/deleteSysOrganizationPropertyByIds',rowsIds).success(function (data) {
                    if(data.code == Response.successCode) {
                        toaster_success('删除组织机构属性信息成功', toaster);
                        $scope.dataTable.fnDraw(true);
                    }
                    else
                        modalAlert($modal,data.message);
                })
            },null,'您确定需要删除吗？')

        }
    }

}])
;