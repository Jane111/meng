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

    //------------- Init pie charts -------------//
    //pass the variables to pie chart init function
    //first is line width, size for pie, animated time , and colours object for theming.
	initPieChart(10,40, 1500, colours);

 	
});

//Setup easy pie charts in sidebar
var initPieChart = function(lineWidth, size, animateTime, colours) {
	$(".pie-chart").easyPieChart({
        barColor: colours.dark,
        borderColor: colours.dark,
        trackColor: '#d9dde2',
        scaleColor: false,
        lineCap: 'butt',
        lineWidth: lineWidth,
        size: size,
        animate: animateTime
    });
}

var content = new Vue({
    el:"#content",
    data:{     
		sId:"",
		details:{
			
		},
		map:{
			0:'商品严重不符合描述', 
			1:'临时加价',
			2:'保证期内拒绝服务'
		},
		smap:{
			0:'餐厅', 
			1:'外卖店',
			2:'奶茶店', 
			3:'西餐厅', 
			4:'小吃摊位', 
			5:'美食城档口'
		},
		reducetype:0,//拒绝类型
		reducetext:"",//拒绝原因
    },
    created(){
		var getdata = this.GetRequest()
		this.sId = getdata.sId
		this.showAbStore()
    },
    methods:{
		//接受mmid
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
   
		//显示店铺详情
		showAbStore(){
			this.$http.post(requestIP+'/admin/showAbStore',
			{
				asId:this.sId//供货商ID
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					this.details = result.body.data
					// layer.msg('通过成功',{time: 1500})	
					// window.location.href = '../pages/user-audit.html';        
				}
			})
		},

		//审核店铺
		checkAbStore(type){
			this.$http.post(requestIP+'/admin/checkAbStore',
			{
				sId:this.sId,//供货商ID
				asId:this.details.asId,
				asStatus:type
			},{emulateJSON:true}).then(result=>{				
				if(result.body.resultCode == 10000){
					layer.msg('处理成功',{time: 1500})	
					window.location.href = 'shopReport.html';        
				}
			})
		},
		//审核拒绝
		reducem(){
			if(this.reducetype == 0){
				layer.msg('请选择退回原因',{time: 1500})
			}
			else if(this.reducetext == ''){
				layer.msg('请填写详细原因',{time: 1500})
			}
			else{
				this.$http.post(requestIP+'8001/admin/UpdateMText',
				{
					mmId:this.mmid,//供货商ID
					mmFialReasonSelect:this.reducetype,//品牌商审核失败原因选择
					mmFailReasonText:this.reducetext,//品牌商审核失败的原因说明
				},{emulateJSON:true}).then(result=>{				
					if(result.body.resultCode == 10000){
						layer.msg('退回成功',{time: 1500})	
						window.location.href = '../pages/user-audit.html'; 										
					}
				})
			}			
		},
    }
})