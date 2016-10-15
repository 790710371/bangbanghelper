##帮帮助手开发文档  
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
- xposed API地址https://github.com/rovo89/XposedBridge/wiki/Using-the-Xposed-Framework-API
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
	- 安装xposed
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