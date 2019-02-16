package com.example.dell.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.myapplication.adapter.SearchAdapter;
import com.example.dell.myapplication.bean.Query;
import com.example.dell.myapplication.bean.User;
import com.example.dell.myapplication.dao.DaoMaster;
import com.example.dell.myapplication.dao.DaoSession;
import com.example.dell.myapplication.persenter.IPersentermpl;
import com.example.dell.myapplication.utils.NetWorkUtils;
import com.example.dell.myapplication.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements IView {
    String path="commodity/v1/findCommodityByKeyword?page=1&count=10&keyword=%s";
    private SearchAdapter searchAdapter;
    private IPersentermpl iPersentermpl;
    Unbinder bind;
    @BindView(R.id.find_edit)
    EditText editText;
    @BindView(R.id.xrecycler)
    XRecyclerView xRecyclerView;
    @BindView(R.id.glass)
    ImageView glass;
    private List<Query.ResultBean> result;
    private String name;
    private int Page;
    private DaoSession daoSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        
        Page=1;
        iPersentermpl = new IPersentermpl(this);
        searchAdapter = new SearchAdapter(this);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Page=1;
                init();
            }

            @Override
            public void onLoadMore() {
               init();
            }
        });
        xRecyclerView.setAdapter(searchAdapter);
        init();
        initGreenDao();
        searchAdapter.setOnClickListrner(new SearchAdapter.OnClickListrn() {
            @Override
            public void getdata(String id) {
                startActivity(new Intent(MainActivity.this,ShowActivity.class));
                EventBus.getDefault().postSticky(id);
            }
        });
    }
    private void init(){
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);

        iPersentermpl.getRequest(String.format(path,name),Query.class);
    }
    @OnClick(R.id.glass)
    public void search(){
        Page=1;
        name = editText.getText().toString();
        init();
    }
    @Override
    public void onSuccess(Object data) {
        Query bean= (Query) data;
        result = bean.getResult();
        if(NetWorkUtils.isNetWork(this)) {
            if(Page==1){
                searchAdapter.setList(result);
            }
            else {
                searchAdapter.addList(result);
            }
            Page++;
            xRecyclerView.loadMoreComplete();
            xRecyclerView.refreshComplete();
            for (int i=0;i<result.size();i++){
                String commodityName = result.get(i).getCommodityName();
                String masterPic = result.get(i).getMasterPic();
                User user = new User();
                user.setTitle(commodityName);
                user.setImg(masterPic);
                daoSession.insert(user);
            }
        }else{
            List<User> users = daoSession.loadAll(User.class);
            for (int i = 0; i <users.size() ; i++) {
                String title = users.get(i).getTitle();
                Query query = new Query();
                query.getResult().get(i).setCommodityName(title);
                List<Query.ResultBean> list=new ArrayList<>();
                list.addAll(query.getResult());
                searchAdapter.setList(list);
            }

        }
    }
    //数据库
    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "userbao.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
