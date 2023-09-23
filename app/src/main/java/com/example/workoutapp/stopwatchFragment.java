package com.example.workoutapp;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class stopwatchFragment extends Fragment{

    private ImageView pausePlayButton;
    private ImageView stopButton;

    private TextView timer;
    private TextView muscleGroup;

    private boolean running = false;

    private RecyclerView recyclerView;

    private ZoomCenterLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pausePlayButton = getView().findViewById(R.id.playpause);
        stopButton = getView().findViewById(R.id.stop);

        timer = getView().findViewById(R.id.stopwatchTextview);
        recyclerView = getView().findViewById(R.id.recyclerView);
        layoutManager = new ZoomCenterLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        muscleGroup = getView().findViewById(R.id.muscleGroup);

        int[] iconResIds = {
                R.color.beige2,
                R.drawable.chesticon,
                R.drawable.absicon,
                R.drawable.backicon,
                R.drawable.shouldericon,
                R.drawable.tricepicon,
                R.drawable.bicepsicon,
                R.drawable.legsicon,
                R.drawable.cardioicon,
                R.color.beige2
        };
        MyAdapter adapter = new MyAdapter(iconResIds); // Create your custom adapter
        recyclerView.setAdapter(adapter);
        int itemSpacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = itemSpacing / 2;
                outRect.right = itemSpacing / 2;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int center = layoutManager.getWidth() / 2;
                View closestChild = null;
                int closestDistance = Integer.MAX_VALUE;
                for (int i = 0; i < layoutManager.getChildCount(); i++) {
                    View child = layoutManager.getChildAt(i);
                    int childCenter = (layoutManager.getDecoratedRight(child) + layoutManager.getDecoratedLeft(child)) / 2;
                    int distance = Math.abs(center - childCenter);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestChild = child;
                    }
                }
                int position = recyclerView.getChildLayoutPosition(closestChild);
                updateMuscleGroupText(position);
            }
        });

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.smoothScrollBy(1, 0);
        stopwatch();
        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    pausePlayButton.setImageResource(R.drawable.play);
                    running = false;
                } else {
                    pausePlayButton.setImageResource(R.drawable.pause);
                    running = true;
                }
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                pausePlayButton.setImageResource(R.drawable.play);
                seconds = 0;
                timer.setText("00:00:00");
            }
        });
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private int[] iconResIds; // Array of image resource IDs

        public MyAdapter(int[] iconResIds) {
            this.iconResIds = iconResIds;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            int iconResId = iconResIds[position];
            holder.iconImageView.setImageResource(iconResId);
        }

        @Override
        public int getItemCount() {
            return iconResIds.length; // Return the number of items
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iconImageView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                iconImageView = itemView.findViewById(R.id.iconImageView);
            }
        }
    }

    public class ZoomCenterLayoutManager extends LinearLayoutManager {

        private final float mShrinkAmount = .4f;
        private final float mShrinkDistance = .6f;

        public ZoomCenterLayoutManager(Context context) {
            super(context);
            setOrientation(LinearLayoutManager.HORIZONTAL);
        }

        @Override
        public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            float midpoint = getWidth() / 2.0f;
            float d0 = 0.0f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.0f;
            float s1 = 1.0f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.0f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
            }
            return scrolled;
        }
    }

    private void updateMuscleGroupText(int position) {
        switch (position) {
            case 1:
                muscleGroup.setText("Chest");
                break;
            case 2:
                muscleGroup.setText("Abs");
                break;
            case 3:
                muscleGroup.setText("Back");
                break;
            case 4:
                muscleGroup.setText("Shoulders");
                break;
            case 5:
                muscleGroup.setText("Triceps");
                break;
            case 6:
                muscleGroup.setText("Biceps");
                break;
            case 7:
                muscleGroup.setText("Legs");
                break;
            case 8:
                muscleGroup.setText("Cardio");
                break;
        }
    }

    private int seconds = 0;

    private void stopwatch(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = 0;
                int minutes = 0;
                if(running){
                    seconds++;
                    if(seconds == 60){
                        seconds = 0;
                        minutes++;
                        if(minutes == 60){
                            minutes = 0;
                            hours++;
                        }
                    }
                }
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timer.setText(time);
                handler.postDelayed(this, 1000);
            }
        });
    }

}
