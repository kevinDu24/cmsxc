/**
 * Created by qiaohao on 2018/2/1.
 */
app.controller('sys_data_dictionary_list_select_controller', ['$scope', '$http', '$modal', 'toaster','$compile','$modalInstance', function ($scope, $http, $modal, toaster,$compile,$modalInstance) {

    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_data_dictionary/findSysDataDictionaryByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_data_dictionary_select_table',
        //table的列
        dataTableColumn: [
            defaultCheckBox(),
            {title:'字典名称',data:'dataDicName',width:'20%'},
            {title:'字典编码',data:'dataDicCode',width:'20%'},
            {title:'状态',data:'dataDicDisable',width:'20%'},
        ],
        //列是单选还是多选 CheckBox多选 Radio单选
        dataTableSelectType: Radio
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.dataDicName = $scope.dataDicName;
        params.dataDicCode = $scope.dataDicCode;
        return params;
    }

    $scope.init = function(){
        //创建dataTable
        $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);
    }

    $scope.searchSysDataDictionary = function(){
        $scope.dataTable.fnDraw(true);
    }

    $scope.resetSysDataDictionary = function(){
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
            var data = {id:0,dataDicName:'无上级'};
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