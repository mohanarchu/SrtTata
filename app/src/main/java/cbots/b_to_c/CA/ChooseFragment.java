package cbots.b_to_c.CA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cbots.b_to_c.R;

public class ChooseFragment<T> extends DialogFragment implements ChooserAdapter.ModelClicked {

    public ArrayList<T> arrayList;
    @BindView(R.id.modelRecyler)
    RecyclerView modelRecyler;
    ChooserAdapter chooserAdapter;
    ChooserAdapter.ModelClicked modelClicked;

    public void setItems(ArrayList<T> items) {
        this.arrayList = items;
    }

    public void setInterface( ChooserAdapter.ModelClicked modelClicked) {
        this.modelClicked = modelClicked;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chooser_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,R.style.dialogTheme);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        chooserAdapter = new ChooserAdapter();
        LinearLayoutManager centerZoomLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        modelRecyler.setLayoutManager(centerZoomLayoutManager);
        modelRecyler.setAdapter(chooserAdapter);
        chooserAdapter.setInterface(this);
        chooserAdapter.setList((ArrayList<String>) arrayList);
        chooserAdapter.notifyDataSetChanged();
    }

    @Override
    public void clickedItemModel(String model) {
        modelClicked.clickedItemModel(model);
        dismiss();
    }
}
