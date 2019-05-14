package cn.bsd.learn.butterknife.library;

import android.app.Activity;

public class ButterKnife {
    public static void bind(Activity activity) {
        //MainActivity$ViewBinder
        String className = activity.getClass().getName() + "$ViewBinder";

        try {
            Class<?> viewBindClass = Class.forName(className);
            //获取实例化对象
            ViewBinder viewBinder = (ViewBinder) viewBindClass.newInstance();
            viewBinder.bind(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
