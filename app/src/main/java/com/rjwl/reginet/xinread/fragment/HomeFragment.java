package com.rjwl.reginet.xinread.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.activity.BookIntroActivity;
import com.rjwl.reginet.xinread.activity.BookLibraryActivity;
import com.rjwl.reginet.xinread.activity.BookStackActivity;
import com.rjwl.reginet.xinread.activity.BooksActivity;
import com.rjwl.reginet.xinread.activity.NewsActivity;
import com.rjwl.reginet.xinread.adapter.BookStackAdapter;
import com.rjwl.reginet.xinread.adapter.ViewPagerAdapter;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.BookStack;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.presenter.BooksPresenter;
import com.rjwl.reginet.xinread.presenter.BooksStackPresenter;
import com.rjwl.reginet.xinread.presenter.MyBooksView;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yanle on 2018/5/2.
 * 主页
 */

public class HomeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, MyBooksView {
    private Context mContext;
    //单选按钮
    private RadioGroup rgHomeFragment;
    //按钮数组
    private RadioButton[] rbs;
    private Rect rect;
    private Drawable[] drawables;


    //轮播图片相关
    private LinearLayout linearLayout;
    private ViewPager homeViewPager;
    //临时图片
    String[] imageUris = {
            "http://img15.3lian.com/2016/h1/68/d/134.jpg",
            "http://img3.redocn.com/tupian/20160330/weimeiertongdushuguanggaosheji_6084043.jpg",
            "https://b-ssl.duitang.com/uploads/item/201207/29/20120729222617_tLMfx.thumb.700_0.jpeg"};
    SimpleDraweeView[] simpleDraweeViews;
    private ImageView[] points;// 圆点集合
    private int currentItem;
    private ScheduledExecutorService executor;
    private ViewPagerAdapter viewPagerAdapter;

    private SearchView svBooks;//搜索框
    private View view;

    private TextView tvBookMore;
    //书单推荐下的展示
    //private ImageView imgBookOne;
    private SimpleDraweeView imgBookOne;
    private TextView tvBookOneName;
    //private ImageView imgBookTwo;
    private SimpleDraweeView imgBookTwo;
    private TextView tvBookTwoName;
    //private ImageView imgBookThree;
    private SimpleDraweeView imgBookThree;
    private TextView tvBookThreeName;

    //private ListView lvHomeBookList;
    //书单列表集合
    private List<Book> bookList;

    private String tvc = "";
    private BooksPresenter booksPresenter;
    private List<BookStack> bookStackList;
    //private BookStackAdapter bookStackAdapter;
    private BooksStackPresenter booksStackPresenter;

    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;
    private View view6;
    private View view7;
    private View view8;
    private View view9;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        this.mContext = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        booksPresenter = new BooksPresenter(this);
        booksStackPresenter = new BooksStackPresenter(this);

        rbs = new RadioButton[4];

        //选项按钮
        rgHomeFragment = view.findViewById(R.id.rg_fragment_home);
        rbs[0] = view.findViewById(R.id.rb_fragment_book_list);
        rbs[1] = view.findViewById(R.id.rb_book_stack);
        rbs[2] = view.findViewById(R.id.rb_teacher);
        rbs[3] = view.findViewById(R.id.rb_bookshop);

        //for循环对每一个RadioButton图片进行缩放
        for (int i = 0; i < rbs.length; i++) {
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            drawables = rbs[i].getCompoundDrawables();
            //获取drawables，2/5表示图片要缩小的比例
            rect = new Rect(0, 0, drawables[1].getMinimumWidth() * 2,
                    drawables[1].getMinimumHeight() * 2);
            //定义一个Rect边界
            drawables[1].setBounds(rect);
            //给每一个RadioButton设置图片大小
            rbs[i].setCompoundDrawables(null, drawables[1], null, null);
            //给drawable设置边界
        }

        //搜索框
        svBooks = view.findViewById(R.id.sv_book_serch);

