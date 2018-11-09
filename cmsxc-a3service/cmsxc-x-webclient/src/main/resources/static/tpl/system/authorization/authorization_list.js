/**
 * Created by yuanzhenxia on 2018/1/9.
 */
app.controller('authorization_list_controller', ['$scope', '$http', '$modal', 'toaster','$compile', function ($scope, $http, $modal, toaster,$compile) {

    // 画面按钮初始化
    $scope.classDisabale = "btn btn-default btn-sm pull-left m-b-sm m-r-xs";
    $scope.classEnabale = "btn btn-success btn-sm pull-left m-b-sm m-r-xs";
    $scope.class = $scope.classDisabale;
    $scope.isDisable = true;
    $scope.classDelay = $scope.classDisabale;
    $scope.isDisableDelay = true;
    $scope.applicantName = "";
    $scope.vehiclePlate = "";
    $scope.vehicleIdentifyNum = "";
    $scope.userId = "";
    $scope.applicantPhone = "";

    /******************请求列表初始化开始*******************/
    //参数配置
    $scope.dataTableProperties= {
        //ajax url 和类型
        dataTableAjax : {
            url : 'authorization/findAuthorizationListByPage',
            type:"GET"
        },
        //table的html id
        dataTableId:'authorization_table',
        // 选中的列
        selectedItems: $scope.mySelections,
        //table的列
        dataTableColumn: [
            //操作 checkbox或radio
            defaultCheckBox(),
            {title:'授权状态',data:'authorizationStatus'},
            {title:'移动端用户名',data:'userId'},
            {title:'申请人姓名',data:'applicantName'},
            {title:'申请人手机号',data:'applicantPhone'},
            {title:'申请人身份证',data:'applicantIdentityNum'},
            {title:'申请备注',data:'remark'},
            {title:'审批备注',data:'approvalRemark'},
            {title:'车主姓名',data:'name'},
            {title:'车牌号',data:'plate'},
            {title:'服务费',data:'serviceFeeValue'},
            {title:'车辆所在省',data:'vehicleProvince'},
            {title:'车辆所在市',data:'vehicleCity'},
            {title:'车架号',data:'vehicleIdentifyNum'},
            {title:'发动机',data:'engineNo'},
            {title:'车型',data:'vehicleType'},
            {title:'车辆颜色',data:'vehicleColor'},
            {title:'开始日期',data:'applyStartString'},
            {title:'审批日期',data:'applyEndString'},
            {title:'操作日期',data:'operateString'},
            {title:'授权失效日期',data:'outTimeDateString'},
            {title:'地址',data:'address',width:'0%'},
            {title:'授权书',data:'authorizationPaperUrl',
                render: function (data, type, row, meta) {
                    var url = row.authorizationPaperUrl;
                    if(url == null || url === undefined){
                        return '<div class="ui-grid-cell-contents"><p ">无</p></div>';
                    }
                    return '<div class="ui-grid-cell-contents"><span class="glyphicon glyphicon-eye-open"></span><a style="color: blue;margin-left: 5px" href="' + url + '"target="_blank">查看</a></div>';
                }
            }
            //操作按钮
            // defaultHandle('searchLeaseUserr','saveLeaseUser','deleteLeaseyUser',$compile,$scope)
        ],
        //列是单选还是多选 CheckBox 多选 Radio 单选
        dataTableSelectType: Radio
    }

    //请求的参数
    function dataTableParams($scope){
        params = {};
        params.authorizationStatus = $scope.authorizationStatus;
        params.applicantName = $scope.applicantName;
        params.vehiclePlate = $scope.vehiclePlate;
        params.vehicleIdentifyNum = $scope.vehicleIdentifyNum;
        params.userId = $scope.userId;
        params.applicantPhone = $scope.applicantPhone;
        return params;
    }

    //创建dataTable 封装了datatable
    $scope.dataTable = createTable($scope.dataTableProperties,dataTableParams,$scope,function (value, $scope) {
        if(value != null && value != ''){
            $scope.rowEntity = $scope.dataTable.getRow(value);
            if ($scope.rowEntity.authorizationStatus == "申请中") {
                $scope.class = $scope.classEnabale;
                $scope.isDisable = false;
            } else {
                $scope.class = $scope.classDisabale;
                $scope.isDisable = true;
            }
            if ($scope.rowEntity.authorizationStatus == "已授权") {
                $scope.classDelay = $scope.classEnabale;
                $scope.isDisableDelay = false;
            } else {
                $scope.classDelay = $scope.classDisabale;
                $scope.isDisableDelay = true;
            }
        }
        $scope.$apply();
    });

    /******************请求列表初始化结束*******************/


    /******************请求列表查询开始*******************/
    $scope.searchAuthorization = function(){
        $scope.dataTable.fnDraw(true);//框架内部方法
        //初始化按钮属性
        $scope.class = $scope.classDisabale;
        $scope.isDisable = true;
        $scope.classDelay = $scope.classDisabale;
        $scope.isDisableDelay = true;
    }
    /******************请求列表查询结束*******************/

    // 授权
    $scope.onClickAuthorization = function(){
        $scope.rowEntity = $scope.dataTable.getRow($scope.dataTable.getRowId());
        if ($scope.rowEntity == null) {
            alert("请选择要授权的任务！");
        } else if ($scope.rowEntity.authorizationStatus == "申请中") {
            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/authorization/authorization_operate.html?datetime='+getTimestamp(),
                controller: 'authorization_operate_controller',
                resolve:{
                    rowEntity : function (){ return $scope.rowEntity; }
                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    $scope.dataTable.fnDraw(true);
                    //初始化按钮属性
                    $scope.class = $scope.classDisabale;
                    $scope.isDisable = true;
                    $scope.classDelay = $scope.classDisabale;
                    $scope.isDisableDelay = true;
                }
            },function(){
            });
        } else {
            toaster_error('该条任务不能进行授权处理！',toaster);
        }
    };

    // 拒绝
    $scope.onClickRefuse = function(){
        $scope.rowEntity = $scope.dataTable.getRow($scope.dataTable.getRowId());
        if ($scope.rowEntity == null) {
            alert("请选择要拒绝的任务！");
        } else if ($scope.rowEntity.authorizationStatus == "申请中") {
            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/authorization/authorization_refuse.html?datetime='+getTimestamp(),
                controller: 'authorization_refuse_controller',
                resolve:{
                    rowEntity : function (){ return $scope.rowEntity; }

                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    toaster_success('拒绝成功',toaster);
                    $scope.dataTable.fnDraw(true);
                    //初始化按钮属性
                    $scope.class = $scope.classDisabale;
                    $scope.isDisable = true;
                    $scope.classDelay = $scope.classDisabale;
                    $scope.isDisableDelay = true;
                }
            },function(){
            });
        } else {
            toaster_error('该条任务不能进行拒绝处理！',toaster);
        }
    };

    // 授权延期
    $scope.onClickDelay = function(){
        $scope.rowEntity = $scope.dataTable.getRow($scope.dataTable.getRowId());
        if ($scope.rowEntity == null) {
            alert("请选择要延期的任务！");
        } else if ($scope.rowEntity.authorizationStatus == "已授权") {
            var rtn = $modal.open({
                backdrop : 'static',
                size:'lg',
                templateUrl: 'tpl/system/authorization/authorization_delay.html?datetime='+getTimestamp(),
                controller: 'authorization_delay_controller',
                resolve:{
                    rowEntity : function (){ return $scope.rowEntity; }

                }
            });
            rtn.result.then(function (status) {
                if(status == Response.successMark) {
                    toaster_success('授权延期成功',toaster);
                    $scope.dataTable.fnDraw(true);
                    //初始化按钮属性
                    $scope.class = $scope.classDisabale;
                    $scope.isDisable = true;
                    $scope.classDelay = $scope.classDisabale;
                    $scope.isDisableDelay = true;
                }
            },function(){
            });
        } else {
            toaster_error('该条任务不能进行拒绝处理！',toaster);
        }
    };


    // $scope.selectChange = function(id){
    //     alert(id);
    // }


    // $scope.onClickRefuse = function(id){
    //     if(isNull(id) || isUndefined(id))
    //         id = $scope.dataTable.getRowId();
    //     if(id == null)
    //         alert('请选择要删除的数据');
    //     $http.delete('sys_user/deleteSysUser?id='+id).success(function (data) {
    //         if(data.code == Response.successCode) {
    //             toaster_success('删除用户信息成功', toaster);
    //             $scope.dataTable.fnDraw(true);
    //         }
    //         else
    //             alert(data.message);
    //     })
    //
    // }

}])
;