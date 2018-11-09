/**
 * Created by yuanzhenxia on 2018/1/10.
 */
app.controller('authorization_refuse_controller', ['$scope', '$http','$modal', '$modalInstance','rowEntity', '$sce',function ($scope, $http,$modal, $modalInstance,rowEntity,$sce) {

    $scope.vo={};
    $scope.rowEntity=rowEntity;
    $scope.vo.authorizationId = $scope.rowEntity.id;
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
     * 拒绝操作
     */
    $scope.refuse = function () {
        if(!$scope.form.$invalid) {
            $http.post('authorization/refuse',$scope.vo).success(function (data) {
                if (data.code == Response.successCode){
                    // alert(data.message);
                    $scope.close(Response.successMark);
                }else{
                    alert(data.message);
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


