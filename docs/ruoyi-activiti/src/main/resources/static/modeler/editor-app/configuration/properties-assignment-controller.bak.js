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

	//******************************????????????????????????????????????????????????******************************
	//???????????????
	$scope.gridData = [];//????????????
	$scope.gridDataName = 'gridData';
	$scope.selectTitle = '???????????????';
	$scope.columnData = [];//???????????????
	$scope.columnDataName = 'columnData';
	$scope.selectType = 0;//0-????????????1-????????????2-?????????
	$scope.totalServerItems = 0;//???????????????
	//???????????????
	$scope.pagingOptions = {
		pageSizes: [10, 20, 30],//page ???????????????
		pageSize: 10, //????????????
		currentPage: 1, //??????????????????
	};
	//Grid ??????
	$scope.projects = [];
	$scope.selectedProject = -1;

	//????????????
	$scope.dataWatch = function () {
		//??????????????????
		$scope.$watch('pagingOptions', function (newValue, oldValue) {
			$scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
		},true);

		//???????????????????????????????????????
		$scope.$watch('selectType', function (newValue, oldValue) {
			if (newValue != oldValue) {
				$scope.pagingOptions.currentPage = 1;
				$scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
			}
		},true);

		//????????????
		$scope.change = function (x) {
			$scope.selectedProject = x;
			$scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
		};
	};

	$scope.dataWatch();

	//????????????????????????
	$scope.getPagedDataAsync = function (pageNum, pageSize, projectId) {
		//var url = ctt + 'system/ogperson/list'; // ????????????????????????
		var url = ctt + 'bmp/config/selectActivityUsers'; // ????????????????????????
		$scope.columnData = $scope.userColumns;
		if ($scope.selectType == 2) {
			//url = ctt + 'system/work/list'; // ?????????????????????
			url = ctt + 'bmp/config/selectActivityGroups'; // ?????????????????????
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
			// ????????????????????????
			$scope.gridData = [];
			$scope.totalServerItems = 0;
		});
	};

    // ??????????????????????????????????????????id???????????????
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
            // ????????????????????????
            $scope.popup.assignment.assigneeName = "";
            $scope.popup.assignment.candidateUserName = "";
            $scope.popup.assignment.candidateGroupName = "";
        });
    }

    // ????????????$??????????????????
    //if($scope.popup.assignment.candidateUserField.indexOf("$") == -1 && $scope.popup.assignment.candidateGroupField.indexOf("$") == -1) {
        $scope.showUserGroupName();
    //}

	// ?????????????????????
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

	// ?????????????????????
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

	//??????????????????
	$scope.gridOptions = {
		data: $scope.gridDataName,
		multiSelect: false,//????????????
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
			if ($scope.selectType == 0) { // ????????????
				data = event.entity.pId;
                name = event.entity.username;
				if ($scope.popup.assignment.assignee == data) {
					$scope.popup.assignment.assignee = '';
					$scope.popup.assignment.assigneeName = '';
				} else {
					$scope.popup.assignment.assignee = data;
                    $scope.popup.assignment.assigneeName = name;
				}
			} else if ($scope.selectType == 1) { // ?????????
				data = event.entity.pId;
				if(data!=null) {
					$scope.handleUsers(data);
				}
			} else if ($scope.selectType == 2) { // ?????????
				data = event.entity.groupId;
				if(data!=null){
					$scope.handleGroups(data);
				}
			}
			return true;
		}
	};

	//?????????????????????
	$scope.userColumns = [
		{
			field : 'pId',
			type:'string',
			displayName : '??????Id',
			minWidth: 100,
			width : '25%'
		},
        {
            field : 'username',
            displayName : '?????????',
            minWidth: 100,
            width : '25%'
        },
		{
			field : 'pName',
			displayName : '??????',
			minWidth: 100,
			width : '25%'
		},
		/*{
			field : 'orgname',
			displayName : '????????????',
			minWidth: 100,
			width : '25%'
		},*/
		{
			field : 'email',
			displayName : '??????',
			minWidth: 100,
			width : '25%'
		}
	];

	//????????????????????????
	$scope.groupColumns = [
		{
			field : 'groupId',
			type:'string',
			displayName : '?????????Id',
			minWidth: 100,
			width : '30%'
		},
		{
			field : 'grpName',
			displayName : '???????????????',
			minWidth: 100,
			width : '70%'
		}
	];

	// ?????????(?????????)
	$scope.selectAssignee = function () {
		$scope.selectType = 0;
		$scope.selectTitle = '???????????????';
	};

	// ?????????
	$scope.selectCandidate = function () {
		$scope.selectType = 1;
		$scope.selectTitle = '???????????????';
	};

	// ?????????
	$scope.selectGroup = function () {
		$scope.selectType = 2;
		$scope.selectTitle = '???????????????';
	};
	
	$scope.selectUserGroupByIds = function (ids, type) {
		if(!ids) {
            // ?????????????????????
            if(type == 0) {
                $scope.popup.assignment.assigneeName = "";
            }
            // ?????????????????????
            if(type == 1) {
                $scope.popup.assignment.candidateUserName = "";
            }
            // ?????????????????????
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
        	// ?????????????????????
        	if(type == 0) {
                $scope.popup.assignment.assigneeName = response.data.data;
            }
            // ?????????????????????
            if(type == 1) {
                $scope.popup.assignment.candidateUserName = response.data.data;
            }
            // ?????????????????????
            if(type == 2) {
                $scope.popup.assignment.candidateGroupName = response.data.data;
            }
        }, function errorCallback(response) {
            // ????????????????????????
            $scope.popup.assignment.assigneeName = "";
            $scope.popup.assignment.candidateUserName = "";
            $scope.popup.assignment.candidateGroupName = "";
        });
    }

}];