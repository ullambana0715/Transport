package com.yang.netokhttp3;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/11/15.
 */

public class ProgressBody extends RequestBody {
    UploadProgressListener mListener;
    BufferedSink bufferedSink;
    RequestBody requestBody;
    public ProgressBody(UploadProgressListener listener, RequestBody body){
        mListener = listener;
        requestBody = body;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null){
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink){
        return new ForwardingSink(sink) {
            long totalSize;
            long currentSize;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                currentSize += byteCount;
                if (totalSize == 0){
                    totalSize = contentLength();
                }
                rx.Observable.just(currentSize)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mListener.onProgress(totalSize,currentSize);
                            }
                        });
            }
        };
    }
}
