package com.example.jhw.exblockdetailapplication.actiblock;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jhw.exblockdetailapplication.common.BaseActivity;
import com.example.jhw.exblockdetailapplication.common.GpsTracker;
import com.example.jhw.exblockdetailapplication.data.HorizonRepo;
import com.example.jhw.exblockdetailapplication.middleblock.MiddleblockActivity;
import com.example.jhw.exblockdetailapplication.data.PoiRepo;
import com.example.jhw.exblockdetailapplication.R;
import com.example.jhw.exblockdetailapplication.common.RetrofitExService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActiblockActivity extends BaseActivity {

    private HashMap<String, HorizonRepo>[] horizontalList;
    private ArrayList<HorizonRepo> verticalList;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private VerticalRecyclerAdapter mAdapter;

    private int responseCount;
    private String TmapapiKey;
    private GpsTracker gpsTracker;

    private static final int MIDDLE_ADD_REQUEST_CODE = 3000;
    private static final int MIDDLE_ALTER_REQUEST_CODE = 3001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actiblock);

        TmapapiKey = getMetadata(this, "com.example.jhw.TmapKey");
        showWaitingDialog();

        // 0 : 숙박, 1 : 음식점, 2 : 레저
        horizontalList = new HashMap[3];
        for (int i = 0; i < horizontalList.length; i++) {
            horizontalList[i] = new HashMap<>();
        }

        gpsTracker = new GpsTracker(ActiblockActivity.this);
        getData();

    }

    private void init() {

        verticalList = new ArrayList<>();
        verticalList.add(null);
        verticalList.add(new HorizonRepo(0,"숙박",horizontalList[0].get("숙박").getPoiList(),1));
        verticalList.add(new HorizonRepo(1,"TV맛집",horizontalList[1].get("TV맛집").getPoiList(),2));
        verticalList.add(new HorizonRepo(2,"놀거리",horizontalList[2].get("놀거리").getPoiList(),3));

        mAdapter = new VerticalRecyclerAdapter(verticalList);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.vertical_recycler_view);
        mRecyclerView.setHasFixedSize(true); // 성능 개선
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnVclickListener(new VerticalRecyclerAdapter.OnVerticalClickListener() {
            @Override
            public void onVitemClick(View view, int vposition) {
                switch (view.getId()) {
                    case R.id.add_block:
                        Intent intent = new Intent(ActiblockActivity.this, MiddleblockActivity.class);
                        intent.putExtra("addposition", vposition);
                        intent.putExtra("list", horizontalList);
                        startActivityForResult(intent, MIDDLE_ADD_REQUEST_CODE);
                        break;
                    case R.id.del_block:
                        verticalList.remove(vposition);
                        mAdapter.notifyItemRemoved(vposition);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "default " + vposition, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mAdapter.setOnHclickListener(new VerticalRecyclerAdapter.OnHorizontalClickListener() {
            @Override
            public void onHitemClick(View view, int hposition, int vposition) {
                HorizonRepo horizonRepo = verticalList.get(vposition);
                horizonRepo.sethIndex(hposition);
                horizonRepo.setvIndex(vposition);
                Intent intent = new Intent(ActiblockActivity.this, MiddleblockActivity.class);
                intent.putExtra("horizonrepo", horizonRepo);
                intent.putExtra("list", horizontalList);
                startActivityForResult(intent, MIDDLE_ALTER_REQUEST_CODE);
            }
        });

        cancelWaitingDialog();
    }


    private void getData() {
        responseCount = 1;
        getPOI("숙박", 0);
        getPOI("호텔", 0);
        getPOI("TV맛집", 1);
        getPOI("한식", 1);
        getPOI("중식", 1);
        getPOI("양식", 1);
        getPOI("치킨", 1);
        getPOI("놀거리", 2);
        getPOI("레저", 2);
        getPOI("레저", 2);
        getPOI("영화관", 2);
        getPOI("노래방", 2);
    }

    private void getPOI(String category, int index) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.Tmap_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitExService retrofitExService = retrofit.create(RetrofitExService.class);

        Call<JsonObject> call = retrofitExService.getPOIData(1, 1, 20, category, gpsTracker.getLongitude(), gpsTracker.getLatitude(), 33, TmapapiKey);
        Log.d("tag", "getPOI: "+call.request().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if(response.body()==null) {
                        responseCount++;
                        return;
                    }

                    JsonObject searchPoiInfo = response.body();
                    JsonArray poiList = searchPoiInfo.get("searchPoiInfo").getAsJsonObject().get("pois").getAsJsonObject().get("poi").getAsJsonArray();
                    ArrayList<PoiRepo> poiArray = new ArrayList<>();

                    for (JsonElement poi : poiList) {
                        StringBuilder address = new StringBuilder();
                        String poiId = poi.getAsJsonObject().get("id").getAsString();
                        String placeName = poi.getAsJsonObject().get("name").getAsString();
                        String telNo = poi.getAsJsonObject().get("telNo").getAsString();
                        double lat = poi.getAsJsonObject().get("noorLat").getAsDouble();
                        double lon = poi.getAsJsonObject().get("noorLon").getAsDouble();
                        address.append(poi.getAsJsonObject().get("upperAddrName").getAsString() + " ");
                        address.append(poi.getAsJsonObject().get("middleAddrName").getAsString() + " ");
                        address.append(poi.getAsJsonObject().get("lowerAddrName").getAsString() + " ");
                        address.append(poi.getAsJsonObject().get("roadName").getAsString() + " ");
                        address.append(poi.getAsJsonObject().get("buildingNo1").getAsString());
                        String buildingNo2 = poi.getAsJsonObject().get("buildingNo2").getAsString();
                        if (!(buildingNo2.equals("0") || buildingNo2.equals(""))) address.append(buildingNo2);
                        if(placeName.contains("주차장") || placeName.contains("정문") || placeName.contains("후문")) continue;
                        if(category.equals("숙박") && (placeName.contains("호텔") || placeName.contains("HOTEL"))) continue;
                        if(category.equals("놀거리") && placeName.contains("노래")) continue;
                        poiArray.add(new PoiRepo(poiId, placeName, telNo, address.toString(), lat, lon));
                        if(poiArray.size()>=10) break;
                    }

                    horizontalList[index].put(category, new HorizonRepo(index,category,poiArray));
                    if (responseCount >= 12) init();
                    else responseCount++;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("getData fail", "======================================");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MIDDLE_ADD_REQUEST_CODE:
                    HorizonRepo addHorizonRepo = (HorizonRepo) data.getSerializableExtra("returnHR");
                    int addPosition = addHorizonRepo.getvIndex();
                    verticalList.add(addPosition,addHorizonRepo);
                    for (int i = addPosition+1; i <verticalList.size() ; i++) {
                        verticalList.get(i).setvIndex(i);
                    }
                    mAdapter.notifyItemInserted(addPosition);
                    break;
                case MIDDLE_ALTER_REQUEST_CODE:
                    HorizonRepo alterHorizonRepo = (HorizonRepo) data.getSerializableExtra("returnHR");
                    int alterPosition = alterHorizonRepo.getvIndex();
                    verticalList.set(alterPosition,alterHorizonRepo);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }

    }
}
