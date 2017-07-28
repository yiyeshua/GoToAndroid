package com.pili.basemodel.manager;

import com.pili.basemodel.base.BaseFragment;

import java.util.Map;
import java.util.WeakHashMap;


/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:19:37
 */
public class FragmentManager {

    private static Map<String, BaseFragment> fragmentList = new WeakHashMap<>();

    /**
     * 根据Class创建Fragment
     *
     * @param clazz the Fragment of create
     * @return
     */
    public static BaseFragment createFragment(Class<?> clazz, boolean isObtain) {
        BaseFragment resultFragment = null;
        String className = clazz.getName();
        if (fragmentList.containsKey(className)) {
            resultFragment = fragmentList.get(className);
        } else {
            try {
                try {
                    resultFragment = (BaseFragment) Class.forName(className).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (isObtain)
                fragmentList.put(className, resultFragment);
        }

        return resultFragment;
    }

    public static BaseFragment createFragment(Class<?> clazz) {
        return createFragment(clazz, true);
    }




}
