密度##帮帮助手开发文档  
###1.1开发目的：  
目前该类新机及注册软件市场拥有较大潜力，我们将进行一次完整的开发。
###1.2项目背景  
软件名称：帮帮助手  
项目github托管地址[https://github.com/790710371/bangbanghelper/](https://github.com/790710371/bangbanghelper/ "github项目地址")	
###1.3术语说明
接口：interface  
类：Class  
对象：Object
###1.4参考资料
xposed API地址https://github.com/rovo89/XposedBridge/wiki/Using-the-Xposed-Framework-API
Xposed框架原理分析:  
一：导入jar包，配置xposed入口，在assets目录下放置入口文件xposed_init，里面放置入口类，格式为包名+类名  
二：配置AndroidManifest.xml文件，新增加三个<meta-data>标签。形式如下

     <!-- Xposed -->
        <meta-data
            android:name="xposedmodule"
            android:value="true" />
        <meta-data
            android:name="xposedminversion"
            android:value="83" />
        <meta-data
            android:name="xposeddescription"
            android:value="@string/xposed_app_description" />
 
三：编写xposed入口类，继承IXposedHookLoadPackage类。重写handleLoaderPackage方法。IXposedHookLoadPackage方法的函数如下：  

    `public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable { }`
在这个方法内部，我们需要调用hookAllMethods方法，里面传入三个参数，第一个参数是所需要Hook的类，第二个是所需要Hook的方法名，第三个是回调接口XC_MethodHook。在这个回调接口内实现了2个函数，一个是Hook前，一个是Hook后。具体代码如下：  
    `  @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }`
	`   @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }`

屏幕分辨率修改方法：
480X800
720X1280
1080X1920
480X854(小米)

IMEI为15位数字
###2. 项目概述  
###2.1软件的一般描述
主要目的是实现对设备信息及其他信息的修改实现新机功能
###2.2软件的功能  

###2.3用户特征和水平
使用Android手机的普通人员
###2.4运行环境  
Android4.0以上系统
###2.5开发规范
- 开发工具：Android studio
- 开发语言：Java
###3．功能需求
###3.1功能划分
- 一键新机
- 功能注册
###4. 外部接口需求
 
###4.1 用户界面
- 主界面(MainActivity)
	- 一键新机
	- 功能注册
- xposed安装界面(XposedInstallAty)
	- 检查root权限
	- 安装xposed(apk)
	- 安装模块
	- 完成
- 设置界面(SettingAty)
- 设备信息界面(DeviceInfoAty)
	- 随机生成数据
	- 保存
- 帮助界面(HelperAty)
	- 使用步骤详细图文说明
- 反馈界面(FeedBackAty)
	- 联系方式
	- 邮箱
	- QQ


###4.2 通信接口
短信验证码接口(后面再具体写)
###4.3 故障处理
出现故障后及时跟开发者协同解决  
###5.1 数据精确度
精准
###5.2 时间特性
- 响应时间：快
- 更新处理时间：快
- 数据转换与传输时间：快