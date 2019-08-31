$(document).ready(function() {    
    function test1(){
        if(sign1){
              //------------- Datatables -------------//
                    $('#datatable1').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                           "oLanguage": {
							"sProcessing": "处理中...",
							"sLengthMenu": "显示 _MENU_ 项结果",
							"sZeroRecords": "没有匹配结果",
							"sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
							"sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
							"sInfoFiltered": "(由 _MAX_ 项结果过滤)",
							"sInfoPostFix": "",
							"sSearch": "搜索：",
							"sUrl": "",
							"sEmptyTable": "表中数据为空",
							"sLoadingRecords": "载入中...",
							"sInfoThousands": ",",
							"oPaginate": {
								"sFirst": "首页",
								"sPrevious": "上页",
								"sNext": "下页",
								"sLast": "末页"
							},
						},
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar1)
        }
    }
	function test2(){
        if(sign2){
              //------------- Datatables -------------//
                    $('#datatable2').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                          "oLanguage": {
							"sProcessing": "处理中...",
							"sLengthMenu": "显示 _MENU_ 项结果",
							"sZeroRecords": "没有匹配结果",
							"sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
							"sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
							"sInfoFiltered": "(由 _MAX_ 项结果过滤)",
							"sInfoPostFix": "",
							"sSearch": "搜索：",
							"sUrl": "",
							"sEmptyTable": "表中数据为空",
							"sLoadingRecords": "载入中...",
							"sInfoThousands": ",",
							"oPaginate": {
								"sFirst": "首页",
								"sPrevious": "上页",
								"sNext": "下页",
								"sLast": "末页"
							},
						},
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar2)
        }
    }
	function test3(){
		if(sign3){
              //------------- Datatables -------------//
                    $('#datatable3').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                         "oLanguage": {
							"sProcessing": "处理中...",
							"sLengthMenu": "显示 _MENU_ 项结果",
							"sZeroRecords": "没有匹配结果",
							"sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
							"sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
							"sInfoFiltered": "(由 _MAX_ 项结果过滤)",
							"sInfoPostFix": "",
							"sSearch": "搜索：",
							"sUrl": "",
							"sEmptyTable": "表中数据为空",
							"sLoadingRecords": "载入中...",
							"sInfoThousands": ",",
							"oPaginate": {
								"sFirst": "首页",
								"sPrevious": "上页",
								"sNext": "下页",
								"sLast": "末页"
							},
						},
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar3)
        }
    }
	function test4(){
        if(sign4){
              //------------- Datatables -------------//
                    $('#datatable4').dataTable({
                        "sPaginationType": "bs_full", //"bs_normal", "bs_two_button", "bs_four_button", "bs_full"
                        "fnPreDrawCallback": function( oSettings ) {
                            $('.dataTables_filter input').addClass('form-control input-large').attr('placeholder', 'Search');
                            $('.dataTables_length select').addClass('form-control input-small');
                        },
                          "oLanguage": {
							"sProcessing": "处理中...",
							"sLengthMenu": "显示 _MENU_ 项结果",
							"sZeroRecords": "没有匹配结果",
							"sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
							"sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
							"sInfoFiltered": "(由 _MAX_ 项结果过滤)",
							"sInfoPostFix": "",
							"sSearch": "搜索：",
							"sUrl": "",
							"sEmptyTable": "表中数据为空",
							"sLoadingRecords": "载入中...",
							"sInfoThousands": ",",
							"oPaginate": {
								"sFirst": "首页",
								"sPrevious": "上页",
								"sNext": "下页",
								"sLast": "末页"
							},
						},
                        "bJQueryUI": false,
                        "bAutoWidth": false,
                        "bLengthChange":false,
                        "sDom": "<'row'<'col-lg-6 col-md-6 col-sm-12 text-center'l><'col-lg-6 col-md-6 col-sm-12 text-center'f>r>t<'row-'<'col-lg-6 col-md-6 col-sm-12'i><'col-lg-6 col-md-6 col-sm-12'p>>",
                    });
              clearInterval(myvar4)
        }
    }
    var myvar1 = setInterval(function(){ test1() },1000)
	var myvar2 = setInterval(function(){ test2() },1000)
	var myvar3 = setInterval(function(){ test3() },1000)
	var myvar4 = setInterval(function(){ test4() },1000)
 });
 var sign1 = true
 var sign2 = true
 var sign3 = true
 var sign4 = true
 var returnList=[]
