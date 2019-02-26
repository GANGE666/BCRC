# 链安控的移动端APP

## JARVISS
独立开发版本：[JARVISS https://github.com/GANGE666/BCRC/tree/master](https://github.com/GANGE666/BCRC/tree/master)

## BCRC
最终版本：[BCRC https://github.com/GANGE666/BCRC/tree/BCRC](https://github.com/GANGE666/BCRC/tree/BCRC)


# BCRC

###项目说明
>- 项目当前主要完成了框架的搭建和界面的部分（2018年5月26日）
>- 需要注意的地方和需要进行数据库操作来实现数据填充的部分都基本写在了TODO中，利用Android Studio可以方便查看
>- 项目使用的库都在build.gradle(Module:app)中进行设置依赖，在build.gradle(Project:BCRC)中只是设置了对
> Java lamda表达式的支持



###app模块说明
>- 包含了本应用的前端相关的代码，包括Activity、Fragment以及一些工具类等
>- 各类配置，以及各种依赖
>- 暂时还未进行数据库的相关操作，在后期建议将数据库的操作放在别的module中，包括把app模块的bean包移至其中

###额外说明
> 1. 在使用RefreshableListView的时候，可以仿照ControlFragment中进行 
> 注意要创建两个类。分别为对应的javabean和viewHolder，适配器需要利用ExtraViewAdapter<>(HOLDER_CREATOR)
> 来创建，其中HOLDER_CREATOR在对应的ViewHolder中创建
> 2. targetSdkVersion为25，minSdkVersion为18，对应的依赖的库也在这个基础上进行选择，若出现编译问题，可
> 在build.gradle中修改
> 3. 在Android7.0之后，Google修改了文件权限，传递Uri要使用FileProvider，需要在AndroidManifest中定义，并
> 在对应的xml文件中配置路径，这部分在拍照或相册选取用户头像的ProfileActivity已经完成了实现，之后若需要获
> 取其他的文件，在7.0及以上系统中也要使用类似的方法
