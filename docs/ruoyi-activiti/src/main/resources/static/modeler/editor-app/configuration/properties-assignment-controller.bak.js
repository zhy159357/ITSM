/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Assignment
 */
var KisBpmAssignmentCtrl = [ '$scope', '$modal', function($scope, $modal) {

	// Config for the modal window
	var opts = {
		template:  'editor-app/configuration/properties/assignment-popup.bak.html?version=' + Date.now(),
		scope: $scope
	};

	// Open the dialog
	$modal(opts);
}];

var KisBpmAssignmentPopupCtrl = [ '$scope', '$http', function($scope, $http) {

	// Put json representing assignment on scope
	if ($scope.property.value !== undefined && $scope.property.value !== null
		&& $scope.property.value.assignment !== undefined
		&& $scope.property.value.assignment !== null)
	{
		$scope.assignment = $scope.property.value.assignment;
	} else {
		$scope.assignment = {};
	}

	$scope.popup = {
		assignment: {
			assignee: undefined,
            assigneeName: '',
			candidateUsers: [],
			candidateUserField: '',
			candidateUserName: '',
			candidateGroups: [],
			candidateGroupField: '',
			candidateGroupName: ''
		}
	};

	if ($scope.assignment.assignee) {
		$scope.popup.assignment.assignee = $scope.assignment.assignee;
        //$scope.popup.assignment.assigneeName = $scope.selectUserGroupByIds($scope.popup.assignment.assignee, 1);
	}

	if ($scope.assignment.candidateUsers && $scope.assignment.candidateUsers.length > 0) {
		for (var i = 0; i < $scope.assignment.candidateUsers.length; i++) {
			$scope.popup.assignment.candidateUsers.push($scope.assignment.candidateUsers[i]);
			$scope.popup.assignment.candidateUserField += (i == 0 ? '' : ',') + $scope.assignment.candidateUsers[i].value;
		}
        //$scope.popup.assignment.candidateUserName = $scope.selectUserGroupByIds($scope.popup.assignment.candidateUserField, 2);
	}

	if ($scope.assignment.candidateGroups && $scope.assignment.candidateGroups.length > 0) {
		for (var i = 0; i < $scope.assignment.candidateGroups.length; i++) {
			$scope.popup.assignment.candidateGroups.push($scope.assignment.candidateGroups[i]);
			$scope.popup.assignment.candidateGroupField += (i == 0 ? '' : ',') + $scope.assignment.candidateGroups[i].value;
		}
        //$scope.popup.assignment.candidateGroupName = $scope.selectUserGroupByIds($scope.popup.assignment.candidateGroupField, 2);
	}

	$scope.save = function() {
		$scope.assignment.assignee = $scope.popup.assignment.assignee;
		$scope.assignment.candidateUsers = $scope.popup.assignment.candidateUsers;
		if($scope.popup.assignment.candidateUserField == '') {
            $scope.assignment.candidateUsers = [];
		} else if($scope.popup.assignment.candidateUserField.indexOf("$") != -1) {
			var arr = [{value : $scope.popup.assignment.candidateUserField}];
            $scope.assignment.candidateUsers = arr;
		}
		$scope.assignment.candidateGroups = $scope.popup.assignment.candidateGroups;
        if($scope.popup.assignment.candidateGroupField == '') {
            $scope.assignment.candidateGroups = [];
        } else if($scope.popup.assignment.candidateGroupField.indexOf("$") != -1) {
            var arr = [{value : $scope.popup.assignment.candidateGroupField}];
            $scope.assignment.candidateGroups = arr;
        }
        $scope.popup.assignment.candidateUserField = '';
        $scope.popup.assignment.candidateGroupField = '';
		$scope.property.value = {};
		$scope.property.value.assignment = $scope.assignment;

		$scope.updatePropertyInModel($scope.property);
		$scope.close();
	};

	// Close button handler
	$scope.close = function() {
		$scope.property.mode = 'read';
		$scope.$hide();
	};

	//******************************自定义选择代理人，候选人，候选组******************************
	//参数初始化
	$scope.gridData = [];//表格数据
	$scope.gridDataName = 'gridData';
	$scope.selectTitle = '选择代理人';
	$scope.columnData = [];//表格列数据
	$scope.columnDataName = 'columnData';
	$scope.selectType = 0;//0-代理人，1-候选人，2-候选组
	$scope.totalServerItems = 0;//表格总条数
	//分页初始化
	$scope.pagingOptions = {
		pageSizes: [10, 20, 30],//page 行数可选值
		pageSize: 10, //每页行数
		currentPage: 1, //当前显示页数
	};
	//Grid 筛选
	$scope.projects = [];
	$scope.selectedProject = -1;

	//数据监视
	$scope.dataWatch = function () {
		//分页数据监视
		$scope.$watch('pagingOptions', function (newValue, oldValue) {
			$scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
		},true);

		//当切换类型时，初始化当前页
		$scope.$watch('selectType', function (newValue, oldValue) {
			if (newValue != oldValue) {
				$scope.pagingOptions.currentPage = 1;
				$scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
			}
		},true);

		//切换平台
		$scope.change = function (x) {
			$scope.selectedProject = x;
			$scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
		};
	};

	$scope.dataWatch();

	//异步请求表格数据
	$scope.getPagedDataAsync = function (pageNum, pageSize, projectId) {
		//var url = ctt + 'system/ogperson/list'; // 获取用户数据接口
		var url = ctt + 'bmp/config/selectActivityUsers'; // 获取用户数据接口
		$scope.columnData = $scope.userColumns;
		if ($scope.selectType == 2) {
			//url = ctt + 'system/work/list'; // 获取组数据接口
			url = ctt + 'bmp/config/selectActivityGroups'; // 获取组数据接口
			$scope.columnData = $scope.groupColumns;
		}
		$http({
			method: 'POST',
			url: url,//ACTIVITI.CONFIG.contextRoot +
			params: {
				'pageNum': pageNum,
				'pageSize': pageSize,
				'projectId': projectId
			},
		}).then(function successCallback(response) {
			$scope.gridData = response.data.rows;
			$scope.totalServerItems = response.data.total;
		}, function errorCallback(response) {
			// 请求失败执行代码
			$scope.gridData = [];
			$scope.totalServerItems = 0;
		});
	};

    // 反显代理人、候选人、候选组的id对应的名称
    $scope.showUserGroupName = function () {
        $http({
            method: 'POST',
            url: ctt + 'bmp/config/showUserGroupName',
            params: {
                'assignee': $scope.popup.assignment.assignee,
                'candidateUser': $scope.popup.assignment.candidateUserField,
                'candidateGroup': $scope.popup.assignment.candidateGroupField
            }
        }).then(function successCallback(response) {
            $scope.popup.assignment.assigneeName = response.data.data.assigneeName;
            $scope.popup.assignment.candidateUserName = response.data.data.candidateUserName;
            $scope.popup.assignment.candidateGroupName = response.data.data.candidateGroupName;
        }, function errorCallback(response) {
            // 请求失败执行代码
            $scope.popup.assignment.assigneeName = "";
            $scope.popup.assignment.candidateUserName = "";
            $scope.popup.assignment.candidateGroupName = "";
        });
    }

    // 都不包含$才去获取名称
    //if($scope.popup.assignment.candidateUserField.indexOf("$") == -1 && $scope.popup.assignment.candidateGroupField.indexOf("$") == -1) {
        $scope.showUserGroupName();
    //}

	// 处理候选人数据
	$scope.handleUsers = function (data) {
		var notExist = true;
		for (var i = 0; i < $scope.popup.assignment.candidateUsers.length; i++) {
			if ($scope.popup.assignment.candidateUsers[i].value == data) {
				$scope.popup.assignment.candidateUsers.splice(i, 1);
				notExist = false;
				break;
			}
		}
		if (notExist) {
			$scope.popup.assignment.candidateUsers.push({value: data});
		}
		$scope.popup.assignment.candidateUserField = '';
		for (var i = 0; i < $scope.popup.assignment.candidateUsers.length; i++) {
			$scope.popup.assignment.candidateUserField += (i == 0 ? '' : ',') + $scope.popup.assignment.candidateUsers[i].value;
		}
		$scope.selectUserGroupByIds($scope.popup.assignment.candidateUserField, 1);
	};

	// 处理候选组数据
	$scope.handleGroups = function (data) {
		var notExist = true;
		for (var i = 0; i < $scope.popup.assignment.candidateGroups.length; i++) {
			if ($scope.popup.assignment.candidateGroups[i].value == data) {
				$scope.popup.assignment.candidateGroups.splice(i, 1);
				notExist = false;
				break;
			}
		}
		if (notExist) {
			$scope.popup.assignment.candidateGroups.push({value: data});
		}
		$scope.popup.assignment.candidateGroupField = '';
		for (var i = 0; i < $scope.popup.assignment.candidateGroups.length; i++) {
			$scope.popup.assignment.candidateGroupField += (i == 0 ? '' : ',') + $scope.popup.assignment.candidateGroups[i].value;
		}
        $scope.selectUserGroupByIds($scope.popup.assignment.candidateGroupField, 2);
	};

	//表格属性配置
	$scope.gridOptions = {
		data: $scope.gridDataName,
		multiSelect: false,//不可多选
		enablePaging: true,
		pagingOptions: $scope.pagingOptions,
		totalServerItems: 'totalServerItems',
		i18n:'zh-cn',
		showFooter: true,
		showSelectionCheckbox: false,
		columnDefs : $scope.columnDataName,
		beforeSelectionChange: function (event) {
			var data;
			var name;
			if ($scope.selectType == 0) { // 选代理人
				data = event.entity.pId;
                name = event.entity.username;
				if ($scope.popup.assignment.assignee == data) {
					$scope.popup.assignment.assignee = '';
					$scope.popup.assignment.assigneeName = '';
				} else {
					$scope.popup.assignment.assignee = data;
                    $scope.popup.assignment.assigneeName = name;
				}
			} else if ($scope.selectType == 1) { // 候选人
				data = event.entity.pId;
				if(data!=null) {
					$scope.handleUsers(data);
				}
			} else if ($scope.selectType == 2) { // 候选组
				data = event.entity.groupId;
				if(data!=null){
					$scope.handleGroups(data);
				}
			}
			return true;
		}
	};

	//选择用户时表头
	$scope.userColumns = [
		{
			field : 'pId',
			type:'string',
			displayName : '用户Id',
			minWidth: 100,
			width : '25%'
		},
        {
            field : 'username',
            displayName : '用户名',
            minWidth: 100,
            width : '25%'
        },
		{
			field : 'pName',
			displayName : '姓名',
			minWidth: 100,
			width : '25%'
		},
		/*{
			field : 'orgname',
			displayName : '所属机构',
			minWidth: 100,
			width : '25%'
		},*/
		{
			field : 'email',
			displayName : '邮箱',
			minWidth: 100,
			width : '25%'
		}
	];

	//选择用户组时表头
	$scope.groupColumns = [
		{
			field : 'groupId',
			type:'string',
			displayName : '工作组Id',
			minWidth: 100,
			width : '30%'
		},
		{
			field : 'grpName',
			displayName : '工作组名称',
			minWidth: 100,
			width : '70%'
		}
	];

	// 代理人(审批人)
	$scope.selectAssignee = function () {
		$scope.selectType = 0;
		$scope.selectTitle = '选择代理人';
	};

	// 候选人
	$scope.selectCandidate = function () {
		$scope.selectType = 1;
		$scope.selectTitle = '选择候选人';
	};

	// 候选组
	$scope.selectGroup = function () {
		$scope.selectType = 2;
		$scope.selectTitle = '选择候选组';
	};
	
	$scope.selectUserGroupByIds = function (ids, type) {
		if(!ids) {
            // 代理人名称反显
            if(type == 0) {
                $scope.popup.assignment.assigneeName = "";
            }
            // 候选人名称反显
            if(type == 1) {
                $scope.popup.assignment.candidateUserName = "";
            }
            // 候选组名称反显
            if(type == 2) {
                $scope.popup.assignment.candidateGroupName = "";
            }
			return;
		}
        $http({
            method: 'POST',
            url: ctt + 'bmp/config/selectUserGroupByIds',
            params: {
                'ids': ids,
				'type': type
            }
        }).then(function successCallback(response) {
        	// 代理人名称反显
        	if(type == 0) {
                $scope.popup.assignment.assigneeName = response.data.data;
            }
            // 候选人名称反显
            if(type == 1) {
                $scope.popup.assignment.candidateUserName = response.data.data;
            }
            // 候选组名称反显
            if(type == 2) {
                $scope.popup.assignment.candidateGroupName = response.data.data;
            }
        }, function errorCallback(response) {
            // 请求失败执行代码
            $scope.popup.assignment.assigneeName = "";
            $scope.popup.assignment.candidateUserName = "";
            $scope.popup.assignment.candidateGroupName = "";
        });
    }

}];