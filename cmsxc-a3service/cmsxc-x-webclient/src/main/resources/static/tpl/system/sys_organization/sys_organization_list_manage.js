/**
 * Created by qiaohao on 2018/2/1.
 */
app.controller('sys_organization_list_manage_controller', ['$scope', '$http', '$modal', 'toaster','$compile','$modalInstance','sysOrg', function ($scope, $http, $modal, toaster,$compile,$modalInstance,sysOrg) {

    $scope.orgParentId = sysOrg.id;
    $scope.orgParentName = sysOrg.orgName;


    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_organization/findSysOrganizationVoByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_organization_manage_table',
        //table的列
        dataTableColumn: [
            defaultCheckBox(),
            defaultDetail('orgName','detailSysOrganization','组织机构名称','20%',$compile,$scope),
            {title:'组织机构编码',data:'orgCode',width:'20%'},
            {title:'上级组织机构名称',data:'orgParentName',width:'20%',
                render: function (data, type, row, meta) {
                    if(row.orgParentId == 0)
                        return '组织机构';
                    else
                        return data;
                }
            },
            {
                title: '组织属性', data: 'orgPropertyName', width: '20%',
                render: function (data, type, row, meta) {
                    if (row.orgPropertyId == 0)
                        return '无组织属性';
                    else
                        return data;
                }
            },
            {
                title: '组织机构类型', data: 'orgType', width: '20%',
                render: function (data, type, row, meta) {
                    if(data == 0){
                        return "公司/集团";
                    }else if(data == 1){
                        return  "区域";
                    }else if(data == 2){
                        return "分公司";
                    }else if(data == 3){
                        return "SP";
                    }else
                        return data;
                }
            },
            {title:'状态',data:'orgDisable',width:'20%',
                render: function (data, type, row, meta) {
                    if(data == 0)
                        return '启用';
                    else
                        return '禁用';
                }
            }
        ],
        //列是单选还是多选 CheckBox多选 Radio单选
        dataTableSelectType: CheckBox
    }




    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.orgName = $scope.orgName;
        params.orgCode = $scope.orgCode;
        params.orgParentId = $scope.orgParentId;
        return params;
    }


    $scope.init = function(){
        //创建dataTable
        $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);
    }


    $scope.searchSysOrganization = function(){
        $scope.dataTable.fnDraw(true);
    }

    $scope.resetSysOrganization = function(){
        $scope.orgName = '';
        $scope.orgCode = '';
        $scope.dataTable.fnDraw(true);
    }


    $scope.saveSysOrganization = function(){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_organization/sys_organization_save.html'+getCacheTime(),
            controller: 'sys_organization_save_controller',
            resolve:{
            }
        });
        rtn.result.then(function (status) {
            if(status == Response.successMark) {
                toaster_success('添加组织机构信息成功',toaster);
                $scope.dataTable.fnDraw(true);
            }
        },function(){
        });
    }

    $scope.modifySysOrganization = function(sysOrgId){
        var rowsIds =  $scope.dataTable.getRowsIds('id');//主键

        if(rowsIds.length < 1)
            modalAlert($modal,'请您选择需要修改的数据！');
        else if(rowsIds.length > 1)
            modalAlert($modal,'只能同时修改一条数据！');
        else{

            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/sys_organization/sys_organization_modify.html'+getCacheTime(),
                controller: 'sys_organization_modify_controller',
                resolve:{
                    sysOrgId : function (){ return rowsIds[0] }
                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    toaster_success('编辑组织机构信息成功', toaster);
                    $scope.dataTable.fnDraw(true);
                }
            },function(){
            });

        }

    }


    $scope.detailSysOrganization = function(sysOrgId){
        var rtn = $modal.open({
            backdrop : 'static',
            size:'lg',
            templateUrl: 'tpl/system/sys_organization/sys_organization_detail.html'+getCacheTime(),
            controller: 'sys_organization_detail_controller',
            resolve:{
                sysOrg : function (){ return $scope.dataTable.getRow(sysOrgId) }
            }
        });
        rtn.result.then(function (status) {

        },function(){
        });
    }

    $scope.deleteSysOrganization = function(){
        var rowsIds =  $scope.dataTable.getRowsIds();
        if(rowsIds.length < 1){
            alert('请选择要删除的数据');
        }else{

            modalConfirm($modal,function(){
                $http.post('sys_organization/deleteSysOrganizationByIds',rowsIds).success(function (data) {
                    if(data.code == Response.successCode) {
                        toaster_success('删除组织机构信息成功', toaster);
                        $scope.dataTable.fnDraw(true);
                    }
                    else
                        alert(data.message);
                })
            },null,'您确定需要删除吗？')
        }
    }

    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };


}])
;