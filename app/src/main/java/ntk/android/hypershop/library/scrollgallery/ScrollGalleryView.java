package ntk.android.hypershop.library.scrollgallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ntk.android.hypershop.R;
import ntk.android.hypershop.library.scrollgallery.loader.MediaLoader;

public class ScrollGalleryView extends LinearLayout {
    private FragmentManager fragmentManager;
    private Context context;
    private Point displayProps;
    private PagerAdapter pagerAdapter;
    private List<MediaInfo> mListOfMedia;

    // Options
    private int thumbnailSize; // width and height in pixels
    private boolean zoomEnabled;
    private boolean isThumbnailsHidden;
    private boolean hideThumbnailsOnClick;
    private Integer hideThumbnailsAfterDelay;

    // Views
    private LinearLayout thumbnailsContainer;
    private HorizontalScrollView horizontalScrollView;
    private ViewPager viewPager;

    // Transitions
    private Transition thumbnailsTransition;
    private boolean useDefaultThumbnailsTransition;

    // Listeners
    private final ViewPager.SimpleOnPageChangeListener viewPagerChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            scroll(thumbnailsContainer.getChildAt(position));
        }
    };

    private final OnClickListener thumbnailOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            scroll(v);
            viewPager.setCurrentItem(v.getId(), true);
        }
    };

    private OnImageClickListener onImageClickListener;

    private OnImageClickListener innerOnImageClickListener = new OnImageClickListener() {
        @Override
        public void onClick() {
            if (hideThumbnailsOnClick) {
                if (isThumbnailsHidden) {
                    showThumbnails();
                    isThumbnailsHidden = false;
                } else {
                    hideThumbnails();
                    isThumbnailsHidden = true;
                }
            }
            if (onImageClickListener != null) onImageClickListener.onClick();
        }
    };

    public interface OnImageClickListener {
        void onClick();
    }

    public ScrollGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mListOfMedia = new ArrayList<>();

        setOrientation(VERTICAL);
        displayProps = getDisplaySize();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.scroll_gallery_view, this, true);

        horizontalScrollView = findViewById(R.id.thumbnails_scroll_view);

        thumbnailsContainer = findViewById(R.id.thumbnails_container);
        thumbnailsContainer.setPadding(displayProps.x / 2, 0, displayProps.x / 2, 0);
    }

    public ScrollGalleryView setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        initializeViewPager();

        if (hideThumbnailsAfterDelay != null) hideThumbnailsAfterDelay(hideThumbnailsAfterDelay);

        return this;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public ScrollGalleryView addOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
        return this;
    }

    public void addOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                scroll(thumbnailsContainer.getChildAt(position));
                listener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                listener.onPageScrollStateChanged(state);
            }
        });
    }

    public ScrollGalleryView addMedia(MediaInfo mediaInfo) {
        if (mediaInfo == null) {
            throw new NullPointerException("Infos may not be null!");
        }

        return addMedia(Collections.singletonList(mediaInfo));
    }

    public ScrollGalleryView addMedia(List<MediaInfo> infos) {
        if (infos == null) {
            throw new NullPointerException("Infos may not be null!");
        }

        for (MediaInfo info : infos) {
            mListOfMedia.add(info);

            final ImageView thumbnail = addThumbnail(getDefaultThumbnail());
            info.getLoader().loadThumbnail(getContext(), thumbnail, new MediaLoader.SuccessCallback() {
                @Override
                public void onSuccess() {
                    thumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            });

            pagerAdapter.notifyDataSetChanged();
        }
        return this;
    }

    public ScrollGalleryView setCurrentItem(int i) {
        viewPager.setCurrentItem(i, false);
        return this;
    }

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    public ScrollGalleryView setThumbnailSize(int thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
        return this;
    }

    public ScrollGalleryView setZoom(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
        return this;
    }

    public ScrollGalleryView withHiddenThumbnails(boolean isThumbnailsHidden) {
        if (this.isThumbnailsHidden && !isThumbnailsHidden) {
            showThumbnails();
        } else if (!this.isThumbnailsHidden && isThumbnailsHidden) {
            hideThumbnails();
        }
        this.isThumbnailsHidden = isThumbnailsHidden;

        return this;
    }

    public ScrollGalleryView hideThumbnailsOnClick(boolean hideThumbnailsOnClick) {
        if (!isThumbnailsHidden) {
            this.hideThumbnailsOnClick = hideThumbnailsOnClick;
            if (hideThumbnailsOnClick) this.useDefaultThumbnailsTransition = true;
        }
        return this;
    }

    public ScrollGalleryView hideThumbnailsOnClick(boolean hideThumbnailsOnClick, Transition thumbnailsTransition) {
        if (!isThumbnailsHidden) {
            this.hideThumbnailsOnClick = hideThumbnailsOnClick;
            this.thumbnailsTransition = thumbnailsTransition;
        }
        return this;
    }

    public ScrollGalleryView hideThumbnailsAfter(int hideThumbnailsAfterDelay) {
        if (!isThumbnailsHidden) {
            this.hideThumbnailsAfterDelay = hideThumbnailsAfterDelay;
        }
        return this;
    }

    public void showThumbnails() {
        setThumbnailsTransition();
        horizontalScrollView.setVisibility(VISIBLE);
        // Hide thumbnails container when hideThumbnailsAfterDelay option is enabled
        if (hideThumbnailsAfterDelay != null) hideThumbnailsAfterDelay(hideThumbnailsAfterDelay);
    }

    public void hideThumbnails() {
        setThumbnailsTransition();
        horizontalScrollView.setVisibility(GONE);
    }

    /**
     * Remove all images from gallery
     */
    public void clearGallery() {
        // remove all media infos
        mListOfMedia.clear();
        // create new adapter
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, mListOfMedia, zoomEnabled, innerOnImageClickListener);
        viewPager.setAdapter(pagerAdapter);
        // remove thumbnails
        thumbnailsContainer.removeAllViews();
    }

    private void hideThumbnailsAfterDelay(int delay) {
        horizontalScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideThumbnails();
                isThumbnailsHidden = !isThumbnailsHidden;
            }
        }, delay);
    }

    // Make choice between default and provided by user transition
    private void setThumbnailsTransition() {
        if (thumbnailsTransition == null && useDefaultThumbnailsTransition) {
            TransitionManager.beginDelayedTransition(horizontalScrollView);
        } else if (thumbnailsTransition != null) {
            TransitionManager.beginDelayedTransition(horizontalScrollView, thumbnailsTransition);
        }
    }

    private Bitmap getDefaultThumbnail() {
        return ((BitmapDrawable) getContext().getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
    }

    private Point getDisplaySize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(point);
        } else {
            point.set(display.getWidth(), display.getHeight());
        }
        return point;
    }

    private ImageView addThumbnail(Bitmap image) {
        LayoutParams lp = new LayoutParams(thumbnailSize, thumbnailSize);
        lp.setMargins(10, 10, 10, 10);
        Bitmap thumbnail = createThumbnail(image);

        ImageView thumbnailView = createThumbnailView(lp, thumbnail);
        thumbnailsContainer.addView(thumbnailView);
        return thumbnailView;
    }

    private ImageView createThumbnailView(LayoutParams lp, Bitmap thumbnail) {
        ImageView thumbnailView = new ImageView(context);
        thumbnailView.setLayoutParams(lp);
        thumbnailView.setImageBitmap(thumbnail);
        thumbnailView.setId(mListOfMedia.size() - 1);
        thumbnailView.setOnClickListener(thumbnailOnClickListener);
        thumbnailView.setScaleType(ImageView.ScaleType.CENTER);
        return thumbnailView;
    }

    private Bitmap createThumbnail(Bitmap image) {
        return ThumbnailUtils.extractThumbnail(image, thumbnailSize, thumbnailSize);
    }

    private void initializeViewPager() {
        viewPager = (HackyViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, mListOfMedia, zoomEnabled, innerOnImageClickListener);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerChangeListener);
    }

    private void scroll(View thumbnail) {
        int[] thumbnailCoords = new int[2];
        thumbnail.getLocationOnScreen(thumbnailCoords);

        int thumbnailCenterX = thumbnailCoords[0] + thumbnailSize / 2;
        int thumbnailDelta = displayProps.x / 2 - thumbnailCenterX;

        horizontalScrollView.smoothScrollBy(-thumbnailDelta, 0);
    }

    private int calculateInSampleSize(int imgWidth, int imgHeight, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        while (imgWidth / inSampleSize > maxWidth || imgHeight / inSampleSize > maxHeight) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }
}
