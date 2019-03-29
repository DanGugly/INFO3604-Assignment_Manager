package info3604.assignment_organizer.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.CheckpointController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Checkpoint;
import info3604.assignment_organizer.views.update_checkpoint;


public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.ViewHolder>  {

    private List<Checkpoint> mCheckpointList;
    private Context mContext;
    private RecyclerView mRecyclerV;
    private MainController MC;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView chkCourseCode;
        public TextView assignmentTitle;
        public TextView checkpointTitle;
        public TextView dueDate;
        public CheckBox chkCheckbox;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;

            chkCourseCode = (TextView)v.findViewById(R.id.chkCourseCode);
            checkpointTitle = (TextView)v.findViewById(R.id.checkpointTitle);
            assignmentTitle = (TextView)v.findViewById(R.id.assignmentTitle);
            dueDate = (TextView)v.findViewById(R.id.dueDate);
            chkCheckbox = (CheckBox) v.findViewById(R.id.chkCheckbox);
        }
    }

    public void add(int position, Checkpoint checkpoint) {
        mCheckpointList.add(position, checkpoint);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mCheckpointList.size());
    }

    public void remove(int position) {
        mCheckpointList.remove(position);
        mRecyclerV.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCheckpointList.size());
    }

    public CheckpointAdapter(List<Checkpoint> mCheckpointList, Context mContext, RecyclerView mRecyclerV) {
        this.mCheckpointList = mCheckpointList;
        this.mContext = mContext;
        this.mRecyclerV = mRecyclerV;
    }

    @NonNull
    @Override
    public CheckpointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MC = new MainController(mContext);

        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.checkpoint_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        CheckpointAdapter.ViewHolder vh = new CheckpointAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckpointAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Checkpoint checkpoint = mCheckpointList.get(position);
        holder.chkCourseCode.setText("Course Code: " + MC.getAssignment(checkpoint.getAssignmentID()).getCourseID());
        Log.d("COURSE CODE:", "" + MC.getAssignment(checkpoint.getAssignmentID()).getCourseID());
        holder.assignmentTitle.setText("Assignment Title: " + MC.getAssignment(checkpoint.getAssignmentID()).getTitle());
        holder.checkpointTitle.setText("Checkpoint Title: " + checkpoint.getTitle());
        holder.dueDate.setText("Due Date: " + checkpoint.getDueDate());

        int progress = checkpoint.getProgress();
        if(progress == 0){
            holder.chkCheckbox.setChecked(false);
        }
        else{
            holder.chkCheckbox.setChecked(true);
        }

        //If I want to add an image
        //Picasso.with(mContext).load(checkpoint.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.checkpointImageImgV);

        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete checkpoint?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //go to update activity
                        goToUpdateActivity(checkpoint.getCheckpointID());

                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckpointController dbHelper = new CheckpointController(mContext);
                        dbHelper.deleteCheckpoint(checkpoint.getCheckpointID());

                        mCheckpointList.remove(position);
                        mRecyclerV.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mCheckpointList.size());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }

    private void goToUpdateActivity(int checkpoint_id){
        Intent goToUpdate = new Intent(mContext, update_checkpoint.class);
        goToUpdate.putExtra("CHECKPOINT_ID", checkpoint_id);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() { return mCheckpointList.size(); }
}
