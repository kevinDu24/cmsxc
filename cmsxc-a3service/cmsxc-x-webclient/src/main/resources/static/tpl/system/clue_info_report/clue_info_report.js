/**
 * Created by yuanzhenxia on 2018/1/9.
 *
 * 线索扫描列表及报表导出
 */
app.controller('clue_info_report_controller', ['$scope', '$http', '$modal', 'toaster','$compile','$filter', '$localStorage','$window',function ($scope, $http, $modal, toaster,$compile,$filter,$localStorage, $window) {
    // 初始化
    $scope.loginUser=$localStorage.user;
    var loginUserRole = $scope.loginUser.userRole;
    $scope.plate="";
    $scope.targetFlag="";
    $scope.startDate = "";
    $scope.endDate = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.recoveryCompanyId = "";
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
            url : 'clueinfo/findClueInfoListByPage',
            type:"GET",
        },
        //table的html id
        dataTableId:'clue_info_report_table',

        //table的列
        dataTableColumn: [
            //操作 checkbox或radio
            // defaultCheckBox(),
            {title:'扫描内容',data:'type',width:'100px',
                render: function (data, type, row, meta) {
                    if(row.type == '0')
                        return row.plate;
                    else if(row.type == '1')
                        return row.vehicleIdentifyNum;
                    else
                        return '';
                }
            },
            {title:'命中状态',data:'targetFlag',width:'100px'},
            {title:'地址',data:'appAddr',width:'180px'},
            {title:'时间',data:'uploadDate',width:'120px'},
            {title:'账号',data:'userId',width:'120px'},
            {title:'所属收车公司',data:'recoveryFullName',width:'120px'},
            //拓展操作按钮
            //defaultHandle('modifySysUser','detailSysUser','deleteSysUser',$compile,$scope)
        ],
        //列是单选还是多选 CheckBox 多选 Radio 单选
        dataTableSelectType: Radio
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.plate = $scope.plate;
        params.targetFlag = $scope.targetFlag;
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
        $scope.plate="";
        $scope.targetFlag="";
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
        var url = 'excel/exportClueInfoExcel?plate=' + $scope.plate + '&targetFlag='+$scope.targetFlag
            + '&startDate=' + $scope.startDate + '&endDate=' + $scope.endDate+ '&recoveryCompanyId=' + $scope.recoveryCompanyId;
        var popup  = $window.open("about:blank", "_blank");
        popup.location = url;
    }


}])
;