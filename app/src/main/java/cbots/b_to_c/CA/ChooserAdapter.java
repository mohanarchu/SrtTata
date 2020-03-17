package cbots.b_to_c.CA;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cbots.b_to_c.R;

public class ChooserAdapter extends RecyclerView.Adapter<ChooserAdapter.ModelHolder> {

    ArrayList<String> models;
    ModelClicked modelClicked;


    @NonNull
    @Override
    public ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModelHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chooser_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelHolder holder, int position) {
        holder.setDatas(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }

    class ModelHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textForChoose)
        TextView textForChoose;

        public ModelHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modelClicked.clickedItemModel(models.get(getAdapterPosition()));
                }
            });
        }

        void setDatas(String s) {
            textForChoose.setText(s);
        }
    }

    public void setList(ArrayList<String> models) {
        this.models = models;
    }

    public void setInterface(ModelClicked modelClicked) {
        this.modelClicked = modelClicked;
    }

    public interface ModelClicked {
        void clickedItemModel(String model);
    }

}
