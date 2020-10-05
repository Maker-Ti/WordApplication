package com.example.maker.wordapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maker.wordapplication.adapter.dataBaseAdapterOpenHelper;
import com.example.maker.wordapplication.adapter.wordListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yanzhikai.textpath.AsyncTextPathView;

public class MainActivity extends Activity {
    private dataBaseAdapterOpenHelper openHelper;
    private SQLiteDatabase sqLiteDatabase;
    @BindView(R.id.icon_layout)
    RelativeLayout icon_layout;
    @BindView(R.id.control_lable_icon)
    TextView control_lable_icon;
    @BindView(R.id.layout_lable)
    RelativeLayout layout_lable;
    private int lable_width;
    @BindView(R.id.layout_icons)
    LinearLayout layout_icons;
    private boolean lableControl = false;
    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.ed_word)
    EditText ed_word;
    @BindView(R.id.ed_wordmean)
    EditText ed_wordmean;
    @BindView(R.id.lin_button)
    LinearLayout lin_button;
    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.btn_cancle)
    Button btn_cancle;
    private int AlphaAnimTime = 200;
    private float moveX=0,moveY = 0;
    @BindView(R.id.tv_times)
    TextView tv_times;
    private List<Map<String,String>> allWords;
    private int index = 0;
    @BindView(R.id.txt_word)
    AsyncTextPathView txt_word;
    @BindView(R.id.txt_wordMean)
    TextView txt_wordMean;
    @BindView(R.id.tv_line)
    TextView tv_line;
    @BindView(R.id.re_word_content)
    RelativeLayout re_word_content;
    @BindView(R.id.re_mean_content)
    RelativeLayout re_mean_content;
    @BindView(R.id.main_content)
    LinearLayout main_content;
    @BindView(R.id.button_re)
    RelativeLayout button_re;
    @BindView(R.id.re_delete_check)
    RelativeLayout re_delete_check;
    @BindView(R.id.btn_delete_cancle)
    Button btn_delete_cancle;
    @BindView(R.id.btn_delete_sure)
    Button btn_delete_sure;
    @BindView(R.id.txt_seekbar)
    TextView txt_seekbar;
    @BindView(R.id.re_seekbar)
    RelativeLayout re_seekbar;
    @BindView(R.id.re_info_dialog)
    RelativeLayout re_info_dialog;
    @BindView(R.id.tv_close)
    TextView tv_close;
    @BindView(R.id.list_word)
    RelativeLayout list_word_dialog;
    @BindView(R.id.dialog_close)
    RelativeLayout dialog_close;
    @BindView(R.id.show_delete)
    TextView show_delete;
    @BindView(R.id.word_list)
    ListView word_list;
    @BindView(R.id.tv_word_control)
    TextView tv_word_control;
    @BindView(R.id.tv_wordmean_control)
    TextView tv_wordmean_control;
    private boolean wordShowControl = true;
    private boolean wordMeanShowControl = true;
    private boolean listDialogControllFlag = false;

    private int deleteCheckHeight = 0;
    private float startX=0,startY=0;
    private boolean touchDown = false;
    private boolean touchFlag = false;
    private float lastpositionX,lastpositionY;
    private boolean wordMode = true;
    private float infoDialogWidth = 0,infoDIalogHeight = 0;
    private SharedPreferences sharedPreferences;
    private wordListAdapter wordListAdapter;
    private boolean listDeleteIsOpen = false;
    private List<String> seekBarDataList = new ArrayList<>();
    private ObjectAnimator transSeekBarShow;
    private ObjectAnimator transSeekBarHiden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("wordShare",MODE_PRIVATE);
        index = sharedPreferences.getInt("index",0);
        initTextPathView();
        initDataBase();
        init();
        loadDeleteCheck();
        loadTouchLinster();
        initListView(listDeleteIsOpen);
        initWordListClick(true);
        initLeftViewCotrol();


    }
    private void initSeekBarAnim(){
        transSeekBarShow =
                ObjectAnimator.ofFloat(re_seekbar,"translationY",0,deleteCheckHeight*-1).setDuration(500);
        transSeekBarHiden =
                ObjectAnimator.ofFloat(re_seekbar,"translationY",deleteCheckHeight*-1,0).setDuration(500);
        transSeekBarHiden.setStartDelay(700);
    }
    private void initLeftViewCotrol() {
        tv_word_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wordShowControl){
                    wordShowControl = false;
                    re_word_content.setVisibility(View.GONE);
                    tv_word_control.setBackgroundResource(R.drawable.left_control_top);
                    if(wordMeanShowControl == false){
                        wordMeanShowControl = true;
                        re_mean_content.setVisibility(View.VISIBLE);
                        tv_wordmean_control.setBackgroundResource(R.drawable.left_control_buttom_white);
                    }
                }else {
                    wordShowControl = true;
                    re_word_content.setVisibility(View.VISIBLE);
                    tv_word_control.setBackgroundResource(R.drawable.left_control_top_white);
                }
            }
        });

        tv_wordmean_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wordMeanShowControl){
                    wordMeanShowControl = false;
                    re_mean_content.setVisibility(View.GONE);
                    tv_wordmean_control.setBackgroundResource(R.drawable.left_control_buttom);
                  if(wordShowControl == false){
                      wordShowControl = true;
                      re_word_content.setVisibility(View.VISIBLE);
                      tv_word_control.setBackgroundResource(R.drawable.left_control_top_white);
                  }

                }else {
                    wordMeanShowControl = true;
                    re_mean_content.setVisibility(View.VISIBLE);
                    tv_wordmean_control.setBackgroundResource(R.drawable.left_control_buttom_white);
                }
            }
        });
    }

    private void listDialogControll(){
        if(listDialogControllFlag){

            listDialogControllFlag = false;
            ObjectAnimator dialogDismiss = ObjectAnimator.ofFloat(list_word_dialog,"alpha",1,0).setDuration(300);
            dialogDismiss.start();
            dialogDismiss.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    list_word_dialog.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }else {
            list_word_dialog.setVisibility(View.VISIBLE);
            listDialogControllFlag = true;
            ObjectAnimator dialogShow = ObjectAnimator.ofFloat(list_word_dialog,"alpha",0,1).setDuration(300);
            dialogShow.start();

        }
    }

    private void initWordListClick(boolean flag) {
        if(flag){
            word_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startSeekBar(allWords.get(i).get("word"));
                    listDialogControll();
                    index = i;
                    changeWordContent(index);
                }
            });
            word_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ObjectAnimator transDeleteCheck = ObjectAnimator.ofFloat(re_delete_check,"translationY",0,(deleteCheckHeight*-1)).setDuration(300);
                    transDeleteCheck.start();
                    index = i;
                    initListView(false);
                    return true;
                }
            });
        }else {
            word_list.setOnItemLongClickListener(null);
            word_list.setOnLongClickListener(null);
        }
    }


    private void initListView(boolean flag) {
      //  word_list.setDividerHeight(10);
       // word_list.setDivider(R.color.mainTextColor);
        wordListAdapter = new wordListAdapter(allWords,MainActivity.this,index,flag);
        wordListAdapter.setOnClickMyTextView(new wordListAdapter.onClickMyTextView() {
            @Override
            public void myTextViewClick(int id) {
               deleteWord(allWords.get(id).get("word"));
            }
        });
        word_list.setAdapter(wordListAdapter);
        word_list.setSelection(index);
        show_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_delete.getText().toString().equals("批量删除")){
                    show_delete.setText("取消");
                    initListView(true);
                    initWordListClick(false);
                    listDeleteIsOpen = true;
                }else if(show_delete.getText().toString().equals("取消")){
                    show_delete.setText("批量删除");
                    initListView(false);
                    initWordListClick(true);
                    listDeleteIsOpen = false;
                }
            }
        });

    }

    @OnClick({R.id.tv_info,R.id.tv_random,R.id.tv_order,R.id.tv_upload,R.id.tv_download,R.id.tv_list})
    public void iconsClickListener(View view){
        switch (view.getId()){
            case R.id.tv_info:
                re_info_dialog.setVisibility(View.VISIBLE);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(re_info_dialog,"scaleX",0.05f,1f).setDuration(500);
                ObjectAnimator rotationIcon = ObjectAnimator.ofFloat(tv_close,"rotation",0,180).setDuration(300);
                ObjectAnimator alphaIcon = ObjectAnimator.ofFloat(tv_close,"alpha",0,1).setDuration(300);
                scaleX.start();
                rotationIcon.setStartDelay(200);
                alphaIcon.setDuration(200);
                rotationIcon.start();
                alphaIcon.start();

                tv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(re_info_dialog,"scaleX",1f,0.0f).setDuration(700);
                        ObjectAnimator rotationIcon = ObjectAnimator.ofFloat(tv_close,"rotation",180,0).setDuration(500);
                        ObjectAnimator alphaIcon = ObjectAnimator.ofFloat(tv_close,"alpha",1,0).setDuration(700);
                        scaleX.start();
                        scaleX.setInterpolator(new AnticipateInterpolator());
                        rotationIcon.start();
                        rotationIcon.setInterpolator(new AnticipateInterpolator());
                        alphaIcon.start();
                        scaleX.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                re_info_dialog.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                    }
                });
                break;
            case R.id.tv_random:
                startSeekBar("随机查看");
                wordMode = false;
                break;
            case R.id.tv_order:
                startSeekBar("添加顺序");
                wordMode = true;
                index = 0;
                changeWordContent(index);
                break;
            case R.id.tv_upload:
                startSeekBar("本地单词数量："+allWords.size()+"个");
            break;
            case R.id.tv_download:startSeekBar("还没写");break;
            case R.id.tv_list:
                lableControl();
                startSeekBar("所有单词");
                initListView(false);
                listDialogControll();
                dialog_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listDialogControll();
                        startSeekBar("关闭");
                        show_delete.setText("批量删除");
                        initWordListClick(true);
                    }
                });

            break;
        }
    }
    private void startSeekBar(String str){
        seekBarDataList.add(str);
            seekBarControl(str);
    }
    private void seekBarControl(String str){
            txt_seekbar.setText(str);
            transSeekBarShow.start();
            transSeekBarShow.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }
                @Override
                public void onAnimationEnd(Animator animator) {
                          transSeekBarHiden.start();
                }
                @Override
                public void onAnimationCancel(Animator animator) {

                }
                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }





    private void loadDeleteCheck() {
        txt_word.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Toast.makeText(MainActivity.this, "longclick", Toast.LENGTH_SHORT).show();
                ObjectAnimator transDeleteCheck = ObjectAnimator.ofFloat(re_delete_check,"translationY",0,(deleteCheckHeight*-1)).setDuration(300);
                transDeleteCheck.start();
                return true;
            }
        });
        btn_delete_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSeekBar("取消");
                ObjectAnimator transDeleteCheck = ObjectAnimator.ofFloat(re_delete_check,"translationY",(deleteCheckHeight*-1),0).setDuration(300);
                transDeleteCheck.start();
            }
        });
        btn_delete_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWord(allWords.get(index).get("word"));
            }
        });
    }
    private void deleteWord(String word){
        String str;
        boolean flag = openHelper.deleteWord( word,sqLiteDatabase);
        if(flag){

            str = "删除成功";
          if(listDeleteIsOpen == false){
              ObjectAnimator transDeleteCheck = ObjectAnimator.ofFloat(re_delete_check,"translationY",(deleteCheckHeight*-1),0).setDuration(300);
              transDeleteCheck.start();
          }
            init();
            initListView(listDeleteIsOpen);
        }else {
            str = "删除失败";
        }
        startSeekBar(str);

    }
    private void initTextPathView() {
        txt_word.showFillColorText();
        Typeface typeface = Typeface.SANS_SERIF;
        txt_word.setTypeface(typeface);
    }

    private void loadTouchLinster() {

        button_re.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float positionX = motionEvent.getX();
                float positionY = motionEvent.getY();
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    Log.e("txhLog",""+lastpositionY);
                    moveX = startX-positionX;
                    moveY = startY - positionY;
                    Log.e("txhPosition","x:"+moveX+",y:"+moveY+touchFlag);
                        RelativeLayout.LayoutParams layoutParamsWord =
                                (RelativeLayout.LayoutParams) main_content.getLayoutParams();
                        layoutParamsWord.setMargins(0, (int) (moveY*-1),0,0);
                    main_content.setLayoutParams(layoutParamsWord);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    startX = positionX;
                    startY = positionY;

                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    float endY = motionEvent.getY();
                    float endX = motionEvent.getX();
                    Log.e("txhLogY",""+moveY);
                    RelativeLayout.LayoutParams layoutParamsWord =
                            (RelativeLayout.LayoutParams) main_content.getLayoutParams();
                    layoutParamsWord.setMargins(0, (int) 0,0,0);
                    main_content.setLayoutParams(layoutParamsWord);
                    float distence = endY-startY;
                    float isOpen = endX - startX;
                    if (isOpen>300&&lableControl == true){
                        lableControl();
                    }else if(isOpen<-300&&lableControl == false){
                        lableControl();
                    }
                   if(wordMode){
                       if(distence>200){
                           //up last
                           index = index-1;
                           changeWordContent(index);
                       }else if(distence<-200){
                           index++;
                           changeWordContent(index);
                           //down next
                       }
                   }else {
                       int radom = (int) (Math.random()*allWords.size());
                       index = radom;
                       changeWordContent(index);
                   }
                    moveY = 0;
                }
                return true;

            }
        });
    }

    @OnClick(R.id.btn_cancle)
    public void cancleClick(){
        addViewColtrol(false);
        changeWordContent(index);
        lineRestar();
        startSeekBar("取消添加");
    }
    private void lineRestar(){
        ObjectAnimator lineScanleX = ObjectAnimator.ofFloat(tv_line,"scaleX",0,1f).setDuration(600);
        lineScanleX.start();
        ObjectAnimator scaleMean = ObjectAnimator.ofFloat(txt_wordMean,"alpha",0,1).setDuration(600);
        scaleMean.start();
        ed_word.setText("");
        ed_wordmean.setText("");
    }

    private void addWords(String word,String wordMean){
        boolean flag=openHelper.getWordExist(word,sqLiteDatabase);
        boolean insert;
        if(flag==false){
            Log.e("sqlLog","insert");
            insert = openHelper.insertWord(word,wordMean,sqLiteDatabase);
            startSeekBar("添加成功");
            Log.e("sqlLog","插入："+insert);
        }else {
            insert = openHelper.updateWordTimes(word,sqLiteDatabase);
            startSeekBar("单词存在，添加次数");
            Log.e("sqlLog","更新"+insert);
        }
        init();
        Log.e("txhLog",""+flag);
    }

    @OnClick(R.id.btn_add)
    public void addClick(){
       addWords(ed_word.getText().toString(),ed_wordmean.getText().toString());
      //  addViewColtrol(false);

        lineRestar();
    }
    private void changeTimes(String times){
        tv_times.setText(times);
    }
    @OnClick(R.id.tv_add)
    public void addIconClick(){
        addViewColtrol(true);
        lableControl();
        changeTimes("");

    }

    private void addViewColtrol(boolean flag) {

        ObjectAnimator wordAnim;
        ObjectAnimator wordMeanAnim;
        ObjectAnimator linAnim;
        if(flag){
            ed_word.setVisibility(View.VISIBLE);
            ed_wordmean.setVisibility(View.VISIBLE);
            lin_button.setVisibility(View.VISIBLE);
            wordAnim = ObjectAnimator.ofFloat(ed_word,"alpha",0,1).setDuration(AlphaAnimTime);
            wordMeanAnim = ObjectAnimator.ofFloat(ed_wordmean,"alpha",0,1).setDuration(AlphaAnimTime);
            linAnim = ObjectAnimator.ofFloat(lin_button,"alpha",0,1).setDuration(AlphaAnimTime);
            wordAnim.start();
            wordMeanAnim.start();
            linAnim.start();
            ed_word.requestFocus();
        }else {

            wordAnim = ObjectAnimator.ofFloat(ed_word,"alpha",1,0).setDuration(AlphaAnimTime);
            wordMeanAnim = ObjectAnimator.ofFloat(ed_wordmean,"alpha",1,0).setDuration(AlphaAnimTime);
            linAnim = ObjectAnimator.ofFloat(lin_button,"alpha",1,0).setDuration(AlphaAnimTime);
            wordAnim.start();
            wordMeanAnim.start();
            linAnim.start();
            wordAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    ed_word.setVisibility(View.GONE);
                    ed_wordmean.setVisibility(View.GONE);
                    lin_button.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }


    }

    @OnClick(R.id.icon_layout)
    public void lableIconClick(){
       lableControl();
    }
    private void lableControl(){
        ObjectAnimator controlAnim;
        ObjectAnimator iconRotate;
        if (lableControl){
            iconRotate =
                    ObjectAnimator.ofFloat
                            (control_lable_icon,"rotation",180,0);
            controlAnim =
                    ObjectAnimator.ofFloat
                            (layout_lable,"translationX",lable_width*-1,0);
            lableControl = false;
        }else {
            iconRotate =
                    ObjectAnimator.ofFloat
                            (control_lable_icon,"rotation",0,180);
            controlAnim =
                    ObjectAnimator.ofFloat
                            (layout_lable,"translationX",0,lable_width*-1);
            lableControl = true;
        }
        iconRotate.start();
        controlAnim.start();
    }


    private void initDataBase() {
        openHelper = new dataBaseAdapterOpenHelper(MainActivity.this,"wordDB",null,1);
        sqLiteDatabase = openHelper.getReadableDatabase();

    }
    @SuppressLint("WrongConstant")
    private void changeWordContent(int index){

        Log.e("txhLogIndex",""+index+"max:"+allWords.size());
        if(index==-1){
            this.index++;
            startSeekBar("已经是第一个了");
        }else if(index == allWords.size()){
           this.index--;
            startSeekBar("已经是最后一个了");
        }else if(-1<index&&index<(allWords.size())){
            Map<String,String> map = allWords.get(index);
            lineRestar();
            txt_word.setText(map.get("word"));
            txt_word.startAnimation(0,1);
            txt_wordMean.setText(map.get("wordMean"));
            tv_times.setText((map.get("times")));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("index",index);
            editor.commit();
        }else {
            Toast.makeText(this, "inedx输入错误，"+index,500).show();
        }

    }

    private void init() {
        allWords = openHelper.getWordList(sqLiteDatabase);
        Log.e("txhList",""+allWords);
        if(allWords.size()>0){
            changeWordContent(index);
        }
         /* btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag=openHelper.getWordExist("apple",sqLiteDatabase);
                boolean insert;
                if(flag==false){
                    Log.e("sqlLog","insert");
                    insert = openHelper.insertWord("apple","苹果",sqLiteDatabase);
                    Log.e("sqlLog","插入："+insert);
                }else {
                    insert = openHelper.updateWordTimes("apple",sqLiteDatabase);
                    Log.e("sqlLog","更新"+insert);
                }
                Log.e("txhLog",""+flag);
            }
        });
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Map<String,String>> wordList = openHelper.getWordList(sqLiteDatabase);
                Log.e("txhList",""+wordList);

            }
        });*/
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        lable_width = layout_icons.getWidth();
        deleteCheckHeight = re_delete_check.getHeight();
        infoDIalogHeight = re_info_dialog.getHeight();
        infoDialogWidth = re_info_dialog.getWidth();
        Log.e("txhWidth",""+lable_width);
        initSeekBarAnim();
    }
}
