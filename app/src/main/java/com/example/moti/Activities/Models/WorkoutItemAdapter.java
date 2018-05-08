package com.example.moti.Activities.Models;

/**
 * Created by User on 13/02/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moti.R;
import com.example.moti.listener.OnWorkoutClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorkoutItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private List<WorkoutItem> dataSet;
    private List<WorkoutItem> mFilteredList;

    private OnWorkoutClickListener mListener;

    public WorkoutItemAdapter(List<WorkoutItem> workoutItems, Context c) {
        this.dataSet = workoutItems;
        this.context = c;
        this.mFilteredList = workoutItems;

        shortDataByDay(mFilteredList);
        shortDataByFBW(mFilteredList);
    }

    public void setListener(OnWorkoutClickListener listener){
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_workout, parent, false);

        return new WorkoutItemViewHolder(view);
    }

    private WorkoutItem workoutItem;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final WorkoutItemViewHolder workoutItemViewHolder = (WorkoutItemViewHolder) holder;

        workoutItem = mFilteredList.get(position);
        workoutItemViewHolder.txtName.setText(workoutItem.getExerciseName());
        workoutItemViewHolder.txtDay.setText(workoutItem.getExerciseDay());
        workoutItemViewHolder.txtWeight.setText(String.valueOf(workoutItem.getExerciseWeight()));
        workoutItemViewHolder.txtRepeats.setText(String.valueOf(workoutItem.getExerciseRepeats()));
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public WorkoutItem getWorkoutItem (int pos) {
        return mFilteredList.get(pos);
    }

    public WorkoutItem getWorkoutItemByName (String name) {
        for (WorkoutItem c : mFilteredList) {
            String data = c.getExerciseName().toLowerCase();
            if (data.contains(name.toLowerCase())) {
                return c;
            }
        }

        return null;
    }

    public void addData(WorkoutItem workoutItem){
        mFilteredList.add(workoutItem);
        shortDataByDay(mFilteredList);
        shortDataByFBW(mFilteredList);
        notifyDataSetChanged();
    }

    public List<WorkoutItem> getDataSet() {
        return mFilteredList;
    }

    public void setDataSet(List<WorkoutItem> workoutItems) {
        mFilteredList = workoutItems;
        shortDataByDay(mFilteredList);
        shortDataByFBW(mFilteredList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                    mFilteredList = dataSet;
                } else {
                    List<WorkoutItem> filteredList = new ArrayList<>();

                    for (WorkoutItem workoutItem : dataSet) {

                        if (workoutItem.getExerciseDay().toLowerCase().contains(charString)
                                || workoutItem.getExerciseName().toLowerCase().contains(charString)
                                || String.valueOf(workoutItem.getExerciseWeight()).contains(charString)
                                || String.valueOf(workoutItem.getExerciseRepeats()).contains(charString)) {

                            filteredList.add(workoutItem);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<WorkoutItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public void removeItemByID(int id)
    {
        int position=0;
        WorkoutItem wi;
        for (int i = 0; i< mFilteredList.size(); i++)
        {
            wi = mFilteredList.get(i);
            if(wi.getId() == id)
                position = i;
        }
        removeItem(position);
    }

    public void removeItem(int position) {
        mFilteredList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(WorkoutItem workoutItem, int position) {
        mFilteredList.add(position, workoutItem);
        // notify item added by position
        notifyItemInserted(position);
    }

    //first short the data by ascending ordered alphabets
    public void shortDataByDay(List<WorkoutItem> workoutItems){
        Collections.sort(workoutItems, new Comparator<WorkoutItem>(){
            public int compare(WorkoutItem obj1, WorkoutItem obj2) {
                return obj1.getExerciseDay()
                        .toUpperCase()
                        .compareTo(obj2.getExerciseDay().toUpperCase());
            }
        });
    }

    //2nd shorting for bringing the 'FBW' in the start
    public void shortDataByFBW(List<WorkoutItem> workoutItems){
        Collections.sort(workoutItems, new Comparator<WorkoutItem>(){
            public int compare(WorkoutItem obj1, WorkoutItem obj2) {
                //get the compare value
                int comp = obj1.getExerciseDay().compareToIgnoreCase("FBW");
                //0 means it contains "FBW" so bring it to front
                //-1 means obj1 is smaller so it will go to front
                if (comp == 0) return -1;

                //if not then send it to rear
                //1 means obj1 is greater so it will go to rears
                return 1;
            }
        });
    }

    public class WorkoutItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtDay, txtName, txtWeight, txtRepeats;
        ImageView btnEdit, btnDelete;

        public WorkoutItemViewHolder(View v) {
            super (v);
            txtDay = (TextView) v.findViewById(R.id.txt_ex_day);
            txtName = (TextView) v.findViewById(R.id.txt_ex_name);
            txtWeight = (TextView) v.findViewById(R.id.txt_ex_weight);
            txtRepeats = (TextView) v.findViewById(R.id.txt_ex_repeats);
            btnEdit = (ImageView) v.findViewById(R.id.btn_edit);
            btnDelete = (ImageView) v.findViewById(R.id.btn_delete);

            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null) mListener.onItemClick(v, dataSet.get(getAdapterPosition()));
        }
    }
}
