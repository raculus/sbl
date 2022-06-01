package com.raculus.sbl.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raculus.sbl.Activity.NearbyActivity;
import com.raculus.sbl.Activity.StationActivity;
import com.raculus.sbl.ListView_Adapter.Bus_Adapter;
import com.raculus.sbl.ListView_Adapter.Station_Adapter;
import com.raculus.sbl.OpenAPI.Station;
import com.raculus.sbl.R;
import com.raculus.sbl.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    ListView listView;

    ArrayList<Station> stationArrayList = new ArrayList<>();
    private void addBus(Station station){
        stationArrayList.add(station);
        if(stationArrayList.size() > 0 ){
            final Bus_Adapter bus_adapter = new Bus_Adapter(getContext(), stationArrayList);
            Log.e("station name: ", station.getStationName()+"");
            listView.setAdapter(bus_adapter);
        }
    }
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    Station station = (Station) intent.getSerializableExtra("Station");
                    addBus(station);
                }
            }
    );

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fabAdd = root.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(view -> busAdd());

        listView = root.findViewById(R.id.listView);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void busAdd(){
        Intent intent = new Intent(getActivity(), NearbyActivity.class);
        mStartForResult.launch(intent);
    }
}