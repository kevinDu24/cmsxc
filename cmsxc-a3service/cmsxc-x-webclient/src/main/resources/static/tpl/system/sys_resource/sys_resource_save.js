/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_resource_save_controller', ['$scope', '$http','$modal', '$modalInstance','toaster', function ($scope, $http,$modal, $modalInstance,toaster) {

    $scope.sysRes={};

    $scope.sysRes.type = 0;

    $scope.sysRes.resLevel = 0;

    $scope.sysResParents = [];

    $scope.formValidate = false;

    $scope.submit = false;

    var resLevelDataTmp = [{id:0,description:'一级菜单'},{id:1,description:'二级菜单'},{id:2,description:'三级菜单'}];

    $scope.resLevelDatas = resLevelDataTmp;

    var parentIdInfoTmp = "---请选择父级菜单---";

    $scope.parentIdInfo = parentIdInfoTmp;


    $scope.typeChange = function(){
        if($scope.sysRes.type == 0){
            $scope.resLevelDatas = resLevelDataTmp;
        }else{
            $scope.resLevelDatas = [];
        }
    }

    $scope.resLevelChange = function(){
        if($scope.sysRes.resLevel != 0){
            var resLevel = $scope.sysRes.resLevel - 1;
            $http.get('sys_resource/findSysResourceIsParent?resLevel='+resLevel).success(function(data){
                $scope.sysResParents = data.data;
            });
        }else{
            $scope.sysResParents = [];
        }
    }

    $scope.$watch('sysRes.resLevel',function(){
        if($scope.sysRes.resLevel != 0){
            $scope.parentIdInfo = parentIdInfoTmp;
        }else{
            $scope.parentIdInfo = "";
        }
    });



    /**
     * 保存菜单信息
     */
    $scope.save = function () {

        if(!$scope.form.$invalid) {

            if($scope.sysRes.type == 0 && $scope.sysRes.resLevel != 0 && isNullEmpty($scope.sysRes.parentId)){
                modalAlert($modal,"非一级菜单需要选择父级菜单");
            }else{

                $scope.submit = true;

                $http.post('sys_resource/saveSysResource', $scope.sysRes).success(function (data) {
                    if (data.code == Response.successCode)
                        $scope.close(Response.successMark);
                    else
                        modalAlert($modal,data.message);
                    $scope.submit = false;
                }).error(function(data){
                    modalAlert($modal,data);
                    $scope.submit = false;
                })

            }
        }else{
            $scope.formValidate = true;
            toaster_info(promptInfo.submitWarn,toaster);
        }


    }

    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

}]);


