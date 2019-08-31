//------------- login.js -------------//
$(document).ready(function() {

	//for custom checkboxes
	$('input').not('.noStyle').iCheck({
        checkboxClass: 'icheckbox_flat-green'
    });

	//validate login form 
	$("#login-form").validate({
		ignore: null,
		ignore: 'input[type="hidden"]',
		errorPlacement: function(error, element) {
			wrap = element.parent();
			wrap1 = wrap.parent();
			if (wrap1.hasClass('checkbox')) {
				error.insertAfter(wrap1);
			} else {
				if (element.attr('type')=='file') {
					error.insertAfter(element.next());
				} else {
					error.insertAfter(element);
				}
			}
		}, 
		errorClass: 'help-block',
		rules: {
			email: {
				required: true,
				email: true
			},
			password: {
				required: true,
				minlength: 6,
				maxlength:13
			}
		},
		messages: {
			password: {
				required: "密码不能为空",
				minlength: "密码长度为6到13位"
			},
			email: "请填写正确的手机号码",
		},
		highlight: function(element) {
			if ($(element).offsetParent().parent().hasClass('form-group')) {
				$(element).offsetParent().parent().removeClass('has-success').addClass('has-error');
			} else {
				if ($(element).attr('type')=='file') {
					$(element).parent().parent().removeClass('has-success').addClass('has-error');
				}
				$(element).offsetParent().parent().parent().parent().removeClass('has-success').addClass('has-error');
				
			}
	    },
	    unhighlight: function(element,errorClass) {
	    	if ($(element).offsetParent().parent().hasClass('form-group')) {
	    		$(element).offsetParent().parent().removeClass('has-error').addClass('has-success');
		    	$(element.form).find("label[for=" + element.id + "]").removeClass(errorClass);
	    	} else if ($(element).offsetParent().parent().hasClass('checkbox')) {
	    		$(element).offsetParent().parent().parent().parent().removeClass('has-error').addClass('has-success');
	    		$(element.form).find("label[for=" + element.id + "]").removeClass(errorClass);
	    	} else if ($(element).next().hasClass('bootstrap-filestyle')) {
	    		$(element).parent().parent().removeClass('has-error').addClass('has-success');
	    	}
	    	else {
	    		$(element).offsetParent().parent().parent().removeClass('has-error').addClass('has-success');
	    	}
		}
	});

});

var content = new Vue({
	el:"#login",
	data:{
		tel:"",
		pwd:"",
	},
	methods:{
		//登录
		login(){
				var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;//手机号码
				if (!myreg.test(this.tel)){		
					return false
				}
				else if(this.pwd.length < 6){
					return false
				}
				else{
					this.$http.post(requestIP+'/admin/loginin',
					{
						aAccount:this.tel,
						aPwd:this.pwd
					},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							window.location.href = 'index.html';
							// 用户管理、店铺管理、消息管理、系统管理(超级管理员)1,2,3,4（四选一）
						}
						else if(result.body.resultCode == 10014){
							layer.msg('账户或密码错误',{time: 1500})
						}
						else{
							layer.msg('登录失败',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
				}				
		},
		//忘记密码
		forgetpwd(){
			layer.msg('请联系超级管理员',{time: 1500})
		}

	}
})