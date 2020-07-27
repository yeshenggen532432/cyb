package com.xmsx.cnlife.widget.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.widget.TextView;

import com.chiyong.t3.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

public class ImagePagerActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
	private ImagePagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.x_activity_image_pager);

        Intent intent = getIntent();
		pagerPosition = intent.getIntExtra(EXTRA_IMAGE_INDEX, 0);
		/**注意 该urls是完整的地址 */
        String[] urls = intent.getStringArrayExtra(EXTRA_IMAGE_URLS);
        
        mPager = (HackyViewPager) findViewById(R.id.pager_hacky);
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
        mPager.setOffscreenPageLimit(3);
        
    }

    
    @Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		overridePendingTransition(0, R.anim.zoom_exit);
	}
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    public class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            url = MyUrlUtil.getUrl(url);
            return ImageDetailFragment.newInstance(url);
        }

    }
}