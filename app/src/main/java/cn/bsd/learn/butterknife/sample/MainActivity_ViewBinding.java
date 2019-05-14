////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package cn.bsd.learn.butterknife.sample;
//
//import android.support.annotation.CallSuper;
//import android.support.annotation.UiThread;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//import butterknife.Unbinder;
//import butterknife.internal.DebouncingOnClickListener;
//import butterknife.internal.Utils;
//
//public class MainActivity_ViewBinding implements Unbinder {
//    private MainActivity target;
//    private View view7f070080;
//    private View view7f070081;
//
//    @UiThread
//    public MainActivity_ViewBinding(MainActivity target) {
//        this(target, target.getWindow().getDecorView());
//    }
//
//    @UiThread
//    public MainActivity_ViewBinding(final MainActivity target, View source) {
//        this.target = target;
//        View view = Utils.findRequiredView(source, 2131165312, "field 'tv1' and method 'click'");
//        target.tv1 = (TextView)Utils.castView(view, 2131165312, "field 'tv1'", TextView.class);
//        this.view7f070080 = view;
//        view.setOnClickListener(new DebouncingOnClickListener() {
//            public void doClick(View p0) {
//                target.click(p0);
//            }
//        });
//        view = Utils.findRequiredView(source, 2131165313, "field 'tv2' and method 'click2'");
//        target.tv2 = (TextView)Utils.castView(view, 2131165313, "field 'tv2'", TextView.class);
//        this.view7f070081 = view;
//        view.setOnClickListener(new DebouncingOnClickListener() {
//            public void doClick(View p0) {
//                target.click2();
//            }
//        });
//    }
//
//    @CallSuper
//    public void unbind() {
//        MainActivity target = this.target;
//        if (target == null) {
//            throw new IllegalStateException("Bindings already cleared.");
//        } else {
//            this.target = null;
//            target.tv1 = null;
//            target.tv2 = null;
//            this.view7f070080.setOnClickListener((OnClickListener)null);
//            this.view7f070080 = null;
//            this.view7f070081.setOnClickListener((OnClickListener)null);
//            this.view7f070081 = null;
//        }
//    }
//}
