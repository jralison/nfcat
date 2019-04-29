package com.jralison.nfcat.tagContent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jralison.nfcat.R;

public class ContentFragment extends Fragment {

    TextView mTagContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tag_content, container, false);
        mTagContent = view.findViewById(R.id.text_tag_content);
        return view;
    }

}
