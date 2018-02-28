package com.mahao.objectanimtor;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtView;
    private LinearLayout parentLay;
    private LayoutTransition transition;
    private ListView listView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        txtView = (TextView) findViewById(R.id.txt_animator);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_list_view).setOnClickListener(this);
        parentLay = (LinearLayout) findViewById(R.id.layout_change);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,new String[]{"第一个","第二个","第三个","第四个","第五个"});

        //自定义动画
        initTransAnim();
        //设置动画
        setTransAnim();

        //设置listview动画
        setAnimationView();
    }

    /**
     *  APPEARINgG : 当view出现或者添加的时候，出现动画
     *  DiSAPPEARING : 当view小时或者隐藏的时候，VIEW消失动画
     *  CHANGE_APPEARING : 当添加VIEW导致布局容器改变的时候，整个布局容器的动画
     *  CHANGE_DISAPPREARING : 当删除或者隐藏VIEW导致布局容器改变的时候，整个布局的动画
     */
    private void setTransAnim() {

        //1 view出现
        ObjectAnimator animator = ObjectAnimator.ofFloat(null,"rotationY",90f,0f);
        animator.setDuration(transition.getDuration(LayoutTransition.APPEARING));
        transition.setAnimator(LayoutTransition.APPEARING,animator);

        //2 view消失
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(null,"rotationX",0f,90f,0f);
        animator1.setDuration(transition.getDuration(LayoutTransition.DISAPPEARING));
        transition.setAnimator(LayoutTransition.DISAPPEARING,animator1);

        //设置动画改变时，子view动画的时间间隔
        transition.setStagger(LayoutTransition.CHANGE_APPEARING,30);
        transition.setStagger(LayoutTransition.CHANGE_DISAPPEARING,30);

        PropertyValuesHolder left = PropertyValuesHolder.ofInt("left",0,1);
        PropertyValuesHolder top = PropertyValuesHolder.ofInt("top",0,1);
        PropertyValuesHolder right = PropertyValuesHolder.ofFloat("bottom",0,1);
        PropertyValuesHolder bottom = PropertyValuesHolder.ofInt("right",0,1);

        //view出现 容器变化
        PropertyValuesHolder animator3 = PropertyValuesHolder.ofFloat("scaleX",1f,2f,1f);
        ObjectAnimator viewAnim = ObjectAnimator.ofPropertyValuesHolder(parentLay,left,top,right,bottom,animator3);
        viewAnim.setDuration(transition.getDuration(LayoutTransition.CHANGE_APPEARING));
        viewAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator)animation).getTarget();
                view.setScaleX(1f);
            }
        });
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING,viewAnim);

        //view消失，导致整个布局改变时的动画
        Keyframe f1 = Keyframe.ofFloat(0f,0f);
        Keyframe f2 = Keyframe.ofFloat(0.5f,2f);
        Keyframe f3 = Keyframe.ofFloat(1f,0f);
        PropertyValuesHolder animator4 = PropertyValuesHolder.ofKeyframe("scaleX",f1,f2,f3);
        ObjectAnimator disViewAnim = ObjectAnimator.ofPropertyValuesHolder(parentLay,left,top,right,bottom,animator4);
        disViewAnim.setDuration(transition.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        disViewAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator)animation).getTarget();
                view.setScaleX(1.0f);
            }
        });
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING,disViewAnim);
    }

    /**
     *  类似系统动画
     */
    private void initTransAnim() {
        transition = new LayoutTransition();
        parentLay.setLayoutTransition(transition);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_start:
                //keyFrameAnim();
                //viewPropertyAnimators();
                loadXmlAnimator();
                break;
            case R.id.btn_add:
                addBtnView();
                break;
            case R.id.btn_delete:
                removeView();
                break;
            case R.id.btn_list_view:
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     *  单帧设置动画。
     */
    public void keyFrameAnim(){
        //动画left从0平移到200f,再平移到开始的位置
        ObjectAnimator animator = ObjectAnimator.ofFloat(txtView,"translationX",0.0f,200f,0f);

        //第二种执行方式  第一个参数是动画的完成度值[0-1]
        Keyframe keyframe1 = Keyframe.ofFloat(0.0f,0);
        Keyframe keyframe2 = Keyframe.ofFloat(0.5f,500);
        Keyframe keyframe3 = Keyframe.ofFloat(1f,0);
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofKeyframe("translationX",keyframe1,keyframe2,keyframe3);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(txtView,valuesHolder);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(2);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();
    }


    /**
     *  同时执行多个属性的动画--------animatorset的简化版
     */
    public void viewPropertyAnimators(){

        ViewPropertyAnimator animator = txtView.animate();
        animator.alpha(0.5f).scaleX(1.5f).translationX(700f).setDuration(3000)
                .start();
    }

    /**
     *  加载属性动画
     */
    public void  loadXmlAnimator(){
 
        Animator animator = AnimatorInflater.loadAnimator(this,R.animator.object_anim_set);
        animator.setTarget(txtView);
        animator.start();
    }

    /**
     *   android 默认添加view的动画
     */
    public void addBtnView(){
        Button btnAdd = new Button(this);
        btnAdd.setText("第"+parentLay.getChildCount()+"个");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        btnAdd.setLayoutParams(params);
        if(parentLay.getChildCount() > 2){
            parentLay.addView(btnAdd,1);
        }else {
            parentLay.addView(btnAdd);
        }
    }

    /**
     *  android 默认删除动画
     */
    public void removeView(){
        if(parentLay.getChildCount() > 0){
            parentLay.removeViewAt(0);
        }
    }

    /**
     *  listview视图动画
     */
    public void setAnimationView(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.left);
        LayoutAnimationController controller = new LayoutAnimationController(animation,0.5f);
        controller.setInterpolator(new BounceInterpolator());
        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
        listView.setLayoutAnimation(controller);
    }
}
