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
				ssid:"",
				sdId:"",
				sName:'',//对应店铺名称
				imageDisplay:false,
				details:{
					ssId:'',//用户签约店铺ID
					sdProblem:'',//问题描述
					sdPhoto:'',//图片证据上传
					sdApplyName:'',//申请人姓名	
					sdApplyNum:'',//身份证号	
					sdApplyPhone:''//联系电话	
				},
				reducetype:0,//拒绝类型
				reducetext:"",//拒绝原因
    },
    created(){
		var getdata = this.GetRequest()
		this.ssid=getdata.ssid
		this.sdId = getdata.sdId
		this.sName=getdata.sName
		this.showdetail()
    },
    methods:{
		//获取退款申请详细信息
		showdetail(){
			this.$http.post(requestIP+'/admin/showTuiApplyDetail',
			{
				sdId:this.sdId//商ID
			},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						var temp = result.body.data
						temp.sdPhoto = temp.sdPhoto.split("###")
						this.details = temp
						console.log(temp)
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
			this.$http.post(requestIP+'/admin/agreeTuiApply',
			{
				ssId:this.ssid
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('审核通过',{time: 1500})	
					window.location.href = '../pages/deposit.html';        
				}
			})
		},
		//审核拒绝
		reducem(){
				this.$http.post(requestIP+'/admin/refuseTuiApply',
				{
					ssId:this.ssid,
				},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						layer.msg('审核失败',{time: 1500})	
						window.location.href = '../pages/deposit.html'; 									
					}
				})			
		},
    }
})