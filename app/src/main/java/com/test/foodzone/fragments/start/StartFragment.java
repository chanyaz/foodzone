package com.test.foodzone.fragments.start;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.foodzone.R;
import com.test.foodzone.adapters.start.IntroAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    @BindView(R.id.tab_layout)
    TabLayout tabDots;

    @BindView(R.id.view_pager)
    ViewPager pager;

    @BindView(R.id.rlHead)
    RelativeLayout rlHead;

    @BindView(R.id.tvhead)
    TextView tvhead;

    @BindView(R.id.imgSideArrow)
    ImageView imgSideArrow;

    IntroAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.bind(this,view);
        tabDots.setupWithViewPager(pager);
        adapter=new IntroAdapter(getActivity().getSupportFragmentManager());
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setPageTransformer(false,new DepthPageTransformer());
        pager.setAdapter(adapter);
        tabDots.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                updateUI(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        updateUI(0);

        rlHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(tabDots.getSelectedTabPosition()==tabDots.getTabCount()-1)
                {

                }
            }
        });

        return view;
    }


    public void updateUI(int pos)
    {
        if(tabDots.getTabCount()-1==pos)
        {
            pos=1;
        }
        else
        {
            pos=0;
        }
        switch (pos)
        {
            case 0:
                rlHead.setBackgroundResource(R.drawable.get_started_normal);
                tvhead.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                imgSideArrow.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                break;

            case 1:
                rlHead.setBackgroundResource(R.drawable.get_started_full);
                tvhead.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                imgSideArrow.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;

        }

    }



    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.3f;
        private static final float MIN_SCALE1 = 0.8f;


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            float absPosition = Math.abs(position);
            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0);
            }
            else if (position <= 0)
            { // [-1,0]

                final View bike = view.findViewById(R.id.imageBackground);
                if (bike != null)
                {
                    bike.setAlpha(1 - position);
                    bike.setTranslationX(pageWidth * -position);
                    float scaleFactor = MIN_SCALE1
                            + (1 - MIN_SCALE1) * (1 - Math.abs(position));
                    bike.setScaleX(scaleFactor);
                    bike.setScaleY(scaleFactor);
                }


                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
                view.bringToFront();

                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(scaleFactor);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.

                final View bike = view.findViewById(R.id.imageBackground);
                if (bike != null)
                {
                    bike.setAlpha(1 - position);
                    bike.setTranslationX(pageWidth * -position);
                    float scaleFactor = MIN_SCALE1
                            + (1 - MIN_SCALE1) * (1 - Math.abs(position));
                    bike.setScaleX(scaleFactor);
                    bike.setScaleY(scaleFactor);
                }


                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(scaleFactor);


            }
            else
            { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);


            }
        }
    }
}