var content = new Vue({
    el:"#content",
    data:{ 
        //交易完成审核返款
        ReturnApply:[
		/*
            {
			    ssId:'',//用户签约店铺ID
				uWeiXinName:'',//用户微信名
				sName:''//对方店铺名称
            },
			*/
        ],
		//交易终止审核退款
        TuiApply:[
		/*
            {
				ssId:'',//用户签约店铺ID
				sName:'',//对应店铺名称
				sdProblem:'',//问题描述
				sdPhoto:'',//图片证据上传
				sdApplyName:'',//申请人姓名
				sdApplyNum:'',//身份证号
				sdApplyPhone:''//联系电话	
            },*/
        ],
		ReturnDeposit:[//系统返款信息
		/*{	
			tmTo:'',//退款给的用户微信名称
			tmStore:'',//退款给的店铺名称
			tmModifyTime:''//退款同意的时间
		},*/
		],
		HandinDeposit:[//查看用户上交押金信息
		/*{
			tmFrom:'',//用户微信名称
			tmStore:'',//店铺名称
			tmMoney:'',//交易金钱数（单位:元）
			tmCreateTime:''//上交押金的时间
		},*/
		]
    },
    created(){
		this.showAllList()  
    },
    methods:{
		showAllList()
		{
			//交易完成返款申请
			this.$http.post(requestIP+'/admin/showReturnApply',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.ReturnApply = result.body.data
						returnList=result.body.data//维护一个数组
					}
					else if(result.body.resultCode==10007){
						layer.msg('暂无数据',{time: 1500})
					}
					else{
						layer.msg('请求失败',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 });
		},
			//切换选项卡
        changetab(type){
            //发布不同请求
			//交易完成审核返款
			if(type==1){
				this.$http.post(requestIP+'/admin/showReturnApply',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.ReturnApply = result.body.data
						returnList=result.body.data//维护一个数组
						console.log(result)
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
				
			}
			//交易终止审核退款
			else if(type==2){
				this.$http.post(requestIP+'/admin/showTuiApplyList',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.TuiApply = result.body.data
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
			}
			//查看系统返款信息
			else if(type==3){
				this.$http.post(requestIP+'/admin/showReturnDeposit',{},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							this.ReturnDeposit = result.body.data
							console.log(result)
						}
						else{
							this.ReturnDeposit =""
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
			}
			//查看用户上交押金信息
			else{
				this.$http.post(requestIP+'/admin/showHandInDeposit',{},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							this.HandinDeposit = result.body.data
							console.log(result)
						}else{
							this.HandinDeposit=""
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
			}
        },
		//交易完成审核返款审核通过M
		agreemReturn(ssId){
			this.$http.post(requestIP+'/admin/agreeReturnApply',
			{
				ssId:ssId
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('通过成功',{time: 1500})
					for(var i=0;i<returnList.length;i++){
						if(returnList[i].ssId==ssId){
							returnList.splice(i,1);
							break;
						}
					}
					this.ReturnApply=returnList;
				}
				else{
					layer.msg('拒绝失败',{time: 1500})
				}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});    
		},
		//交易完成审核返款审核拒绝
		reducemReturn(ssId){
			this.$http.post(requestIP+'/admin/refuseReturnApply',
			{
				ssId:ssId
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('拒绝成功',{time: 1500})
					for(var i=0;i<returnList.length;i++){
						if(returnList[i].ssId==ssId){
							returnList.splice(i,1);
							break;
						}
					}
					this.ReturnApply=returnList;						
				}
				else{
					layer.msg('拒绝失败',{time: 1500})
				}
			}).catch(function(){
					layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			});								
		},
		  ////跳转到退款详情页
        hreftodetails(sdId,sName,ssid){            
            window.location.href = "sdetailsTuiApply.html?sdId="+sdId+"&sName="+sName+"&ssid="+ssid;
        }
       
    }
})
