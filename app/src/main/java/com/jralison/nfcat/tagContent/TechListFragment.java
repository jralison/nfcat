package com.jralison.nfcat.tagContent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jralison.nfcat.R;

public class TechListFragment extends Fragment {

    public static final String KEY_TECH_LIST = "com.jralison.nfcat.TECH_LIST";
    public static final String TAG = "TechListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_tech_list, container, false);

        final RecyclerView mRecyclerView = v.findViewById(R.id.recycler_tech_list);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(KEY_TECH_LIST)) {
            final String[] techList = args.getStringArray(KEY_TECH_LIST);

            Log.d(TAG, (techList != null) ? techList.length + " tecnologia(s) na listagem" : "Lista de tecnologias é NULL.");

            final TechListAdapter mAdapter = new TechListAdapter(techList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
        } else {
            Log.i(TAG, "Sem argumento para " + KEY_TECH_LIST + ".");
            Toast.makeText(v.getContext(), "Lista de tecnologias está vazia.", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private class TechListAdapter extends RecyclerView.Adapter<TechListAdapter.TechViewHolder> {

        private final String[] mTechList;

        TechListAdapter(final String[] techList) {
            this.mTechList = techList;
        }

        @NonNull
        @Override
        public TechViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            return new TechViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_tech_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TechViewHolder holder, int position) {
            holder.vTitle.setText(mTechList[position]);
        }

        @Override
        public int getItemCount() {
            return mTechList.length;
        }

        class TechViewHolder extends RecyclerView.ViewHolder {

            private TextView vTitle;

            TechViewHolder(@NonNull View techView) {
                super(techView);
                vTitle = techView.findViewById(R.id.text_tech_title);
            }
        }
    }

}
