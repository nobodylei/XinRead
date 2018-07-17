package com.rjwl.reginet.xinread.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.activity.BookIntroActivity;
import com.rjwl.reginet.xinread.activity.BookLibraryActivity;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.BookStack;
import com.rjwl.reginet.xinread.model.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 * 书库适配器
 */

public class BookStackAdapter extends BaseAdapter {
    private List<BookStack> mBookStackList;
    private List<Book> mBookList;
    private Context mContext;

    public BookStackAdapter(List<BookStack> bookStackList, Context context) {
        this.mBookStackList = bookStackList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mBookStackList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookStackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //返回项的View
        //持有者
        Wrapper wrapper = null;
        //解析成view
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.item_book_stack, parent, false);
            //将布局文件解析成View的过程，需要资源
            wrapper = new Wrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (Wrapper) row.getTag();
        }
        TextView tvStackClass = wrapper.getTvStackClass();//年级
        TextView tvMore = wrapper.getTvMore();//更多

        SimpleDraweeView sdvStackOne = wrapper.getSdvStackOne();
        TextView bookStackOne = wrapper.getBookStackOne();

        SimpleDraweeView sdvStackTwo = wrapper.getSdvStackTwo();
        TextView bookStackTwo = wrapper.getBookStackTwo();

        SimpleDraweeView sdvStackThree = wrapper.getSdvStackThree();
        TextView bookStackThree = wrapper.getBookStackThree();

        final String library = mBookStackList.get(position).getClassify() + "";
        //通过position决定内容
        tvStackClass.setText(library);
        mBookList = mBookStackList.get(position).getBook();
        //Log.d("TAG1", "BookStackAdapter:集合" + mBookList);
        if (mBookList != null && mBookList.size() >= 1) {
            bookStackOne.setVisibility(View.VISIBLE);
            sdvStackOne.setVisibility(View.VISIBLE);
            bookStackOne.setText(mBookList.get(0).getName());
            sdvStackOne.setImageURI(Uri.parse(Constant.IMG + mBookList.get(0).getHeadPic() + ""));
            sdvStackOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TAG1", "书库的点击事件1:" + mBookStackList.get(position).getBook());
                    Intent intent = new Intent(mContext, BookIntroActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", mBookStackList.get(position).getBook().get(0));
                    bundle.putString("btnTitle", "加入书架");
                    intent.putExtra("bookBundle", bundle);
                    mContext.startActivity(intent);

                }
            });
        }
        if (mBookList != null && mBookList.size() >= 2) {
            bookStackTwo.setVisibility(View.VISIBLE);
            sdvStackTwo.setVisibility(View.VISIBLE);
            bookStackTwo.setText(mBookList.get(1).getName());
            sdvStackTwo.setImageURI(Uri.parse(Constant.IMG + mBookList.get(1).getHeadPic() + ""));
            sdvStackTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TAG1", "书库的点击事件2:" + mBookStackList.get(position).getBook());
                    Intent intent = new Intent(mContext, BookIntroActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", mBookStackList.get(position).getBook().get(1));
                    bundle.putString("btnTitle", "加入书架");
                    intent.putExtra("bookBundle", bundle);
                    mContext.startActivity(intent);
                }
            });
        }
        if (mBookList != null && mBookList.size() >= 3) {
            bookStackThree.setVisibility(View.VISIBLE);
            sdvStackThree.setVisibility(View.VISIBLE);
            bookStackThree.setText(mBookList.get(2).getName());
            sdvStackThree.setImageURI(Uri.parse(Constant.IMG + mBookList.get(2).getHeadPic() + ""));
            sdvStackThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TAG1", "书库的点击事件3:" + mBookStackList.get(position).getBook());
                    Intent intent = new Intent(mContext, BookIntroActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book", mBookStackList.get(position).getBook().get(2));
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
                bundle.putSerializable("booklist", (Serializable) mBookStackList.get(position).getBook());
                bundle.putString("library", library);
                intent.putExtra("books", bundle);
                mContext.startActivity(intent);
            }
        });


        return row;
    }

    /*MyListener myListener = new MyListener();

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mBookList == null || mBookList.size() <= 0) {
                return;
            }
            Intent intent = new Intent(mContext, BookIntroActivity.class);
            Bundle bundle = new Bundle();
            int i = 0;
            switch (v.getId()) {
                case R.id.img_book_stack_one:
                    i = 0;
                    break;
                case R.id.img_book_stack_two:
                    if (mBookList.size() < 2) {
                        return;
                    }
                    i = 1;
                    break;
                case R.id.img_book_stack_three:
                    if (mBookList.size() < 3) {
                        return;
                    }
                    i = 2;
                    break;
            }
            bundle.putSerializable("book", mBookList.get(i));
            bundle.putString("btnTitle", "加入书架");
            intent.putExtra("bookBundle", bundle);
            mContext.startActivity(intent);
        }
    }*/

    class Wrapper {
        TextView tvStackClass;//年级
        TextView tvMore;

        SimpleDraweeView sdvStackOne;
        TextView bookStackOne;

        SimpleDraweeView sdvStackTwo;
        TextView bookStackTwo;

        SimpleDraweeView sdvStackThree;
        TextView bookStackThree;


        View row;

        public Wrapper(View row) {
            this.row = row;
        }

        public TextView getTvMore() {
            if (tvMore == null) {
                tvMore = row.findViewById(R.id.tv_stack_more);
            }
            return tvMore;
        }

        public TextView getTvStackClass() {
            if (tvStackClass == null) {
                tvStackClass = row.findViewById(R.id.tv_stack_class);
            }
            return tvStackClass;
        }


        public SimpleDraweeView getSdvStackOne() {
            if (sdvStackOne == null) {
                sdvStackOne = row.findViewById(R.id.img_book_stack_one);
            }
            return sdvStackOne;
        }


        public TextView getBookStackOne() {
            if (bookStackOne == null) {
                bookStackOne = row.findViewById(R.id.tv_book_stack_one_name);
            }
            return bookStackOne;
        }


        public SimpleDraweeView getSdvStackTwo() {
            if (sdvStackTwo == null) {
                sdvStackTwo = row.findViewById(R.id.img_book_stack_two);
            }
            return sdvStackTwo;
        }


        public TextView getBookStackTwo() {
            if (bookStackTwo == null) {
                bookStackTwo = row.findViewById(R.id.tv_book_stack_two_name);
            }
            return bookStackTwo;
        }


        public SimpleDraweeView getSdvStackThree() {
            if (sdvStackThree == null) {
                sdvStackThree = row.findViewById(R.id.img_book_stack_three);
            }
            return sdvStackThree;
        }


        public TextView getBookStackThree() {
            if (bookStackThree == null) {
                bookStackThree = row.findViewById(R.id.tv_book_stack_three_name);
            }
            return bookStackThree;
        }


    }


}
