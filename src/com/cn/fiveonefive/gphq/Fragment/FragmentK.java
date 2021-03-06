package com.cn.fiveonefive.gphq.Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.KBean;
import com.cn.fiveonefive.gphq.dto.RKBean;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.cn.fiveonefive.gphq.view.KChartsView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 2016/9/4.
 */
public class FragmentK extends Fragment implements KChartsView.KChange{
    String code;
    public FragmentK(String code,IKChange ikChange){
        this.code=code;
        this.ikChange=ikChange;
    }
    RadioGroup radioGroup;
    RadioButton riBtn,zhouBtn,yueBtn;
    KChartsView riView,zhouView,yueView;
    ImageView left,right,up,down;
    TextView chooseSpin;

    private List<KBean> riList;
    private List<KBean> riList2;
    private GetRiTask getRiTask;

    private List<KBean> zhouList;
    private List<KBean> zhouList2;
    private GetZhouTask getZhouTask;

    private List<KBean> yueList;
    private List<KBean> yueList2;
    private GetYueTask getYueTask;

    public boolean isDo=false;
    IKChange ikChange;
    int graytu;


    private PopupWindow popupWindow;

    TextView time,today,high,low,yest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graytu=getResources().getColor(R.color.grayfortu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.k_freg,null);
        finView(view);
        setListener();

