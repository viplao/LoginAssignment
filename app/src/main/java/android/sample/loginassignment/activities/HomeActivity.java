package android.sample.loginassignment.activities;

import android.graphics.Bitmap;
import android.sample.loginassignment.Constants;
import android.sample.loginassignment.R;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Created by viplao on 03/04/17.
 */

public class HomeActivity extends AppCompatActivity {

    private TextView mUsername;
    private NetworkImageView mImageView;
    private RequestQueue mMainRequestQueue;
    private ImageLoader mImageLoader;
    private final String mImageUrl = "http://www.rutlandherald.com/wp-content/uploads/2017/03/default-user.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMainRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mMainRequestQueue, getImageCache());

        mUsername = (TextView) findViewById(R.id.username_textview);

        mImageView = (NetworkImageView) findViewById(R.id.user_image);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageLoader.get(mImageUrl, ImageLoader.getImageListener(mImageView, android.R.drawable.ic_search_category_default, android.R.drawable.ic_dialog_alert));
        mImageView.setImageUrl(mImageUrl, mImageLoader);
    }

    @Override
    protected void onStart() {
        super.onStop();
        Bundle bundle = getIntent().getExtras();
        if(null != bundle){
            String username = bundle.getString(Constants.KEY_USER_NAME);
            if(username != null || !TextUtils.isEmpty(username)) {
                this.mUsername.setText(username);
            }
        }
    }

    private ImageLoader.ImageCache getImageCache() {
        return new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> customImageCache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return customImageCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                customImageCache.put(url, bitmap);
            }
        };
    }
}
