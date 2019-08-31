//------------- gallery.js -------------//
$(document).ready(function() {
	//get object with colros from plugin and store it.
	var objColors = $('body').data('sprFlat').getColors();
	var colours = {
		white: objColors.white,
		dark: objColors.dark,
		red : objColors.red,
		blue: objColors.blue,
		green : objColors.green,
		yellow: objColors.yellow,
		brown: objColors.brown,
		orange : objColors.orange,
		purple : objColors.purple,
		pink : objColors.pink,
		lime : objColors.lime,
		magenta: objColors.magenta,
		teal: objColors.teal,
		textcolor: '#5a5e63',
		gray: objColors.gray
	}

	//------------- MixitUp sorting -------------//
	$('.gallery-inner').mixItUp({
		animation: {
	        effects: 'fade translateZ(500px)',
	        duration: 300
	    },
	}); 

	//------------- Open image -------------//
	$('.gallery-inner').magnificPopup({
	  	delegate: 'a.gallery-image-open', // child items selector, by clicking on it popup will open
	  	type: 'image',
	  	gallery: {
	    	enabled: true
	    }
	});

	//------------- hover direction plugin -------------//
    $(function () {
		$('.mix').hoverDirection();      
		$('.mix .gallery-image-controls ').on('animationend', function (event) {
			var $box = $(this).parent();
			$box.filter('[class*="-leave-"]').hoverDirection('removeClass');
		});
    });
  

	//------------- Sparklines -------------//
	$('#usage-sparkline').sparkline([35,46,24,56,68, 35,46,24,56,68], {
		width: '180px',
		height: '30px',
		lineColor: colours.dark,
		fillColor: false,
		spotColor: false,
		minSpotColor: false,
		maxSpotColor: false,
		lineWidth: 2
	});

	$('#cpu-sparkline').sparkline([22,78,43,32,55, 67,83,35,44,56], {
		width: '180px',
		height: '30px',
		lineColor: colours.dark,
		fillColor: false,
		spotColor: false,
		minSpotColor: false,
		maxSpotColor: false,
		lineWidth: 2
	});

	$('#ram-sparkline').sparkline([12,24,32,22,15, 17,8,23,17,14], {
		width: '180px',
		height: '30px',
		lineColor: colours.dark,
		fillColor: false,
		spotColor: false,
		minSpotColor: false,
		maxSpotColor: false,
		lineWidth: 2
	});
});

var content = new Vue({
    el:"#content",
    data:{     
				dbId:"",
				checkDisplay:false,
				imageDisplay:false,
				details:{
					dbDcpt:'',//二手商自我描述
					dbName:'',// 二手商姓名
					dbLoc:'',//二手商位置
					dbLat:'',//二手商经度
					dbLng:'',// 二手商纬度
					dbDeviceType:'',// 二手商设备类型
					dbPhone:'',// 二手商联系方式
					dbOtherConnect:'',// 其他联系方式
					dbOtherType:'',// 其他联系方式的类型
					dbImg:'',// 证件照
					dbImgone:'',//大图
				},
				reducetext:"",//拒绝原因
    },
    created(){
		var getdata = this.GetRequest()
		this.dbId = getdata.dbId
		var dbId=getdata.dbId
		var merchant=getdata.merchant
		var verify=getdata.verify
		console.log(verify)
		if(merchant=='2'){//是否是新设备商，只有新设备商才会有图片需要显示
			this.imageDisplay=true
			this.showNew()
		}else if(merchant=='1'){//是否是二手商
			this.showSecond()
		}
		else{//是否是维修商
			this.showFix()
		}
		if(verify=='yes'){//是否需要审核
			this.checkDisplay=true	
		}
    },
    methods:{
		//获取新设备商家详细信息
		showNew(){
			this.$http.post(requestIP+'/admin/getNewInfo',
			{
				dbId:this.dbId//商ID
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.details = result.body.data
					}
					else{
						layer.msg('请求失败',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
		},
		//获取二手商家详细信息
		showSecond(){
			this.$http.post(requestIP+'/admin/getSecondHandInfo',
			{
				dbId:this.dbId//商ID
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.details = result.body.data
						console.log(result)
					}
					else{
						layer.msg('请求失败',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
		},
		//获取维修商家详细信息
		showFix(){
			this.$http.post(requestIP+'/admin/getRepairInfo',
			{
				dbId:this.dbId//商ID
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						this.details = result.body.data
						console.log(result)
					}
					else{
						layer.msg('请求失败',{time: 1500})
					}
				}).catch(function(){
						layer.msg('服务器异常',{time: 1500})//访问不到返回服务器异常
				});
		},
		GetRequest() {
			var url = location.search; //获取url中"?"符后的字串
			var theRequest = new Object();
			if (url.indexOf("?") != -1) {
				var str = url.substr(1);
				strs = str.split("&");
				for (var i = 0; i < strs.length; i++) {
					theRequest[strs[i].split("=")[0]] = decodeURIComponent(strs[i].split("=")[1]);
				}
			}
			return theRequest;
		},
   
		//审核通过
		agreem(){
			this.$http.post(requestIP+'/admin/passOrNotDBusiness',
			{
				dbId:this.dbId,
				operate:0//0通过1拒绝
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('通过成功',{time: 1500})	 
					window.location.href = '../pages/providerAudit.html'; 							
				}
			})
		},
		//审核拒绝
		reducem(){
				this.$http.post(requestIP+'/admin/passOrNotDBusiness',
				{
					dbId:this.dbId,
					operate:1
				},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						layer.msg('退回成功',{time: 1500})	
						window.location.href = '../pages/providerAudit.html'; 										
					}
				})		
		},
    }
})