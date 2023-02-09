var KisBpmFormSelectPropertiesCtrl = [ '$scope', '$http', function($scope, $http) {

    /*$scope.valueFlushed = false;*/
    $scope.formData=[
        "Writing code",
        "Testing code",
        "Fixing bugs",
        "Dancing"
    ];
    /**请求后台查询获取表单编号列表*/
    $http({
        method: 'POST',
        url: ctt + 'bmp/config/selectFromKeyList',
        params: {

        },
    }).then(function successCallback(response) {
        $scope.formData = response.data.rows;
    }, function errorCallback(response) {
        // 请求失败执行代码
        $scope.formData = [];
    });
    $scope.selectChanged = function() {
        $scope.updatePropertyInModel($scope.property);
    };
}];