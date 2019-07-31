import com.google.gson.Gson;

import java.io.IOException;

import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.utils.LogUtils;
import bocang.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/2/28.
 */

public class NetTest  {

    public static void main(String[] args){
        OkHttpUtils.getGoodsList("0", "1", "0", "", "", "", "20", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String result=response.body().string();
                JSONObject jsonObject=new JSONObject(result);
                LogUtils.logE("result",jsonObject.toString());
            }
        });

    }
}
