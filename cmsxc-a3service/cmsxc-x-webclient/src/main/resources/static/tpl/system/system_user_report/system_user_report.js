/**
 * Created by qiaohao on 2018/1/9.
 *
 * 账户注册列表及报表导出
 */
app.controller('system_user_report_controller', ['$scope', '$http', '$modal', 'toaster','$compile','$filter', '$localStorage','$window',function ($scope, $http, $modal, toaster,$compile,$filter,$localStorage, $window) {
    // 初始化
    $scope.loginUser=$localStorage.user;
    $scope.userId="";
    $scope.userName="";
    $scope.userRole = "";
    $scope.startDate = "";
    $scope.endDate = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.recoveryCompanyId = "";
    var loginUserRole = $scope.loginUser.userRole;
    if(loginUserRole == '0'){
        $scope.adminFlag = true;
        $scope.leaseAdminFlag = false;
    }else if(loginUserRole == '11'|| loginUserRole == '12'){
        $scope.adminFlag = false;
        $scope.leaseAdminFlag = true;
    }
    init();
    function init() {
        // 取得收车公司
        $http.get('sys_user/getRecoveryCompanys').success(function(data){
            $scope.recoveryCompanysVoList = data.data;
        });

    }
    /******************请求列表初始化开始*******************/
    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'sys_user/findSysUserListByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'sys_user_report_table',

        //table的列
        dataTableColumn: [
            //操作 checkbox或radio
            // defaultCheckBox(),
            {title:'手机号',data:'userId',width:'100px'},
            {title:'姓名',data:'userName',width:'100px'},
            {title:'角色',data:'userRole',width:'120px'},
            {title:'所属收车公司',data:'recoveryFullName',width:'120px'},
            {title:'注册时间',data:'createTime',width:'120px'},
            {title:'当前积分',data:'totalScore',width:'120px'},
            {title:'扫描总量',data:'scanCount',width:'120px'},
            //拓展操作按钮
            //defaultHandle('modifySysUser','detailSysUser','deleteSysUser',$compile,$scope)
        ],

        //列是单选还是多选 CheckBox 多选 Radio 单选
        dataTableSelectType: Radio
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.userId = $scope.userId;
        params.userName = $scope.userName;
        params.userRole = $scope.userRole;
        params.startDate = $('#startDate').val();
        params.endDate = $('#endDate').val();
        params.recoveryCompanyId = $scope.recoveryCompanyId;
        return params;
    }

    //创建dataTable 封装了datatable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope);

    /******************请求列表初始化结束*******************/


    /******************请求列表查询开始*******************/
    $scope.search = function(){
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    // 重置画面参数
    $scope.reset = function(){
        $scope.userId="";
        $scope.userName="";
        $scope.userRole = "";
        $('#startDate').val("");
        $('#endDate').val($filter('date')(new Date(), 'yyyy-MM-dd'));
        $scope.recoveryCompanyId = "";
        $scope.dataTable.fnDraw(true);//框架内部方法
    }
    /******************请求列表查询结束*******************/

    /******************报表导出*******************/
    $scope.export = function (){
        $scope.startDate = $('#startDate').val();
        $scope.endDate = $('#endDate').val();
        var url = 'excel/exportSysUserExcel?userId=' + $scope.userId + '&userName='+$scope.userName
            + '&userRole=' + $scope.userRole + '&startDate=' + $scope.startDate + '&endDate=' + $scope.endDate+ '&recoveryCompanyId=' + $scope.recoveryCompanyId;
        var popup  = $window.open("about:blank", "_blank");
        popup.location = url;
    }


}])
;