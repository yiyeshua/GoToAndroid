package com.pili.gotome;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pili.basemodel.base.BaseActivity;
import com.pili.gotome.ui.diary.AddFragment;
import com.pili.gotome.ui.diary.DiaryFragment;
import com.pili.gotome.ui.home.HomeFragment;
import com.pili.gotome.ui.meizi.MeiziFragment;
import com.pili.gotome.ui.setting.SettingFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class Main2Activity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.rg)
    RadioGroup rg;
    private Fragment mFragment;
    private ArrayList<Fragment> fragments;
    private int position;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fragments=new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new DiaryFragment());
        fragments.add(new AddFragment());
        fragments.add(new MeiziFragment());
        fragments.add(new SettingFragment());

        rg.setOnCheckedChangeListener(this);
        ((RadioButton) rg.findViewById(R.id.home)).setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.home :
                position=0;
                break;
            case R.id.diary :
                position=1;
                break;
            case R.id.add :
                position=2;
                break;
            case R.id.meizi :
                position=3;
                break;
            case R.id.setting :
                position=4;
                break;
        }
       switchFragment(mFragment,fragments.get(position));
    }

    /**
     * 切换
     * @param fromFragment
     * @param toFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(mFragment!=null){
            mFragment=toFragment;
            if(!toFragment.isAdded()){
                ft.hide(fromFragment).add(R.id.content,toFragment).commit();
            }else{
                ft.hide(fromFragment).show(toFragment).commit();
            }

        }else{
            mFragment=toFragment;
            ft.add(R.id.content,mFragment).commit();
        }

    }
}
