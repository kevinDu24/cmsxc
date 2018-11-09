/**
 * Created by yuanzhenxia on 2018/1/10.
 */
app.controller('authorization_operate_controller', ['$scope', '$http','$modal','toaster', '$modalInstance','rowEntity','$sce', function ($scope, $http,$modal, toaster,$modalInstance, rowEntity,$sce) {

    $scope.vo={};
    $scope.rowEntity=rowEntity;
    $scope.vo.authorizationId = $scope.rowEntity.id;
    $scope.vo.userId = $scope.rowEntity.userId;
    $scope.vo.plate = $scope.rowEntity.plate;
    $scope.photoList = [];
    $scope.vedioPath = '';

    function init() {
        // 图片的UUID
        $scope.photoUuid = $scope.rowEntity.photoUuid;
        $http.get('authorization/getPhotoList?photoUuid=' + $scope.photoUuid).success(function (result) {
            if (result.code == Response.successCode) {
                $scope.photoList = result.data.photoPathList;
                $scope.vedioPath = result.data.vedioPath;
            } else {
                $scope.photoList = [];
                $scope.vedioPath = '';
            }
        });
    }
    // 点击图片显示大图
    $scope.changePic = function ($event) {
        var img = $event.srcElement || $event.target;
        $("#bigimage")[0].src=img.src;
        $("#js-imgview")[0].style.display="block";
        $("#js-imgview-mask")[0].style.display="block";
    };

    // 页面初始化
    init();
    /**
     * 授权操作
     */
    $scope.authorization = function () {
        if(!$scope.form.$invalid) {
            $http.post('authorization/authorization', $scope.vo).success(function (data) {
                if (data.code == Response.successCode){
                    toaster_success('授权成功',toaster);
                    $scope.close(Response.successMark);
                }else{
                    toaster_error(data.message,toaster);
                    $scope.close(Response.errorMark);
                }})
        }
    }
    /**
     * 关闭窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };

    $scope.trustSrc = function(url){
        return $sce.trustAsResourceUrl(url);
    }

}]);


