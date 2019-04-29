package com.jralison.nfcat;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jralison.nfcat.tagContent.TagContentPagerAdapter;
import com.jralison.nfcat.tagContent.TechListFragment;

public class TagContentActivity extends AppCompatActivity {

    public static final String EXTRA_TAG_CONTENT = "tagContent";
    public static final String EXTRA_TAG = "com.jralison.nfcat.TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_content);

        final Tag tag = getIntent().getParcelableExtra(EXTRA_TAG);

        final Bundle fragmentsData = new Bundle();
        fragmentsData.putStringArray(TechListFragment.KEY_TECH_LIST, tag.getTechList());

        final ViewPager viewPager = findViewById(R.id.view_pager_content);
        TagContentPagerAdapter adapter = new TagContentPagerAdapter(this, getSupportFragmentManager(), fragmentsData);
        viewPager.setAdapter(adapter);

        final TabLayout tabLayout = findViewById(R.id.tab_layout_content);
        tabLayout.setupWithViewPager(viewPager);
    }

}
