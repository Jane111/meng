var adminstate = localStorage.getItem("adminstate"); //获取管理员身份

if(adminstate==null){
    // alert('身份异常，请重新登录')
    // window.location.href='../pages/login.html'
}
Vue.component('sidebar-component',    
{        
    template: `<div id="sidebar">
                    <div class="sidebar-inner">
                        <ul id="sideNav" class="nav nav-pills nav-stacked">

                            <li><a href="index.html">首页<i class="im-home"></i></a>
                            </li>

                            <li><a href="user.html"><i class="st-user"></i>用户管理</a>
                            </li>

                            <li>
                                <a href="#">店铺管理<i class="st-uniE606"></i></a>
                                <ul class="nav sub">
                                    <li><a href="shopAudit.html"><i class="en-arrow-right7"></i>审核发布店铺</a>
                                    </li>
                                    <li><a href="shopReport.html"><i class="en-arrow-right7"></i>审核举报店铺</a>
                                    </li>                                   
                                </ul>
                            </li>

                            <li>
                                <a href="#">设备管理<i class="st-settings"></i></a>
                                <ul class="nav sub">
                                    <li><a href="deviceAudit.html"><i class="en-arrow-right7"></i>审核发布设备</a>
                                    </li>
                                    <li><a href="providerAudit.html"><i class="en-arrow-right7"></i>审核入住设备商</a>
                                    </li>   
                                    <li><a href="deviceReport.html"><i class="en-arrow-right7"></i>审核举报设备</a>
                                    </li>                                 
                                </ul>
                            </li>         
                            
                            <li><a href="deposit.html">押金管理<i class="fa-money"></i></a>                              
                            </li>

                            <li>
                                <a href="#">合同管理<i class="im-file-word"></i></a>
                                <ul class="nav sub">
                                    <li><a href="contractupload.html"><i class="en-arrow-right7"></i>上传合同模板</a>
                                    </li>
                                    <li><a href="contractAudit.html"><i class="en-arrow-right7"></i>审核合同</a>
                                    </li>
                                </ul>
                            </li>                          

                            <li>
                                <a href="subaccount.html">系统管理<i class="im-spinner3"></i></a>
                                <ul class="nav sub">
                                    <li><a href="cueword.html"><i class="en-arrow-right7"></i>提示词管理</a>
                                    </li>
                                    <li><a href="foundStore.html"><i class="en-arrow-right7"></i>定制化找店</a>
                                    </li>
                                    <li><a href="platform.html"><i class="en-arrow-right7"></i>平台设置</a>
                                    </li>
                                    <li><a href="advert.html"><i class="en-arrow-right7"></i>轮播图设置</a>
                                    </li>
                                </ul>
                            </li>                           
                        </ul>                       
                    </div>
                    </div>`,
        data:function(){
            return{
              
            }
        },
        created() {
          
        },
        methods:{

        }
    })

Vue.component('header-component', 
{
    template: `<div id="header">
                <div class="container-fluid">
                    <div class="navbar">
                        <div class="navbar-header">
                            <a class="navbar-brand" href="index.html">
                                <span class="text-logo">&emsp;萌系</span><span class="text-slogan">餐饮人</span> 
                            </a>
                        </div>
                        <nav class="top-nav" role="navigation">
                            <ul class="nav navbar-nav pull-left">
                                <li id="toggle-sidebar-li">
                                    <a href="#" id="toggle-sidebar"><i class="en-arrow-left2"></i></a>
                                </li>
                                <li>
                                    <a href="#" class="full-screen"><i class="fa-fullscreen"></i></a>
                                </li>
                            </ul>
                            
                            <ul class="nav navbar-nav pull-right">
                                
                                <li class="dropdown">
                                    <a href="#" data-toggle="dropdown">
                                        欢迎您~ 管理员</a>
                                    <ul class="dropdown-menu right" role="menu">
                                        <li><a href="login.html"><i class="im-exit"></i>退出</a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
                <!-- Start .header-inner -->
                </div>`,
        data:function(){
            return{
               
            }
        },
        created() {
           
        },
        methods:{        
           
        }
    })


var sidebar = new Vue({
    el: '.sidebar',
    data:{

    },
    
})

var header = new Vue({
    el: '.header',
})

