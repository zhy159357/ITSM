(function () {
    'use strict';

    window.globalUiKit = window.globalUiKit || {};

    // 全局vm对象
    window.globalUiKit.vm = new Vue();

    /**
     * 提示类
     *
     * @param title 提示标题
     * @param message 提示内容
     * @param type 提示类型
     * @param location 默认bottom-right
     */
    window.globalUiKit.notify = function (title, message, type, location) {
        window.globalUiKit.vm.$notify({
            title: title || '',
            message: message || '',
            type: type || 'info',
            position: location || 'bottom-right',
            dangerouslyUseHTMLString: true
        });
    };

    /**
     *
     * @param title
     * @param message
     * @param submitHandler
     * @param cancelHandler
     * @param options
     */
    window.globalUiKit.confirm = function (title, message, submitHandler, cancelHandler, options) {
        if(!options) {
            options = {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'info',
                dangerouslyUseHTMLString: true
            }
        }
        window.globalUiKit.vm.$confirm(
            message, title || '提示', options).then(() => {
            if(submitHandler) {
                submitHandler()
            }
        }).catch(() => {
            if(cancelHandler) {
                cancelHandler()
            }
        });
    },

    /**
     *
     * @param title
     * @param message
     * @param submitHandler
     * @param cancelHandler
     * @param options
     */
    window.globalUiKit.alert = function (title, message, submitHandler, confirmButtonText) {
        window.globalUiKit.vm.$alert(message, title || '提示', {
            confirmButtonText: confirmButtonText || '确定',
            callback: action => {
                if(submitHandler) {
                    submitHandler();
                }
            }
        });
    }

})();
