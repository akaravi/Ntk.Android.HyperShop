package ntk.android.academy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.library.scrollgallery.MediaInfo;
import ntk.android.academy.library.scrollgallery.ScrollGalleryView;
import ntk.android.academy.library.scrollgallery.loader.DefaultImageLoader;

public class ActPhotoGallery extends AppCompatActivity {

    @BindView(R.id.scroll_gallery_view)
    ScrollGalleryView Gallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_photo_gallery);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        String Request = getIntent().getExtras().getString("Request");
        String Links[] = Request.split("@");
        List<String> links = Arrays.asList(Links);
        List<MediaInfo> infos = new ArrayList<>(links.size());
        for (String url : links) infos.add(MediaInfo.mediaLoader(new DefaultImageLoader(url)));
        Gallery
                .setThumbnailSize(250)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Gallery.clearGallery();
    }
}
