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
 var needCheckFlag=1 //是否待审核 0否1是
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
			//获取待审核二手举报设备
			this.$http.post(requestIP+'/admin/waitCheckDeviceList',{
				dHand:2
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						var temp = result.body.data
						for(var i=0;i<temp.length;i++){
							if(temp[i].rdOption==0){
								temp[i].rdOption="商品严重不符合描述"
							}else if(temp[i].rdOption==1){
								temp[i].rdOption="临时加价"
							}else{
								temp[i].rdOption="保证期内拒绝服务"
							}
						}
						this. Businesstransfer = temp
						console.log(result)
					}
					else{
						layer.msg('暂无数据',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
			 	});
		},
        ////跳转到举报详情页
        hreftodetails(rdId){ 
			console.log(needCheckFlag)
			if(needCheckFlag==1){
				needCheckFlag=0
				window.location.href = "sdetailsDeviceReport.html?rdId="+rdId+"&needCheckFlag=1";
			}else
				window.location.href = "sdetailsDeviceReport.html?rdId="+rdId+"&needCheckFlag=0";
        },
		changetab(type){
            //发布不同请求
			if(type==1){
				//获取待审核二手举报设备
				this.$http.post(requestIP+'/admin/waitCheckDeviceList',{
					dHand:2
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							this. Businesstransfer = result.body.data
							console.log(result)
						}
						else{
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
			}else if(type==2){
				//获取待审核举报新设备
				this.$http.post(requestIP+'/admin/waitCheckDeviceList',{
					dHand:1
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							var temp = result.body.data
							for(var i=0;i<temp.length;i++){
								if(temp[i].rdOption==0){
									temp[i].rdOption="商品严重不符合描述"
								}else if(temp[i].rdOption==1){
									temp[i].rdOption="临时加价"
								}else{
									temp[i].rdOption="保证期内拒绝服务"
								}
							}
							this.Shoprental = temp
							console.log(result)
						}
						else{
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
			}
        },
		check(flag){//待审核二手设备
			if(flag==0){
				needCheckFlag=1;
				this.$http.post(requestIP+'/admin/waitCheckDeviceList',{
					dHand:2
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							var temp = result.body.data
							for(var i=0;i<temp.length;i++){
								if(temp[i].rdOption==0){
									temp[i].rdOption="商品严重不符合描述"
								}else if(temp[i].rdOption==1){
									temp[i].rdOption="临时加价"
								}else{
									temp[i].rdOption="保证期内拒绝服务"
								}
							}
							this. Businesstransfer = temp
						}
						else{
							this. Businesstransfer = ""
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
			}
			else{//已审核二手
				needCheckFlag=0;
					this.$http.post(requestIP+'/admin/checkedReportDeviceList',{
					dHand:2
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							var temp = result.body.data
							for(var i=0;i<temp.length;i++){
								if(temp[i].rdOption==0){
									temp[i].rdOption="商品严重不符合描述"
								}else if(temp[i].rdOption==1){
									temp[i].rdOption="临时加价"
								}else{
									temp[i].rdOption="保证期内拒绝服务"
								}
							}
							this. Businesstransfer = temp
						}
						else{
							this. Businesstransfer = ""
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
			}
		},
	checkXin(flag){//待审核新
			if(flag==0){
				needCheckFlag=1;
				this.$http.post(requestIP+'/admin/waitCheckDeviceList',{
					dHand:1
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							var temp = result.body.data
							for(var i=0;i<temp.length;i++){
								if(temp[i].rdOption==0){
									temp[i].rdOption="商品严重不符合描述"
								}else if(temp[i].rdOption==1){
									temp[i].rdOption="临时加价"
								}else{
									temp[i].rdOption="保证期内拒绝服务"
								}
							}
							this.Shoprental = temp
						}
						else{
							this.Shoprental = ""
							layer.msg('暂无数据',{time: 1500})
						}
					}).catch(function(){
							layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
					});
				
			}
			else{//已审核新
				needCheckFlag=0;
			this.$http.post(requestIP+'/admin/checkedReportDeviceList',{
					dHand:1
				},{emulateJSON:true}).then(result=>{				
						if(result.body.resultCode == 10000){
							var temp = result.body.data
							for(var i=0;i<temp.length;i++){
								if(temp[i].rdOption==0){
									temp[i].rdOption="商品严重不符合描述"
								}else if(temp[i].rdOption==1){
									temp[i].rdOption="临时加价"
								}else{
									temp[i].rdOption="保证期内拒绝服务"
								}
							}
							this.Shoprental = temp
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

