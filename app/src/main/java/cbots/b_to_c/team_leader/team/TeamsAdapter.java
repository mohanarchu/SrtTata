package cbots.b_to_c.team_leader.team;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cbots.b_to_c.R;
import cbots.b_to_c.config.SelectionInterface;
import cbots.b_to_c.home.DataPojo;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamHolder> {

    List<String> teamArray;
    int previousPos = -1;

    SelectionInterface<DataPojo.Results> selectionInterface;
    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeamHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull TeamHolder holder, int position) {

        holder.teamText.setText(teamArray.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectionInterface.onSelected(teamArray.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return teamArray.size();
    }

    public   void setArray(List<String> teamArrays) {
        this.teamArray = teamArrays;
        previousPos = 0;
    }
    public void setListener(SelectionInterface<DataPojo.Results> listener) {
        this.selectionInterface = listener;
    }

    class TeamHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.teamText)
        TextView teamText;
        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
