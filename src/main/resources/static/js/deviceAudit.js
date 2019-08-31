$(document).ready(function() {    
    function test1(){
        if(sign1){
              //------------- Datatables -------------//
                    $('#datatable').dataTable({
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
							"sSearch": "",
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
							"sSearch": "",
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
    var myvar1 = setInterval(function(){ test1() },1000)
    var myvar2 = setInterval(function(){ test2() },1000)   
 });
 var sign1 = true
 var sign2 = true
 var needAuditFlag=1;//是否需要审核0-否1-是
var content = new Vue({
    el:"#content",
    data:{ 
        //二手设备
        Businesstransfer:[
			/*
            {
                name:'郑修月',
                type:"火锅设备",
                title:"火锅",
                tel:"15171411165",
				clean:"已清洗",
                fee:"36781元",
            },*/
           
        ],
        //新设备
        Shoprental:[
		/*
            {
                name:'郑修月',
                type:"火锅设备",
                title:"火锅",
                tel:"15171411165",
                fee:"36781元"
            },*/
            
        ]
    },
    created(){
		this.showAllList()
    },
    methods:{
		showAllList(){
			//二手
			var temp=[]
			this.$http.post(requestIP+'/admin/getWaitSecondList',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						for(var i=0;i<result.body.data.length;i++){
							temp.push(result.body.data[i])
							if(result.body.data[i].dClean=='1'){
								console.log(result.body.data[0].dClean)
								temp[i].dClean='是'
							}
							else{
								temp[i].dClean='否'
							}
						}
						this.Businesstransfer = temp
						console.log(result)
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 	});
		},
        //切换选项卡
        changetab(type){
            //发布不同请求
					//二手
					var temp=[]
					if(type==1){
			this.$http.post(requestIP+'/admin/getWaitSecondList',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						console.log(result.body.data[0].dClean)
						for(var i=0;i<result.body.data.length;i++){
							temp.push(result.body.data[i])
							if(result.body.data[i].dClean=='1'){
								console.log(result.body.data[0].dClean)
								console.log(temp[i])
								temp[i].dClean='是'
							}
							else{
								temp[i].dClean='否'
							}
						}
							this.Businesstransfer = temp
						console.log(temp)
					}
					else{
						this.Businesstransfer=""
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 	});
					}
			//新
			else{
			this.$http.post(requestIP+'/admin/getWaitNewList',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						for(var i=0;i<result.body.data.length;i++){
							temp.push(result.body.data[i])
							if(result.body.data[i].dClean=='1'){
								console.log(result.body.data[0].dClean)
								temp[i].dClean='是'
							}
							else{
								temp[i].dClean='否'
							}
						}
						this.Shoprental = temp
					}
					else{
						this.Shoprental =""
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				}); 
			}
        },
        ////跳转到新设备详情页
        hreftodetails1(dId){  
			if(needAuditFlag==1){
				needAuditFlag==0
				window.location.href = "sdetailsDevice.html?clean=no&dId="+dId+"&needAuditFlag=1";
			}else
				window.location.href = "sdetailsDevice.html?clean=no&dId="+dId+"&needAuditFlag=0";
        },
		 ////跳转到二手设备详情页
        hreftodetails0(dId){ 
			if(needAuditFlag==1){
				needAuditFlag==0
				window.location.href = "sdetailsDevice.html?clean=yes&dId="+dId+"&needAuditFlag=1";
			}else
				window.location.href = "sdetailsDevice.html?clean=no&dId="+dId+"&needAuditFlag=0";
        },
		//二手设备待审核和审核通过切换
		check(flag){
			var temp=[]
			if(flag==0){//待审核
				needAuditFlag=1;
				this.$http.post(requestIP+'/admin/getWaitSecondList',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						for(var i=0;i<result.body.data.length;i++){
							temp.push(result.body.data[i])
							if(result.body.data[i].dClean=='1'){
								console.log(result.body.data[0].dClean)
								console.log(temp[i])
								temp[i].dClean='是'
							}
							else{
								temp[i].dClean='否'
							}
						}
						this.Businesstransfer = temp
						console.log(result)
					}
					else{
						this.Businesstransfer = ""
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 	});
			}
			else{
				needAuditFlag=0;
					this.$http.post(requestIP+'/admin/checkedSecondList',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						for(var i=0;i<result.body.data.length;i++){
							temp.push(result.body.data[i])
							if(result.body.data[i].dClean=='1'){
								console.log(result.body.data[0].dClean)
								console.log(temp[i])
								temp[i].dClean='是'
							}
							else{
								temp[i].dClean='否'
							}
						}
						this.Businesstransfer = temp
					}
					else{
						this.Businesstransfer = ""
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 	});
			}
		},
		//新设备待审核和审核通过切换
		checkXin(flag){
			if(flag==0){
				needAuditFlag=1;
				this.$http.post(requestIP+'/admin/getWaitNewList',{},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							this.Shoprental = result.body.data
							console.log(result)
						}
						else{
							this.Shoprental = ""
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					}); 
				
			}
			else{
				needAuditFlag=0;
			this.$http.post(requestIP+'/admin/checkedNewList',{},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.Shoprental = result.body.data
						console.log(result)
					}
					else{
						this.Shoprental = ""
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				}); 
				
			}
		}
    }
})

