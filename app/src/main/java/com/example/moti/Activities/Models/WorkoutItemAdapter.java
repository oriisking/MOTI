package com.example.moti.Activities.Models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moti.R;
import com.example.moti.util.lib.stickyindex.TextGetter;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkoutItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TextGetter, Filterable {

    private Context context;
    private List<WorkoutItem> dataSet;
    private List<WorkoutItem> mFilteredList;

    public WorkoutItemAdapter(List<WorkoutItem> workoutItems, Context c) {
        this.dataSet = workoutItems;
        this.context = c;
        this.mFilteredList = workoutItems;
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

        //setRegularLineLayout(workoutItemViewHolder);
    }

    private void setRegularLineLayout(WorkoutItemViewHolder vh) {
//        vh.txtDay.setTextColor(Color.parseColor("#ffffff"));
//        vh.txtDay.setTextSize(26);
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

    public List<WorkoutItem> getDataSet() {
        return mFilteredList;
    }

    public void setDataSet(List<WorkoutItem> workoutItems) {
        mFilteredList = workoutItems;
        notifyDataSetChanged();
    }

    @Override
    public String getTextFromAdapter(int pos) {
        return String.valueOf(mFilteredList.get(pos).getExerciseDay().charAt(0)).toUpperCase();
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

    public class WorkoutItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtDay, txtName, txtWeight, txtRepeats;
        ImageButton btnEdit, btnDelete;

        public WorkoutItemViewHolder(View v) {
            super (v);
            txtDay = (TextView) v.findViewById(R.id.txt_ex_day);
            txtName = (TextView) v.findViewById(R.id.txt_ex_name);
            txtWeight = (TextView) v.findViewById(R.id.txt_ex_weight);
            txtRepeats = (TextView) v.findViewById(R.id.txt_ex_repeats);
            btnEdit = (ImageButton) v.findViewById(R.id.btn_edit);
            btnDelete = (ImageButton) v.findViewById(R.id.btn_delete);

            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            WorkoutItem workoutItem = dataSet.get(getAdapterPosition());
            switch (v.getId()){
                case R.id.btn_edit:
                    break;

                case R.id.btn_delete:
                    break;
            }
        }
    }
}
