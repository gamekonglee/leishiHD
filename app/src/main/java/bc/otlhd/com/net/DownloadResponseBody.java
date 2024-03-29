package bc.otlhd.com.net;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * Created by gamekonglee on 2019/4/17.
 */

public class DownloadResponseBody extends ResponseBody {

    private Response originalResponse;
    private DownloadListener downloadListener;
    private long oldPoint = 0;

    public DownloadResponseBody(Response originalResponse, long startsPoint, DownloadListener downloadListener){
        this.originalResponse = originalResponse;
        this.downloadListener = downloadListener;
        this.oldPoint = startsPoint;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return originalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            private long bytesReaded = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                if (downloadListener != null) {
                    downloadListener.loading((int) ((bytesReaded+oldPoint)/(1024)));
                }
                return bytesRead;
            }
        });
    }
    public interface DownloadListener {

        /**
         *  开始下载
         */
        void start(long max);
        /**
         *  正在下载
         */
        void loading(int progress);
        /**
         *  下载完成
         */
        void complete(String path);
        /**
         *  请求失败
         */
        void fail(int code, String message);
        /**
         *  下载过程中失败
         */
        void loadfail(String message);
    }
}
