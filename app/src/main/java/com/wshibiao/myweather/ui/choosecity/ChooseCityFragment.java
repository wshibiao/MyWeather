package com.wshibiao.myweather.ui.choosecity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wshibiao.myweather.R;
import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.base.BaseFragment;
import com.wshibiao.myweather.base.RxBus;
import com.wshibiao.myweather.customview.CityEditText;
import com.wshibiao.myweather.data.bean.City;
import com.wshibiao.myweather.ui.weatherdetail.WeatherDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wsb on 2016/4/26.
 */
public class ChooseCityFragment extends BaseFragment implements ChooseCityContract.View {
    //    @Bind(R.id.edit_city)
    private CityEditText editCity;
    //    @Bind(R.id.gv_remen)
//    MyGridView mGridView;
    @Bind(R.id.hot_view)
    LinearLayout hotView;
    private RecyclerView cityRecyclerView;

    private RelativeLayout first;
    private LinearLayout second;
    private RecyclerView hotCityRecyclerView;
    private List<City> cityList;
    private List<String> hotCity;
    private CityRecyclerViewAdapter mAdapter;
    private SharedPreferences mPrefs;
    //    private HotCityAdapter hotCityAdapter;
    private ChooseCityContract.Presenter chooseCityPresenter;
    private RxBus _rxBus;
    private FragmentManager fm;
    private CompositeSubscription subscription;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=(Activity)context;
    }
    @Override
    public void setPresenter(ChooseCityContract.Presenter presenter) {
        this.chooseCityPresenter = presenter;
    }


    public static ChooseCityFragment newInstance() {
        return new ChooseCityFragment();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _rxBus = ((BaseActivity)mActivity).getRxBusSingleton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.choose_city_fragment, container, false);
        final GridView mGridView=(GridView)view. findViewById(R.id.gv_remen);
        editCity=(CityEditText) view.findViewById(R.id.edit_city);
        cityRecyclerView=(RecyclerView) view.findViewById(R.id.city_list);
        hotCity = new ArrayList<>();
        initData();
        MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter(mActivity, hotCity);
        mGridView.setAdapter(myGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (_rxBus.hasObservers()) {
                    _rxBus.send(new CityEvent(hotCity.get(position)));
                }
                mActivity.startActivity(new Intent(mActivity, WeatherDetailActivity.class));
                mActivity.finish();
            }
        });
        chooseCityPresenter.searchCity();
        listenToSearchInput();
        cityList = new ArrayList<>();
        cityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cityRecyclerView.setHasFixedSize(true);
        mAdapter = new CityRecyclerViewAdapter(mActivity, cityList);
        cityRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CityRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                City city = cityList.get(position);
                try{
                    if (_rxBus.hasObservers()) {
                        _rxBus.send(new CityEvent(city.getDistrict()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                mActivity.startActivity(new Intent(mActivity, WeatherDetailActivity.class));
                mActivity.finish();


            }
        });
        ButterKnife.bind(this, view);
        return view;
    }



    @Override
    public void showCity(List<City> cities) {
        cityList.clear();
        cityList.addAll(cities);
        mAdapter.notifyDataSetChanged();
//        for (City city : cities) {
//            cityList.add(city);
//        }
    }

    @Override
    public void showNoCity() {
        Snackbar.make(editCity, "没有该城市天气数据", Snackbar.LENGTH_SHORT).show();
    }


    public void listenToSearchInput() {
        editCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                chooseCityPresenter.onEditChanged(s.toString());
                hotView.setVisibility(View.GONE);
            }
        });
    }


    private class MyGridViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<String > list;
        @Override
        public int getCount() {
            return hotCity.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public MyGridViewAdapter(Context ct, List<String> list) {
            this.list=list;
            inflater = LayoutInflater.from(ct);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_hot_city, parent,false);
                holder.id_tv_cityname = (TextView) convertView.findViewById(R.id.hot_city);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.id_tv_cityname.setText(list.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView id_tv_cityname;
        }
    }


    public void initData() {
        hotCity.add("北京");
        hotCity.add("上海");
        hotCity.add("广州");
        hotCity.add("深圳");
        hotCity.add("南京");
        hotCity.add("杭州");
        hotCity.add("西安");
        hotCity.add("海口");
        hotCity.add("三亚");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        chooseCityPresenter.searchCity();
    }

    public static class CityEvent {
        public String msg;
        public CityEvent(String msg) {
            this.msg=msg;
        }
    }
}
