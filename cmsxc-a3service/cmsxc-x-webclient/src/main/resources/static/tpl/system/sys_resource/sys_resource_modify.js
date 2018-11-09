/**
 * Created by qiaohao on 2018/1/10.
 */
app.controller('sys_resource_modify_controller', ['$scope', '$http','$modal', '$modalInstance','toaster' ,'sysResourceId', function ($scope, $http,$modal, $modalInstance,toaster,sysResourceId) {

    $scope.sysRes = {};


    $http.get('sys_resource/findSysResById?id='+ sysResourceId).success(function(data){
        $scope.sysRes = data.data;


        if($scope.sysRes.resLevel != 0){

            var resLevel = $scope.sysRes.resLevel - 1;
            $http.get('sys_resource/findSysResourceIsParent?resLevel='+resLevel).success(function(data){
                $scope.sysResParents = data.data;
            });

        }

    });


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
     * 保存用户信息
     */
    $scope.modify = function () {
        if(!$scope.form.$invalid) {

            if($scope.sysRes.type == 0 && $scope.sysRes.resLevel != 0 && isNullEmpty($scope.sysRes.parentId)){
                modalAlert($modal,"非一级菜单需要选择父级菜单");
            }else{
                $scope.submit = true;
                $http.put('sys_resource/modifySysResource', $scope.sysRes).success(function (data) {
                    if (data.code == Response.successCode)
                        $scope.close(Response.successMark);
                    else
                        modalAlert($modal,data.message);
                    $scope.submit = true;
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


