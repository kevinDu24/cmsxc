/**
 * Created by qiaohao on 2018/1/9.
 */
app.controller('sys_user_list_select_controller', ['$scope', '$http', '$modal','$modalInstance', 'toaster','$compile', function ($scope, $http, $modal,$modalInstance, toaster,$compile) {
    /******************请求列表初始化开始*******************/
    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_user/findSysUserByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_user_select_table',

        //table的列
        dataTableColumn: [
            //操作 checkbox或radio
            defaultCheckBox(),
            {title:'用户账号',data:'username',width:'200px'},
            {title:'用户姓名',data:'realName',width:'200px'},
            {title:'用户手机',data:'phone',width:'120px'},
            {title:'用户座机',data:'landLine',width:'120px'},
            retsetColumn('性别','gender',$compile,$scope),
            {title:'Email',data:'email',width:'120px'},

            //拓展操作按钮
            //defaultHandle('modifySysUser','detailSysUser','deleteSysUser',$compile,$scope)
        ],

        //列是单选还是多选 CheckBox 多选 Radio 单选
        dataTableSelectType: Radio
    }

    //请求的参数
     function dataTableParams($scope){
        params = {};
        params.username = $scope.username;
        params.realName = $scope.realName;
        return params;
    }


     $scope.init = function(){
         //创建dataTable 封装了datatable
         $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);
     }

    /******************请求列表初始化结束*******************/


    /******************请求列表查询开始*******************/
    $scope.searchSysUser = function(){
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    $scope.resetSysUser = function(){
        $scope.username="";
        $scope.realName="";
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    /******************请求列表查询结束*******************/


    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(){
        $modalInstance.close(null);
    };


    $scope.confirm = function(status){
        if(status != 'none') {
            var data = $scope.dataTable.getRow();
            if(data == null)
                modalAlert($modal,'请选择上级');
            else
                $modalInstance.close(data);
        }else{
            var data = {id:0,realName:'无上级'};
            $modalInstance.close(data);
        }
    }

}])
;