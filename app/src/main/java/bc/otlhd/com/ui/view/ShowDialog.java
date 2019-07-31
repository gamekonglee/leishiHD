package bc.otlhd.com.ui.view;

import android.content.Context;
import android.view.View;

/**
 * Created by WZH on 2016/12/11.
 */

public class ShowDialog {
    private CustomDialog selfDialog ;
    public  boolean noDismiss=false;
    public ShowDialog() {
    }
    public void show(final Context ctx, String title, String message, final OnBottomClickListener onBottomClickListener){
        selfDialog = new CustomDialog(ctx);
        selfDialog.setTitle(title);
        selfDialog.setMessage(message);

        selfDialog.setYesOnclickListener("确定", new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (onBottomClickListener!=null){
                    onBottomClickListener.positive();
                }
                if(!noDismiss){
                selfDialog.dismiss();
                }
            }
        });
        selfDialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                if (onBottomClickListener!=null){
                    onBottomClickListener.negtive();
                }
                if(!noDismiss)
                selfDialog.dismiss();
            }
        });
        selfDialog.show();
        if(noDismiss){
            selfDialog.yes.setVisibility(View.GONE);
            selfDialog.no.setVisibility(View.GONE);
        }
    }


    public boolean isShowing(){
     return selfDialog==null?false:selfDialog.isShowing();
    }

    public void setNoDismiss(boolean noDismiss) {

        this.noDismiss = noDismiss;
    }

    public interface OnBottomClickListener{
        void positive();
        void negtive();
    }
}
