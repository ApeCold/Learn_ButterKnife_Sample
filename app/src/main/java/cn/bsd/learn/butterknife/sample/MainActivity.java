package cn.bsd.learn.butterknife.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.bsd.learn.butterknife.annotation.BindView;
import cn.bsd.learn.butterknife.annotation.OnClick;
import cn.bsd.learn.butterknife.library.ButterKnife;

//ButterKnife加载一个临时创建的类
//找到了findViewById的实现
//找到了OnClick的实现
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv1)
    public void click(View view){
        Log.e("learn >>> ","Click >>>" + tv1.getText().toString());
    }

    @OnClick(R.id.tv2)
    public void click2(){
        Log.e("learn >>> ","OnClick -> no parameter");
    }

}
