package com.mahao.objectanimtor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_one).setOnClickListener(this);
        findViewById(R.id.btn_two).setOnClickListener(this);
        txtView = (TextView) findViewById(R.id.txt_hello);
    }

    public void startAnimtion(final View view){

        //视图动画。
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.abc_fade_in);

        //1 属性动画
        //view.setTranslationX(100);
        // view.setScaleX(1.5f);
        // view.setAlpha(0.5f);
        // view.setRotation();
        // view.setbackgroundColor();


        //只要view里面有setXX()方法就可以通过反射达到变化的目的
        ObjectAnimator aa = ObjectAnimator.ofFloat(view,"translationX",0f,200f);
        ObjectAnimator oa = ObjectAnimator.ofInt(view,"backgroundColor", Color.RED,Color.BLUE);

        //多个动画同时执行
        ObjectAnimator animator = ObjectAnimator.ofFloat(txtView,"hehe",0f,100f);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        final ObjectAnimator animator1 = ObjectAnimator.ofFloat(txtView,"translationX",0,point.x);
        animator1.setDuration(3000);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                //监听动画回调
                float ratio = animation.getAnimatedFraction();//获取动画执行的百分比 0-1；

                float value = (float) animation.getAnimatedValue(); //获得0f-100f当中的这个时间点对应的值。

                txtView.setScaleX(0.5f+value/point.x);
                txtView.setScaleY(0.5f + value/point.x);
                txtView.setTranslationX(value);
            }
        });
        //动画插值器
       animator1.setInterpolator(new BounceInterpolator());
        //重复次数
       animator1.setRepeatCount(ValueAnimator.INFINITE);
        //重复模式
       animator1.setRepeatMode(ValueAnimator.REVERSE);
       animator1.setStartDelay(2000);
       animator1.start();

       animator1.addListener(new Animator.AnimatorListener() {
           @Override
           public void onAnimationStart(Animator animation) {
           }

           @Override
           public void onAnimationEnd(Animator animation) {
             //  animator1.setRepeatCount(ValueAnimator.INFINITE);
           }

           @Override
           public void onAnimationCancel(Animator animation) {
           }

           @Override
           public void onAnimationRepeat(Animator animation) {
           }
       });

       animator1.addListener(new AnimatorListenerAdapter() {
           @Override
           public void onAnimationEnd(Animator animation) {
               super.onAnimationEnd(animation);
           }
       });


       //方法2
        ValueAnimator animator2 = ValueAnimator.ofFloat(0f,200f);
        animator2.setDuration(200);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                //获取动画运动中的值
                Float animatedValue = (Float) animation.getAnimatedValue();
             //   view.setX(animatedValue);
            }
        });
        animator2.start();

        //方法三  多个属性。
        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("alpha",1f,0.5f);
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleX",0f,1f);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleY",1f,0.5f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(txtView,holder,holder1,holder2);
        animator3.setDuration(300);
        animator3.start();


        //方法四  动画集合
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(txtView,"translationX",0f,point.x);
        animator4.setRepeatCount(3);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(txtView,"alpha",0f,1f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(txtView,"scaleX",0f,2f);
        //属性动画集合
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.play(animator4).with(animator5).after(animator6);
     //   set.play(animator4).with(animator5).before(animator6);
     //   set.playTogether(animator4,animator5,animator6);
     //   set.playSequentially(animator4,animator5,animator6);
        set.start();
    }

    /**
     *  objectAnimator 必须设置setAnimtor()方法 动画才生效。
     * @param args
     */
    public void setBackgroundColor(float args){

        txtView.setBackgroundColor((int)args);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_one:
                ObjectAnimator oa = ObjectAnimator.ofInt(v,"backgroundColor", Color.RED,Color.GRAY);
                oa.setDuration(2000);
                oa.setRepeatMode(ObjectAnimator.RESTART);
                oa.start();
                startAnimtion(txtView);
                break;
            case R.id.btn_two:
                Intent intent = new Intent(this,AnimatorActivity.class);
                startActivity(intent);
                break;
        }
    }
}