        //lvHomeBookList = view.findViewById(R.id.lv_home_book_stack);
        //推荐书籍
        tvBookMore = view.findViewById(R.id.tv_book_list_more);
        imgBookOne = view.findViewById(R.id.img_book_one);
        imgBookTwo = view.findViewById(R.id.img_book_two);
        imgBookThree = view.findViewById(R.id.img_book_three);

        //书名
        tvBookOneName = view.findViewById(R.id.tv_book_one_name);
        tvBookTwoName = view.findViewById(R.id.tv_book_two_name);
        tvBookThreeName = view.findViewById(R.id.tv_book_three_name);

        rgHomeFragment.setOnCheckedChangeListener(this);

        //绑定点击事件
        tvBookMore.setOnClickListener(this);
        imgBookOne.setOnClickListener(this);
        imgBookTwo.setOnClickListener(this);
        imgBookThree.setOnClickListener(this);

        //轮播图相关
        int size = initSize();
        initTextViews(size);
        initViewPager();

        view1 = view.findViewById(R.id.in_book_stack_1);
        view2 = view.findViewById(R.id.in_book_stack_2);
        view3 = view.findViewById(R.id.in_book_stack_3);
        view4 = view.findViewById(R.id.in_book_stack_4);
        view5 = view.findViewById(R.id.in_book_stack_5);
        view6 = view.findViewById(R.id.in_book_stack_6);
        view7 = view.findViewById(R.id.in_book_stack_7);
        view8 = view.findViewById(R.id.in_book_stack_8);
        view9 = view.findViewById(R.id.in_book_stack_9);

        bookList = new ArrayList<>();
        bookStackList = new ArrayList<>();

        /*bookStackAdapter = new BookStackAdapter(bookStackList, mContext);
        lvHomeBookList.setAdapter(bookStackAdapter);*/

        booksStackPresenter.getBooksStack();
        //初始化书籍集合
        //initBook();
        booksPresenter.showBookList(SaveOrDeletePrefrence.lookInt(mContext.getApplicationContext(), "id") + "");
        booksPresenter.bookShop();//书店链接

