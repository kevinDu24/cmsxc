/**
 * Created by qiaohao on 2018/2/1.
 */
app.controller('sys_organization_property_list_select_controller', ['$scope', '$http', '$modal', 'toaster','$compile','$modalInstance', function ($scope, $http, $modal, toaster,$compile,$modalInstance) {

    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_organization_property/findSysOrganizationPropertyByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_organization_property_select_table',
        //table的列
        dataTableColumn: [
            defaultCheckBox(),
            {title:'属性名称',data:'orgPropertyName',width:'40%'},
            {title:'属性编码',data:'orgPropertyCode',width:'40%'}
        ],
        //列是单选还是多选 CheckBox多选 Radio单选
        dataTableSelectType: Radio
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.orgPropertyName = $scope.orgPropertyName;
        params.orgPropertyCode = $scope.orgPropertyCode;
        return params;
    }

    $scope.init = function(){
        //创建dataTable
        $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);
    }

    $scope.searchSysOrganizationProperty = function(){
        $scope.dataTable.fnDraw(true);
    }

    $scope.resetSysOrganizationProperty = function(){
        $scope.orgPropertyName = "";
        $scope.orgPropertyCode = "";
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
            var data = {id:0,orgPropertyName:'无组织属性'};
            $modalInstance.close(data);
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