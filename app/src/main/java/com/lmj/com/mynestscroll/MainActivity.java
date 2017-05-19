package com.lmj.com.mynestscroll;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lmj.com.mynestscroll.view.MyNestedScrollParent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private List<String> mDatas;
    private HomeAdapter mAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] titles = {"天猫双11","淘宝双12","剁手双双双"};
    private MyNestedScrollParent myNestedScrollParent;
    private ArrayList<RecyclerView> listViews ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        myNestedScrollParent=(MyNestedScrollParent)   findViewById(R.id.myNestedScrollParent);
//        lv  = (RecyclerView) findViewById(R.id.listView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tab);
        initData();
        init();
    }
    public void init(){
        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        viewPager.setAdapter(new MyPageAdapter());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//int aa=11;
                viewPager.setCurrentItem(tab.getPosition());

                myNestedScrollParent. setCurrentContentView(listViews.get(tab.getPosition()));
//                currentContentView = (ViewGroup) viewPager.getChildAt(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    protected void initData()
    {
        listViews = new ArrayList<RecyclerView>();
        for(int i=0;i<3;i++){
            View view =     LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main2,null);
            RecyclerView     listView=  ((RecyclerView)view.findViewById(R.id.listView));
            listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            listView.setAdapter(mAdapter = new HomeAdapter());
            listView.setAdapter(new HomeAdapter());
            listViews.add(listView);
        }
        mDatas = new ArrayList<String>();
        for (int i = 0; i <50; i++)
        {
            mDatas.add("" + i);
        }
    }
    class MyPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(  listViews.get(position));

            return listViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
          container.removeView((View) object);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv);
            }
        }
    }

}
