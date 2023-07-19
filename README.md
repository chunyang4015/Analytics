# Android 数据埋点基础库

## 介绍

此库并未使用 「AOP方式」插入统计代码，而是采用「手动添加方式」

这样做虽然代码侵入性较高，但是又有高度定制型的优点，再加上公司业务需求高度定制规则较多，因此选择此方式进行设计开发。

基础库整体架构流程如下图：

![架构](./image/架构.png)

### 事件

* Click 点击事件
* Screen 屏幕事件
* Other 其他事件

```kotlin
data class EventData(
    val sid: String,//APP 生命周期内唯一ID
    val type: String,//类型 [EVENT] [SCREEN]
    val name: String,//事件名称
    val el: String,//事件标签
    private val msg: Map<String, String>?//自定义参数
)
```

代码示例：

```kotlin
//自定义事件
AEvent.event("share_image", "click", mutableMapOf("imageName" to "xxx.png"))

//屏幕事件示例
override fun onResume() {
    super.onResume()
    AEvent.screen(MainActivity::class.java.name, "show")
}

override fun onStop() {
    super.onStop()
    AEvent.screen(MainActivity::class.java.name, "dismiss")
}
```

### 协程队列



### 数据库

### 推送服务

## 使用

## 版本
