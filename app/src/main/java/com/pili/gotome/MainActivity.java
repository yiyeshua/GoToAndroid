package com.pili.gotome;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.rg)
    RadioGroup rg;
    private Fragment mFragment;
    private ArrayList<Fragment> fragments;
    private int position;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments=new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new DiaryFragment());
        fragments.add(new AddFragment());
        fragments.add(new MeiziFragment());
        fragments.add(new SettingFragment());

       // rg= (RadioGroup) findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(this);
       // rg.check(R.id.home);
        ((RadioButton) rg.findViewById(R.id.home)).setChecked(true);
        Log.e("TAG", "aaaaaaaaaaaaaa");

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.home :
                position=0;
                Log.e("TAG", "000000000000000000000");
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
        Log.e("TAG", "111111111111111111111111111");
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
            Log.e("TAG", "3333333333333333333333");
            mFragment=toFragment;
            if(!toFragment.isAdded()){
                ft.hide(fromFragment).add(R.id.content,toFragment).commit();
            }else{
                ft.hide(fromFragment).show(toFragment).commit();
            }

        }else{
            Log.e("TAG", "222222222222222222222222222222");
            mFragment=toFragment;
            ft.add(R.id.content,mFragment).commit();
        }

    }
}
