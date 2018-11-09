/**
 * Created by qiaohao on 2018/2/1.
 */
app.controller('sys_organization_list_select_controller', ['$scope', '$http', '$modal', 'toaster','$compile','$modalInstance', function ($scope, $http, $modal, toaster,$compile,$modalInstance) {

    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_organization/findSysOrganizationByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_organization_select_table',
        //table的列
        dataTableColumn: [
            defaultCheckBox(),
            {title:'组织机构名称',data:'orgName',width:'40%'},
            {title:'组织机构编码',data:'orgCode',width:'40%'},
        ],
        //列是单选还是多选 CheckBox多选 Radio单选
        dataTableSelectType: CheckBox
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.orgName = $scope.orgName;
        params.orgCode = $scope.orgCode;
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
        $scope.description="";
        $scope.dataTable.fnDraw(true);//框架内部方法
    }

    $scope.confirm = function(status){
        if(status != 'none') {
            var data = $scope.dataTable.getRow();
            if(data == null)
                modalAlert($modal,'请选择上级');
            else
                $modalInstance.close(data);
        }else{
            var data = {id:0,orgName:'组织机构'};
            $modalInstance.close(data);
        }
    }


    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(){
        $modalInstance.close(null);
    };


}])
;