        riList=new ArrayList<KBean>();
        zhouList=new ArrayList<KBean>();
        yueList=new ArrayList<KBean>();
        riList2=new ArrayList<KBean>();
        zhouList2=new ArrayList<KBean>();
        yueList2=new ArrayList<KBean>();
        return view;
    }

    @Override
    public void onResume() {
        riBtn.setChecked(true);
        super.onResume();
    }
    public void start(){
        if (riBtn.isChecked()){
            getRiTask=new GetRiTask();
            MyApplication.taskMap.put("ri",getRiTask);
        }else if (zhouBtn.isChecked()){
            getZhouTask=new GetZhouTask();
            MyApplication.taskMap.put("zhou",getZhouTask);
        }else if(yueBtn.isChecked()){
            getYueTask=new GetYueTask();
            MyApplication.taskMap.put("yue",getYueTask);
        }
    }
    public void stop(){
        if (getRiTask!=null){
            getRiTask.cancel(true);
            MyApplication.taskMap.remove("ri");
        }
        if (getZhouTask!=null){
            getZhouTask.cancel(true);
            MyApplication.taskMap.remove("zhou");
        }
        if (getYueTask!=null){
            getYueTask.cancel(true);
            MyApplication.taskMap.remove("yue");
        }
    }

    @Override
    public void onStop() {
        if (getRiTask!=null){
            getRiTask.cancel(true);
            MyApplication.taskMap.remove("ri");
        }
        if (getZhouTask!=null){
            getZhouTask.cancel(true);
            MyApplication.taskMap.remove("zhou");
        }
        if (getYueTask!=null){
            getYueTask.cancel(true);
            MyApplication.taskMap.remove("yue");
        }
        riBtn.setChecked(false);
        zhouBtn.setChecked(false);
        yueBtn.setChecked(false);
        super.onStop();
    }

    private void setListener() {
        chooseSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registering popup with OnMenuItemClickListener
                showPopupWindow(v);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                time.setText("时间");
                today.setText("今开");
                high.setText("最高");
                low.setText("最低");
                yest.setText("昨收");
                if (checkedId==riBtn.getId()){
                    riView.setVisibility(View.VISIBLE);
                    zhouView.setVisibility(View.GONE);
                    yueView.setVisibility(View.GONE);

                    getRiTask=new GetRiTask();
                    getRiTask.execute("");

                    if (getZhouTask!=null){
                        getZhouTask.cancel(true);
                        MyApplication.taskMap.remove("zhou");
                    }

                    if (getYueTask!=null){
                        getYueTask.cancel(true);
                        MyApplication.taskMap.remove("yue");
                    }

                }else if(checkedId==zhouBtn.getId()){
                    riView.setVisibility(View.GONE);
                    zhouView.setVisibility(View.VISIBLE);
                    yueView.setVisibility(View.GONE);

                    if (getRiTask!=null){
                        getRiTask.cancel(true);
                        MyApplication.taskMap.remove("ri");
                    }

                    getZhouTask=new GetZhouTask();
                    getZhouTask.execute("");

                    if (getYueTask!=null){
                        getYueTask.cancel(true);
                        MyApplication.taskMap.remove("yue");
                    }

                }else if (checkedId==yueBtn.getId()){
                    riView.setVisibility(View.GONE);
                    zhouView.setVisibility(View.GONE);
                    yueView.setVisibility(View.VISIBLE);

                    if (getRiTask!=null){
                        getRiTask.cancel(true);
                        MyApplication.taskMap.remove("ri");
                    }

                    if (getZhouTask!=null){
                        getZhouTask.cancel(true);
                        MyApplication.taskMap.remove("zhou");
                    }

                    getYueTask=new GetYueTask();
                    getYueTask.execute("");
                }
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=radioGroup.getCheckedRadioButtonId();
                if (id==riBtn.getId()){
                    riView.left();
                }else if (id==zhouBtn.getId()){
                    zhouView.left();
                }else if (id==yueBtn.getId()){
                    yueView.left();
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=radioGroup.getCheckedRadioButtonId();
                if (id==riBtn.getId()){
                    riView.right();
                }else if (id==zhouBtn.getId()){
                    zhouView.right();
                }else if (id==yueBtn.getId()){
                    yueView.right();
                }
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=radioGroup.getCheckedRadioButtonId();
                if (id==riBtn.getId()){
                    riView.down();
                }else if (id==zhouBtn.getId()){
                    zhouView.down();
                }else if (id==yueBtn.getId()){
                    yueView.down();
                }

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=radioGroup.getCheckedRadioButtonId();
                if (id==riBtn.getId()){
                    riView.up();
                }else if (id==zhouBtn.getId()){
                    zhouView.up();
                }else if (id==yueBtn.getId()){
                    yueView.up();
                }
            }
        });
    }

    private void showPopupWindow(View v) {
        //设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_view, null);

        popupWindow = new PopupWindow(contentView,300,300, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(contentView);
        final TextView cjl =(TextView)contentView.findViewById(R.id.cjl);
        final TextView macd=(TextView)contentView.findViewById(R.id.macd);
        final TextView kdj =(TextView)contentView.findViewById(R.id.kdj);
        cjl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chooseStr=cjl.getText().toString();
                String old=chooseSpin.getText().toString();
                if (chooseStr.equals(old))
                    return ;
                chooseSpin.setText(chooseStr);
                riView.setTitle(chooseStr);
                zhouView.setTitle(chooseStr);
                yueView.setTitle(chooseStr);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });
        macd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chooseStr=macd.getText().toString();
                String old=chooseSpin.getText().toString();
                if (chooseStr.equals(old))
                    return ;
                chooseSpin.setText(chooseStr);
                riView.setTitle(chooseStr);
                zhouView.setTitle(chooseStr);
                yueView.setTitle(chooseStr);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });
        kdj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chooseStr=kdj.getText().toString();
                String old=chooseSpin.getText().toString();
                if (chooseStr.equals(old))
                    return ;
                chooseSpin.setText(chooseStr);
                riView.setTitle(chooseStr);
                zhouView.setTitle(chooseStr);
                yueView.setTitle(chooseStr);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //显示PopupWindow
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]+2, location[1]-popupWindow.getHeight()-2);
    }

    private class GetRiTask extends AsyncTask<Object, Integer, Result> {

        @Override
        protected Result doInBackground(Object[] objects) {
            if (code.equals("")){
                return null;
            }
            riList.clear();
            for (int i= GlobStr.K_ri_start;i<= GlobStr.K_ri_end;i++){
                GetHttpResult getHttpResult=new GetHttpResult(GlobStr.RiKUrl+i+"/"+code+".json");
                Result result=getHttpResult.getResult();
                if(result.getResultCode()==0){
                    riList.addAll(changeStrToKBean(result.getResultData()));
                }
            }
            if(isCancelled()) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Result result) {
            if (riList.size()==0)
                return;
//            Collections.reverse(riList);
            reList(riList);
//            riView.setLowerChartTabTitles(new String[]{"成交量","MACD", "KDJ"});
            riView.setLongiLatitudeColor(graytu);
            riList2.clear();
            riList2.addAll(riList);
            riView.setKData(riList2);
//            riView.postInvalidate();
            if (isDo){
                getRiTask=new GetRiTask();
                MyApplication.taskMap.put("ri",getRiTask);
            }
            super.onPostExecute(result);
        }
    }
    class GetZhouTask extends AsyncTask<Object, Integer, Result> {

        @Override
        protected Result doInBackground(Object[] objects) {
            if (code.equals("")){
                return null;
            }
            zhouList.clear();
            for (int i= GlobStr.K_zhou_start;i<= GlobStr.K_zhou_end;i++){
                GetHttpResult getHttpResult=new GetHttpResult(GlobStr.ZhouKUrl+i+"/"+code+".json");
                Result result=getHttpResult.getResult();
                if(result.getResultCode()==0){
                    zhouList.addAll(changeStrToKBean(result.getResultData()));
                }
            }

            if(isCancelled()) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Result result) {
//            Collections.reverse(zhouList);
            reList(zhouList);
//            zhouView.setLowerChartTabTitles(new String[]{"成交量","MACD", "KDJ"});
            zhouView.setLongiLatitudeColor(graytu);
            zhouList2.clear();
            zhouList2.addAll(zhouList);
            zhouView.setKData(zhouList2);
//            zhouView.postInvalidate();
            if (isDo){
                getZhouTask=new GetZhouTask();
                MyApplication.taskMap.put("zhou",getZhouTask);
            }

            super.onPostExecute(result);
        }
    }
    class GetYueTask extends AsyncTask<Object, Integer, Result> {

        @Override
        protected Result doInBackground(Object[] objects) {
            if (code.equals("")){
                return null;
            }
            yueList.clear();
            for (int i= GlobStr.K_yue_start;i<= GlobStr.K_yue_end;i++){
                GetHttpResult getHttpResult=new GetHttpResult(GlobStr.YueKUrl+i+"/"+code+".json");
                Result result=getHttpResult.getResult();
                if(result.getResultCode()==0){
                    yueList.addAll(changeStrToKBean(result.getResultData()));
                }
            }
            if(isCancelled()) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Result result) {
//            Collections.reverse(yueList);
            reList(yueList);
//            yueView.setLowerChartTabTitles(new String[]{"成交量","MACD", "KDJ"});
            yueView.setLongiLatitudeColor(graytu);
            yueList2.clear();
            yueList2.addAll(yueList);
            yueView.setKData(yueList2);
//            yueView.postInvalidate();
            if (isDo){
                getYueTask=new GetYueTask();
                MyApplication.taskMap.put("yue",getYueTask);
            }
            super.onPostExecute(result);
        }
    }
    private void reList(List<KBean> list){
        try{
            if (list==null||list.size()==0){
                return;
            }
            List<KBean> list0=new ArrayList<>();

            for (int i=list.size()-1;i>=0;i--){
                list0.add(list.get(i));
            }
            list.clear();
            list.addAll(list0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<KBean> changeStrToKBean(String str){
        List<KBean> kBeanList=new ArrayList<KBean>();
        Gson gson=new Gson();
        RKBean rk=gson.fromJson(str, RKBean.class);
        List rkDatas = rk.getData();
        ArrayList<String[]> arrayList = new ArrayList<String[]>();
        for(int i=0;i<rkDatas.size();i++ ){
            int indexBegin = rkDatas.get(i).toString().indexOf("[");
            int indexEnd = rkDatas.get(i).toString().indexOf("]");
            String rkData=rkDatas.get(i).toString().substring(indexBegin+1, indexEnd);
            String[] arr = rkData.split(",");
            arrayList.add(arr);
        }
        for(int i=0;i<arrayList.size();i++){
            KBean kBean=new KBean();
            kBean.setDate(arrayList.get(i)[0]);
            kBean.setOpen(Double.valueOf(arrayList.get(i)[1]));
            kBean.setClose(Double.valueOf(arrayList.get(i)[2]));
            kBean.setHigh(Double.valueOf(arrayList.get(i)[3]));
            kBean.setLow(Double.valueOf(arrayList.get(i)[4]));
            kBean.setVolume(Double.valueOf(arrayList.get(i)[5]));
            kBeanList.add(kBean);
        }
        return kBeanList;
    }

    private void finView(View view) {
        radioGroup= (RadioGroup) view.findViewById(R.id.radioGroup);
        riBtn= (RadioButton) view.findViewById(R.id.riBtn);
        zhouBtn= (RadioButton) view.findViewById(R.id.zhouBtn);
        yueBtn= (RadioButton) view.findViewById(R.id.yueBtn);
        riView= (KChartsView) view.findViewById(R.id.ri_view);
        riView.setInter(this);
        zhouView= (KChartsView) view.findViewById(R.id.zhou_view);
        zhouView.setInter(this);
        yueView= (KChartsView) view.findViewById(R.id.yue_view);
        yueView.setInter(this);
        left= (ImageView) view.findViewById(R.id.leftBtn);
        right= (ImageView) view.findViewById(R.id.rightBtn);
        up= (ImageView) view.findViewById(R.id.upBtn);
        down= (ImageView) view.findViewById(R.id.downBtn);
        chooseSpin= (TextView) view.findViewById(R.id.chooseType);
        time= (TextView) view.findViewById(R.id.time);
        today= (TextView) view.findViewById(R.id.todayopen);
        high= (TextView) view.findViewById(R.id.high);
        low= (TextView) view.findViewById(R.id.low);
        yest= (TextView) view.findViewById(R.id.yest);
    }

    @Override
    public void change(KBean kBean) {
        time.setText("时间:"+GlobMethod.changeDataToData(kBean.getDate()));
        today.setText("今开:"+kBean.getOpen()+"");
        high.setText("最高:"+kBean.getHigh()+"");
        low.setText("最低:"+kBean.getLow()+"");
        yest.setText("昨收:"+kBean.getClose()+"");

    }
    public interface IKChange{
        void changePageK(int i);
    }
    @Override
    public void changePage(int i) {
        ikChange.changePageK(i);
    }

    @Override
    public void setNull() {
        time.setText("");
        today.setText("");
        high.setText("");
        low.setText("");
        yest.setText("");
    }

}