        setSearchView();
        svBooks.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("TAG1", "搜索：" + query);
                booksPresenter.serchBook(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TAG1", "输入搜索：" + newText);
                return true;
            }
        });
    }


    //去掉搜索框下划线
    private void setSearchView() {
        if (svBooks != null) {

            int search_mag_icon_id = svBooks.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
            ImageView mSearchViewIcon = svBooks.findViewById(search_mag_icon_id);
            android.widget.LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) mSearchViewIcon.getLayoutParams();

            int id = svBooks.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchTextView = svBooks.findViewById(id);
            //获取出SearchView中间文本的布局参数，跟上面的图标布局文件进行对比
            android.widget.LinearLayout.LayoutParams textLayoutParams = (android.widget.LinearLayout.LayoutParams) searchTextView.getLayoutParams();

            //发现textLayoutParams中的高度是固定108的，而图标的布局文件的高度是-2也就是WrapContent，将文本的高度也改成WrapContent就可以了
            textLayoutParams.height = textLayoutParams.WRAP_CONTENT;
            searchTextView.setLayoutParams(textLayoutParams);
            searchTextView.setTextSize(15);

            try {        //--拿到字节码
                Class<?> argClass = svBooks.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(svBooks);
                //--设置背景
                mView.setBackgroundResource(R.drawable.search_view_line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Intent intent = null;
        switch (checkedId) {
            case R.id.rb_fragment_book_list://书单推荐
                skipBookList(intent);
                rbs[0].setChecked(false);
                break;
            case R.id.rb_book_stack://书库
                intent = new Intent(mContext, BookStackActivity.class);
                startActivity(intent);
                // Toast.makeText(getActivity(), "暂未开通", Toast.LENGTH_SHORT).show();
                rbs[1].setChecked(false);
                break;
            case R.id.rb_teacher://名师推荐
                Toast.makeText(mContext, "暂未开通", Toast.LENGTH_SHORT).show();
                rbs[2].setChecked(false);
                break;
            case R.id.rb_bookshop://书店
                //Toast.makeText(mContext, "暂未开通", Toast.LENGTH_SHORT).show();
                if ("".equals(tvc)) {
                    tvc = "https://s.taobao.com/search?q=%E4%B9%A6%E5%BA%97";
                }
                Uri uri = Uri.parse(tvc);
                Log.d("TAG1", "书籍链接:" + uri.toString());
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
                rbs[3].setChecked(false);
                break;
        }
    }

    //初始化书籍信息
    private void initBook() {
        /*bookList = new ArrayList<>();

        Book book1 = new Book();
        book1.setBookname("天涯明月刀");
        book1.setAuthor("古龙");
        book1.setPrice("10.04");
        book1.setNum("22.8");
        book1.setDetail("《天涯明月刀》的故事是傅红雪拯救自我、拯救他人最终达到自我升华的过程，" +
                "天涯、明月、刀三个词是傅红雪给人的三个意向，" +
                "天涯般辽阔的胸襟，明月般剔透的心灵加上一把“无敌于天下”的快刀。" +
                "代表着古龙在低谷期时挣扎、痛苦过但依然热爱着生活的一种温暖乐观的心态。");
        Book book2 = new Book();
        book2.setBookname("三体");
        book2.setAuthor("刘慈欣");
        book2.setPrice("25.8");
        book2.setNum("10.2");
        book2.setDetail(" 《三体》是刘慈欣创作的系列长篇科幻小说，由《三体》、《三体Ⅱ·黑暗森林》、" +
                "《三体Ⅲ·死神永生》组成，作品讲述了地球人类文明和三体文明的信息交流、" +
                "生死搏杀及两个文明在宇宙中的兴衰历程。");
        Book book3 = new Book();
        book3.setBookname("流星蝴蝶剑");
        book3.setAuthor("古龙");
        book3.setPrice("15.04");
        book3.setNum("20.3");
        book3.setDetail(" 主要讲述了“老伯”孙玉伯的孙府与万鹏王的十二飞鹏帮相互对峙，" +
                "以及孟星魂、小蝶、孙玉伯、律香川、高老大等人各怀心思，" +
                "相互算计，恩恩怨怨，爱恨情仇的故事。");
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);*/
        Uri uri;
        if (bookList != null) {
            /*switch (bookList.size()) {
                case 0:
                    imgBookOne.setVisibility(View.GONE);
                    imgBookTwo.setVisibility(View.GONE);
                    imgBookThree.setVisibility(View.GONE);
                    break;
                case 1:
                    imgBookTwo.setVisibility(View.GONE);
                    imgBookThree.setVisibility(View.GONE);
                    break;
                case 2:
                    imgBookThree.setVisibility(View.GONE);
                    break;

            }*/
            if (bookList.size() >= 1) {
                tvBookOneName.setVisibility(View.VISIBLE);
                imgBookOne.setVisibility(View.VISIBLE);
                tvBookOneName.setText(bookList.get(0).getName());
                uri = Uri.parse(Constant.IMG + bookList.get(0).getHeadPic());
                imgBookOne.setImageURI(uri);
            }
            if (bookList.size() >= 2) {
                tvBookTwoName.setVisibility(View.VISIBLE);
                imgBookTwo.setVisibility(View.VISIBLE);
                tvBookTwoName.setText(bookList.get(1).getName());
                uri = Uri.parse(Constant.IMG + bookList.get(1).getHeadPic());
                imgBookTwo.setImageURI(uri);
            }
            if (bookList.size() >= 3) {
                tvBookThreeName.setVisibility(View.VISIBLE);
                imgBookThree.setVisibility(View.VISIBLE);
                tvBookThreeName.setText(bookList.get(2).getName());
                uri = Uri.parse(Constant.IMG + bookList.get(2).getHeadPic());
                imgBookThree.setImageURI(uri);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int i = 0;
        if (bookList == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_book_list_more://更多
                skipBookList(intent);
                return;
            case R.id.img_book_one:
                if (bookList.size() == 0) {
                    return;
                }
                i = 0;
                break;
            case R.id.img_book_two:
                if (bookList.size() < 2) {
                    return;
                }
                i = 1;
                break;
            case R.id.img_book_three:
                if (bookList.size() < 3) {
                    return;
                }
                i = 2;
                break;
            default:
                break;
        }

        intent = new Intent(mContext, BookIntroActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", bookList.get(i));
        bundle.putString("btnTitle", "+加入书架");
        intent.putExtra("bookBundle", bundle);
        startActivity(intent);

    }

    //跳转到书单推荐
    private void skipBookList(Intent intent) {
        intent = new Intent(mContext, BooksActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("booklist", (Serializable) bookList);
        intent.putExtra("books", bundle);

        intent.putExtra("title", "书单推荐");
        intent.putExtra("btn", "+加入书架");

        startActivity(intent);
    }

    //初始化viewPager
    private void initViewPager() {
        linearLayout = view.findViewById(R.id.viewPager_lineatLayout);
        homeViewPager = view.findViewById(R.id.vp_home);
        viewPagerAdapter = new ViewPagerAdapter(imageUris, simpleDraweeViews, new ViewPagerAdapter.ViewPagerOnClick() {
            @Override
            public void onClick(int posotion, String url) {
                Log.d("viewPager", "ViewPager点击事件： " + posotion + " ; " + url);
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("imageUrl", url);
                startActivity(intent);
            }
        });

        homeViewPager.setAdapter(viewPagerAdapter);
        initPoint();
        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                setCurrentPoint(position % imageUris.length);
                startAutoScroll();//手动切换完成后恢复自动播放
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        currentItem = imageUris.length * 1000; //取一个中间的大数字，防止接近边界
        homeViewPager.setCurrentItem(currentItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoScroll();//激活时自动播放
        if (bookList != null) {
            initBook();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll();//停止播放
    }

    //开始轮播图片
    private void startAutoScroll() {
        stopAutoScroll();
        executor = Executors.newSingleThreadScheduledExecutor();
        Runnable command = new Runnable() {
            @Override
            public void run() {
                selectNextItem();
            }

            private void selectNextItem() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeViewPager.setCurrentItem(++currentItem);
                    }
                });
            }
        };
        int delay = 4;
        int period = 5;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        executor.scheduleAtFixedRate(command, delay, period, timeUnit);
    }

    //停止轮播图片
    private void stopAutoScroll() {
        if (executor != null) {
            executor.shutdownNow();
        }

    }

    //初始化轮播圆点
    private void initPoint() {
        points = new ImageView[imageUris.length];
        //全部圆点设置为未选中状态
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            points[i] = (ImageView) linearLayout.getChildAt(i);
            points[i].setBackgroundResource(R.drawable.nomal);
        }
        //currentItem = 0;
        points[currentItem % imageUris.length].setBackgroundResource(R.drawable.select);
    }

    //设置当前的点
    private void setCurrentPoint(int position) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            points[i] = (ImageView) linearLayout.getChildAt(i);
            points[i].setBackgroundResource(R.drawable.nomal);
        }
        points[position].setBackgroundResource(R.drawable.select);
    }

    private int initSize() {
        int size;
        if (imageUris.length > 3) {
            size = imageUris.length;
        } else {
            size = imageUris.length * 2; // 小于3个时候, 需要扩大一倍, 防止出错
        }
        return size;
    }

    private void initTextViews(int size) {
        SimpleDraweeView[] tvs = new SimpleDraweeView[size];

        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = new SimpleDraweeView(getActivity());
            tvs[i].getHierarchy().setPlaceholderImage(R.drawable.book_library);//默认图片

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tvs[i].setLayoutParams(layoutParams);
            simpleDraweeViews = tvs;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hidenLoading() {

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {
        bookList = booksPresenter.getBookList();
        Intent intent = new Intent(mContext, BooksActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("booklist", (Serializable) bookList);
        intent.putExtra("books", bundle);
        intent.putExtra("title", "书籍搜索");
        intent.putExtra("btn", "+加入书架");
        startActivity(intent);
    }

    @Override
    public void bookShop() {
        tvc = booksPresenter.getTvc();
    }

    @Override
    public void showBooks() {
        List<Book> list1 = booksPresenter.getBookList();
        if (list1 != null) {
            bookList.clear();
            bookList.addAll(list1);
            Log.d("TAG1", "showbooks :" + bookList);
            initBook();
        }
        List<BookStack> list = booksStackPresenter.getBookStackList();
        if (list != null) {
            bookStackList.clear();
            bookStackList.addAll(list);
            initBookStack();
        }
        Log.d("TAG1", "我的书架集合：" + bookList);
    }

    private void initBookStack() {
        switch (bookStackList.size()) {
            case 0:
                break;
            case 1:
                view1.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                break;
            case 2:
                view1.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                view2.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                break;
            case 3:
                view1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                setBookStack(bookStackList.get(2).getClassify(), bookStackList.get(2).getBook(), view3);
                break;
            case 4:
                view1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                setBookStack(bookStackList.get(2).getClassify(), bookStackList.get(2).getBook(), view3);
                setBookStack(bookStackList.get(3).getClassify(), bookStackList.get(3).getBook(), view4);
                break;
            case 5:
                view1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                setBookStack(bookStackList.get(2).getClassify(), bookStackList.get(2).getBook(), view3);
                setBookStack(bookStackList.get(3).getClassify(), bookStackList.get(3).getBook(), view4);
                setBookStack(bookStackList.get(4).getClassify(), bookStackList.get(4).getBook(), view5);
                break;
            case 6:
                view1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                setBookStack(bookStackList.get(2).getClassify(), bookStackList.get(2).getBook(), view3);
                setBookStack(bookStackList.get(3).getClassify(), bookStackList.get(3).getBook(), view4);
                setBookStack(bookStackList.get(4).getClassify(), bookStackList.get(4).getBook(), view5);
                setBookStack(bookStackList.get(5).getClassify(), bookStackList.get(5).getBook(), view6);
                break;
            case 7:
                view1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                view7.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                setBookStack(bookStackList.get(2).getClassify(), bookStackList.get(2).getBook(), view3);
                setBookStack(bookStackList.get(3).getClassify(), bookStackList.get(3).getBook(), view4);
                setBookStack(bookStackList.get(4).getClassify(), bookStackList.get(4).getBook(), view5);
                setBookStack(bookStackList.get(5).getClassify(), bookStackList.get(5).getBook(), view6);
                setBookStack(bookStackList.get(6).getClassify(), bookStackList.get(6).getBook(), view7);
                break;
            case 8:
                view1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                view7.setVisibility(View.VISIBLE);
                view8.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                setBookStack(bookStackList.get(2).getClassify(), bookStackList.get(2).getBook(), view3);
                setBookStack(bookStackList.get(3).getClassify(), bookStackList.get(3).getBook(), view4);
                setBookStack(bookStackList.get(4).getClassify(), bookStackList.get(4).getBook(), view5);
                setBookStack(bookStackList.get(5).getClassify(), bookStackList.get(5).getBook(), view6);
                setBookStack(bookStackList.get(6).getClassify(), bookStackList.get(6).getBook(), view7);
                setBookStack(bookStackList.get(7).getClassify(), bookStackList.get(7).getBook(), view8);
                break;
            case 9:
                view1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                view7.setVisibility(View.VISIBLE);
                view8.setVisibility(View.VISIBLE);
                view9.setVisibility(View.VISIBLE);
                setBookStack(bookStackList.get(0).getClassify(), bookStackList.get(0).getBook(), view1);
                setBookStack(bookStackList.get(1).getClassify(), bookStackList.get(1).getBook(), view2);
                setBookStack(bookStackList.get(2).getClassify(), bookStackList.get(2).getBook(), view3);
                setBookStack(bookStackList.get(3).getClassify(), bookStackList.get(3).getBook(), view4);
                setBookStack(bookStackList.get(4).getClassify(), bookStackList.get(4).getBook(), view5);
                setBookStack(bookStackList.get(5).getClassify(), bookStackList.get(5).getBook(), view6);
                setBookStack(bookStackList.get(6).getClassify(), bookStackList.get(6).getBook(), view7);
                setBookStack(bookStackList.get(8).getClassify(), bookStackList.get(8).getBook(), view9);
                setBookStack(bookStackList.get(7).getClassify(), bookStackList.get(7).getBook(), view8);
                break;
            default:
                break;
        }
    }

    private void setBookStack(final String library, final List<Book> bookList, View view) {
        TextView tvStackClass = view.findViewById(R.id.tv_stack_class);//年级
        TextView tvMore = view.findViewById(R.id.tv_stack_more);//更多

        SimpleDraweeView sdvStackOne = view.findViewById(R.id.img_book_stack_one);
        TextView bookStackOne = view.findViewById(R.id.tv_book_stack_one_name);

        SimpleDraweeView sdvStackTwo = view.findViewById(R.id.img_book_stack_two);
        TextView bookStackTwo = view.findViewById(R.id.tv_book_stack_two_name);

        SimpleDraweeView sdvStackThree = view.findViewById(R.id.img_book_stack_three);
        TextView bookStackThree = view.findViewById(R.id.tv_book_stack_three_name);

        //通过position决定内容
        tvStackClass.setText(library);
        //mBookList = mBookStackList.get(position).getBook();
        //Log.d("TAG1", "BookStackAdapter:集合" + mBookList);
        if (bookList != null && bookList.size() >= 1) {
            bookStackOne.setVisibility(View.VISIBLE);
            sdvStackOne.setVisibility(View.VISIBLE);
            bookStackOne.setText(bookList.get(0).getName());
            sdvStackOne.setImageURI(Uri.parse(Constant.IMG + bookList.get(0).getHeadPic() + ""));
            sdvStackOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TAG1", "书库的点击事件1:" + mBookStackList.get(position).getBook());
                    Intent intent = new Intent(mContext, BookIntroActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", bookList.get(0));
                    bundle.putString("btnTitle", "加入书架");
                    intent.putExtra("bookBundle", bundle);
                    mContext.startActivity(intent);

                }
            });
        }
        if (bookList != null && bookList.size() >= 2) {
            bookStackTwo.setVisibility(View.VISIBLE);
            sdvStackTwo.setVisibility(View.VISIBLE);
            bookStackTwo.setText(bookList.get(1).getName());
            sdvStackTwo.setImageURI(Uri.parse(Constant.IMG + bookList.get(1).getHeadPic() + ""));
            sdvStackTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TAG1", "书库的点击事件2:" + mBookStackList.get(position).getBook());
                    Intent intent = new Intent(mContext, BookIntroActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", bookList.get(1));
                    bundle.putString("btnTitle", "加入书架");
                    intent.putExtra("bookBundle", bundle);
                    mContext.startActivity(intent);
                }
            });
        }
        if (bookList != null && bookList.size() >= 3) {
            bookStackThree.setVisibility(View.VISIBLE);
            sdvStackThree.setVisibility(View.VISIBLE);
            bookStackThree.setText(bookList.get(2).getName());
            sdvStackThree.setImageURI(Uri.parse(Constant.IMG + bookList.get(2).getHeadPic() + ""));
            sdvStackThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TAG1", "书库的点击事件3:" + mBookStackList.get(position).getBook());
                    Intent intent = new Intent(mContext, BookIntroActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", bookList.get(2));
                    bundle.putString("btnTitle", "加入书架");
                    intent.putExtra("bookBundle", bundle);
                    mContext.startActivity(intent);
                }
            });
        }

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转到书库
                Log.d("TAG1", library + "书库");
                Intent intent = new Intent(mContext, BookLibraryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("booklist", (Serializable) bookList);
                bundle.putString("library", library);
                intent.putExtra("books", bundle);
                mContext.startActivity(intent);
            }
        });
    }


}
