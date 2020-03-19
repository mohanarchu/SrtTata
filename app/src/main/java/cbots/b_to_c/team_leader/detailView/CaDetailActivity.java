package cbots.b_to_c.team_leader.detailView;

import android.os.Bundle;

import com.google.gson.JsonObject;

import cbots.b_to_c.BaseActivity;
import cbots.b_to_c.R;

public class CaDetailActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.ca_detail_activity;
    }

    @Override
    protected void onViewBound() {
        TeamDetailView teamDetailView = new TeamDetailView();
        Bundle bundle  = new Bundle();
        bundle.putString("name",getIntent().getStringExtra("CaName"));
        teamDetailView.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.caDetilFrame, teamDetailView).commit();
    }
}
