package com.catacore.dropdownnotification;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.catacore.dropdownnotification.notifications.INotification;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Boolean.TRUE;

public class CoreNotificationCenter {
    private static CoreNotificationCenter instance;
    private static Activity currentActivity;
    private static boolean recalculationEnded;
    private int mInterval = 5;
    private int mShift = 5;
    private static final int DefaultElevation = 16;
    private static ArrayList<PopupWindow> notificationsList;
    private static ArrayList<Integer> notificationsPositions;
    private static DisplayMetrics metrics;
    private int screenH;
    private int screenW;



    private static int gapBetween = 50;
    private int lastHigh;

    private CoreNotificationCenter(Activity activity){
        notificationsList = new ArrayList<>();
        notificationsPositions = new ArrayList<>();
        currentActivity = activity;
        recalculationEnded = true;
        // instantiate DisplayMetrics
        DisplayMetrics dm = new DisplayMetrics();
        // fill dm with data from current display
        currentActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        metrics = dm;

        screenH = metrics.heightPixels;
        screenW = metrics.widthPixels;
        lastHigh = gapBetween;
    }

    public static CoreNotificationCenter getInstance(Activity activity) {
        if(instance!=null){
            currentActivity = activity;
            return instance;
        }
        instance = new CoreNotificationCenter(activity);
        return instance;
    }

    public void add(INotification notification)
    {
        final PopupWindow popupWindow = new PopupWindow(currentActivity);
        popupWindow.setContentView(notification.getRootView());
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setElevation(DefaultElevation);
        switch (notification.getType())
        {
            case INFO:{
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        //TODO: de facut
                    }
                });
                break;
            }
//            case ERROR:{
//                break;
//            }
//            case SUCCESS:{
//                break;
//            }
//            case CONFIRMATION:{
//                break;
//            }
        }

//        final Handler mHandler = new Handler();
//        final Runnable adder = new Runnable() {
//            @Override
//            public void run() {
//                if(recalculationEnded ) {
//                    recalculationEnded = false;
//
//                    notificationsList.add(popupWindow);
//                    notificationsPositions.add(lastHigh +250 + gapBetween);
//                    int pos = notificationsList.size()-1;
//                    lastHigh += 250 + gapBetween;
//                    showNotification(pos,notificationsPositions.get(pos));
//
//                    mHandler.removeCallbacks(this);
//                    recalculationEnded = true;
//                }
//                else
//                {
//                    mHandler.postDelayed(this, mInterval);
//                }
//            }
//
//        };
//        adder.run();

        notificationsList.add(popupWindow);
        notificationsPositions.add(lastHigh+= 250 + gapBetween);
        //lastHigh += 250 + gapBetween;
        int pos = notificationsList.size()-1;
        showNotification(pos,notificationsPositions.get(pos));

    }



    private void recalculatePositionsAndShow2() {

        int currentSize = notificationsList.size();
        if(currentSize == 0)
        {
            lastHigh = 0;
            recalculationEnded = true;
            return;
        }
//            int shiftH = notificationsList.get(currentSize-1).getHeight();
        int shiftH = 250;
       // lastHigh  = lastHigh -shiftH - gapBetween;

        for(int i=0;i<currentSize;i++)
        {
            //move up this notification
            //adding last item size + gap
            animateViewUp(i,notificationsPositions.get(i),notificationsPositions.get(i) - shiftH - gapBetween, i == currentSize -1);
        }


        //after ended, verify if new notifications showed up
//        if(currentSize != notificationsList.size())
//        {
//            recalculatePositionsAndShow2();
//        }

    }


    private void animateViewUp(final Integer position, Integer start, final Integer end, final boolean last) {
        final Handler mHandler = new Handler();
        final Runnable shifter = new Runnable() {
            @Override
            public void run() {
                int lastPos = notificationsList.size()-1;
                if(!notificationsPositions.get(position).equals(end)) {
                    //move up

                    if(notificationsPositions.get(position) - mShift < end)
                    {
                        notificationsList.get(position).update(0,notificationsPositions.get(position) - (end - notificationsPositions.get(position)),-1,-1);
                        notificationsPositions.set(position, end - notificationsPositions.get(position));
                        if(last)
                            lastHigh  = lastHigh - (end - notificationsPositions.get(position));
                    }
                    else
                    {
                        notificationsList.get(position).update(0,notificationsPositions.get(position) - mShift,-1,-1);
                        notificationsPositions.set(position,notificationsPositions.get(position) - mShift);
                        if(last)
                            lastHigh  = lastHigh - mShift;
                    }

                    mHandler.postDelayed(this, mInterval);

                }
                else
                {
                    //
                    if(last)
                    {
                        //the last one finished moving up
                        recalculationEnded = true;
                    }
                    mHandler.removeCallbacks(this);
                }
            }
        };
        shifter.run();
    }



    public static Handler UIHandler;
    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }



    public void showNotification(final int position,int heigh) {
        View rootView = currentActivity.findViewById(android.R.id.content).getRootView();
        if(rootView==null)
        {
            rootView = currentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        }
        if(rootView == null)
            return;

        if(notificationsList.size()>0) {
            try{

                notificationsList.get(position).showAtLocation(((ViewGroup) rootView).getChildAt(0), Gravity.NO_GRAVITY, 0, heigh);
                final PopupWindow myPop = notificationsList.get(position);
                final Timer t = new Timer();

                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUI(new Runnable() {
                            @Override
                            public void run() {

                                final Handler mHandler = new Handler();
                                final Runnable dismisser = new Runnable() {
                                    @Override
                                    public void run() {
                                        int mypos = notificationsList.indexOf(myPop);
                                        if(recalculationEnded && mypos == 0) {
                                            recalculationEnded = false;
                                            //doar dupa ce au fost toate mutate, pot scoate altul

                                            if(notificationsList.get(mypos).isShowing()==TRUE)
                                            {
                                                notificationsList.get(mypos).dismiss();
                                                notificationsList.remove(mypos);
                                                notificationsPositions.remove(mypos);
                                                recalculatePositionsAndShow2();
                                            }
                                            mHandler.removeCallbacks(this);

                                        }
                                        else
                                        {
                                            mHandler.postDelayed(this, mInterval);
                                        }
                                    }

                                };
                                dismisser.run();
                                t.cancel();
                            }
                        });
                    }
                },2500);
            }
            catch (Exception e)
            {

            }

        }
    }
}
