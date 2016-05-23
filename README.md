# GooglePhotoSelect
仿照Google相册的页面选择效果

对应文章：http://www.jianshu.com/p/7ba76d211299

![screenshot](https://github.com/weidongjian/GooglePhotoSelect/raw/master/app/art/MyVideoGif.gif)

> 2016.5.23更新

 1. 选择跟反选的逻辑也放到DragSelectTouchListener中，跟adapter进一步解耦
 2. 自动滚动，由ScrollerCompat替代handle，使滚动如丝滑般顺畅
 3. 修复滚动的时候，同时有其他手指触碰屏幕造成的错误