package bc.otlhd.com.ui.activity.programme;

import android.content.Intent;
import android.view.View;

import com.bm.library.PhotoView;

import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Programme;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.data.ProgrammeDao;
import bc.otlhd.com.utils.ImageLoadProxy;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/28 9:40
 * @description :
 */
public class ImageDetailActivity extends BaseActivity {
    private String mImagePath="";
    private PhotoView photo_iv;
    private byte[] mPhoto;
    private ProgrammeDao mProgrammeDao;
    private List<Programme> mProgrammes;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_detail);
        photo_iv = (PhotoView)findViewById(R.id.photo_iv);
        photo_iv.enable();
        mProgrammeDao=new ProgrammeDao(this);
        mProgrammes= mProgrammeDao.getData("","",0);
        if(!AppUtils.isEmpty(mImagePath)){
            ImageLoadProxy.displayImage(mImagePath, photo_iv);
        }else{
            ImageLoadProxy.displayImage( NetWorkConst.UR_PLANIMAGE+mProgrammes.get(0).getSceenpath(), photo_iv);
        }

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mImagePath=intent.getStringExtra(Constance.photo);
//        mPhoto=intent.getByteArrayExtra(Constance.img);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
