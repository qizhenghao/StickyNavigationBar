# StickyNavigationBar


###### 仿微博、人人的feed详情页面：Listview上下滑动，导航栏view可吸附在顶部的效果。
#### 一、实现效果
上图：

![效果图.gif](http://upload-images.jianshu.io/upload_images/2957973-587d00d0d74e31a4.gif?imageMogr2/auto-orient/strip)

***简书***: http://www.jianshu.com/p/7fe153e8d237

欢迎拍砖，拍拍更进步。

没有对比，怎么会有伤害，下面是 微博、人人的Feed详情页：

![微博、人人Feed详情页.jpeg](http://upload-images.jianshu.io/upload_images/2957973-7e6171769f4998a6.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 二、实现原理
##### 1、
实例化两个一样的导航栏view，一个放在页面根布局顶部的view1，另一个放在ListView的headerView中的view2，在OnScrollListener的onScroll方法中，检测view2在屏幕中的位置是不是滑动到了顶部，决定顶部view1的显示与隐藏，以达到看起来只有一个导航栏view显示的效果；

##### 2、
为了保持两个导航栏view的状态同步，使用了观察者模式；

##### 3、
导航栏中的Tab切换，即切换ListView的adapter，并且记录滑动的位置信息。

---

#### 三、UML图

![UML图.png](http://upload-images.jianshu.io/upload_images/2957973-604921557834768f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
***StickyNavHostSubject***：它把所有的自定义导航栏view的引用保存到一个list里。AbstractSubject提供了接口，可以增加和删除观察者对象。

***StickyNavHost***：自定义的导航栏view，继承自ViewGroup，可以根据具体需求自行更改显示的布局、样式等。

***NavListViewScollListener***：需要为ListView设置的滑动事件，封装了对吸附导航栏显示、隐藏的逻辑。

***MainActivity***：用于演示demo，包含了对导航栏view的初始化，以及切换tab的操作等。

---

#### 三、具体细节
##### 1、
NavListViewScollListener的onScroll()方法：
```

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //处理了root导航栏的显示与隐藏, 本质上只是控制root导航栏的显示
        //而在listView的headerView中的导航栏不做处理，因为它会随着listView的滑动自行滑出页面
        if (NavBean.IS_NEED_ATTACH && rootView != null && nav != null) {
            rootView.getLocationOnScreen(rootLocation);
            headView.getLocationOnScreen(headLocation);
            //根据两者在屏幕中的location位置信息，决定root导航栏的显示与隐藏
            if (rootLocation[1] > headLocation[1]) {
                rootView.setVisibility(View.VISIBLE);
            } else {
                rootView.setVisibility(View.INVISIBLE);
            }

            //记录当前listView的滑动位置
            nav.setFirstVisibleItem(firstVisibleItem);
            nav.setTopDistance((view.getChildAt(0) == null) ? 0 : view.getChildAt(0).getTop());
        }
    }
```
##### 2、
MainActivity中初始化操作：
```
    private void initNavsView() {
        initNavsData();
        stickyNavHostRoot.setTabItemClickListener(this);//设置点击回调
        stickyNavHostHead.setTabItemClickListener(this);//设置点击回调
        stickyNavHostRoot.setShowTopLine(false);

        stickNavHostSubject = new StickNavHostSubject();
        stickNavHostSubject.attachObserver(stickyNavHostRoot);//观察者模式
        stickNavHostSubject.attachObserver(stickyNavHostHead);

        NavBean[] sortedNavs = new NavBean[mNavs.size()];//指定导航栏的排列顺序
        sortedNavs[0] = mNavs.get(NavBean.TYPE_REPOST);
        sortedNavs[1] = mNavs.get(NavBean.TYPE_COMMENT);
        sortedNavs[2] = mNavs.get(NavBean.TYPE_LIKE);
        stickNavHostSubject.initTabData(sortedNavs);

        scrollListener = new NavListViewScrollListener(stickyNavHostRoot, stickyNavHostHead);
        mListView.setOnScrollListener(scrollListener);//为listView设置滑动监听，内部处理了吸附view的显示与隐藏
    }

    protected void initNavsData() {
        mNavs = new SparseArray<>(NAV_LENGTH);
        mNavs.put(NavBean.TYPE_REPOST, new NavBean(NavBean.TYPE_REPOST, new TestAdapter(20, "我是转发", this)));
        mNavs.put(NavBean.TYPE_COMMENT, new NavBean(NavBean.TYPE_COMMENT, new TestAdapter(20, "我是评论", this)));
        mNavs.put(NavBean.TYPE_LIKE, new NavBean(NavBean.TYPE_LIKE, new TestAdapter(20, "我是赞", this)));
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        stickyNavHostRoot = (StickyNavHost) findViewById(R.id.sticky_nav_layout);
        stickyNavHostRoot.setVisibility(View.INVISIBLE);

        View testHeaderView = LayoutInflater.from(this).inflate(R.layout.listview_head_view_test_layout, null);
        mListView.addHeaderView(testHeaderView);

        View inflateView = LayoutInflater.from(this).inflate(R.layout.sticky_nav_host_layout, null);
        stickyNavHostHead = (StickyNavHost) inflateView.findViewById(R.id.sticky_nav_layout);
        stickyNavHostHead.setVisibility(View.VISIBLE);
        mListView.addHeaderView(stickyNavHostHead);
        STICKY_POSITION_IN_HEADER = mListView.getHeaderViewsCount();
    }
```
##### 3、
MainActivity中点击切换导航栏Tab的回调：
```
    @Override
    public void onTabItemSelected(@NavBean.TYPE int type) {
        NavBean currNav = mNavs.get(type);
        stickNavHostSubject.setSelectedType(type);//事件分发给注册者，注册者进行相应的变化
        if (currNav.type == NavBean.TYPE_CURRENT)//等于当前选中的tab，可以屏蔽掉
            return;
        NavBean.TYPE_CURRENT = currNav.type;
        scrollListener.setNav(currNav);
        mListView.setAdapter(currNav.adapter);
        if (stickyNavHostRoot.getVisibility() == View.VISIBLE) {//吸附在顶部的rootView正在展示
            if (currNav.getFirstVisibleItem() < STICKY_POSITION_IN_HEADER)
                mListView.setSelectionFromTop(STICKY_POSITION_IN_HEADER, stickyNavHostRoot.getHeight() - 2);
            else
                mListView.setSelectionFromTop(currNav.getFirstVisibleItem(), currNav.getTopDistance());
        } else {//吸附在顶部的rootView没有展示，说明在切换导航栏的时候是不需要进行滑动的，保持上次的位置即可
            mListView.setSelectionFromTop(NavBean.firstVisibleItemUniversal, NavBean.topDistanceUniversal);
        }
    }
```
##### 4、
StickyNavHostSubject做的事情就很简单了，和常见的观察者模式没区别：
```
public class StickNavHostSubject extends AbstractSubject<IStickyNavHostObserver> {

    private List<IStickyNavHostObserver> observers;

    public StickNavHostSubject() {
        observers = new ArrayList<>();
    }

    public void attachObserver(IStickyNavHostObserver observer) {
        observers.add(observer);
    }

    public void detachObserver(IStickyNavHostObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void initTabData(NavBean[] navs) {
        for (IStickyNavHostObserver observer : observers)
            observer.initTabData(navs);
    }

    @Override
    public void refreshTabData(NavBean nav) {
        for (IStickyNavHostObserver observer : observers)
            observer.refreshTabData(nav);
    }

    @Override
    public void setSelectedType(@NavBean.TYPE int type) {
        for (IStickyNavHostObserver observer : observers)
            observer.setSelectedType(type);
    }

    @Override
    public void setSelectedPosition(int position) {
        for (IStickyNavHostObserver observer : observers)
            observer.setSelectedPosition(position);
    }
```
