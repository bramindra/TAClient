package org.giwi.networkgraph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class InputFragment extends Fragment {

    private String src, dest;
    private Integer tagScale, distanceScale;
    private boolean[] flag = {false, false, true};
    private Spinner srcSpinner, destSpinner;
    private SeekBar tagDistanceSeek;
    private Switch tagSwitchs;
    private EditText threshold, kstp, tagArrays;
    private TextView minSeekbar, maxSeekbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_input, container, false);

//        source = view.findViewById(R.id.srcInput);
        srcSpinner = view.findViewById(R.id.srcInput);
        destSpinner = view.findViewById(R.id.destInput);
        threshold = view.findViewById(R.id.thresholdInput);
        kstp = view.findViewById(R.id.jumlahstpInput);
        tagSwitchs = view.findViewById(R.id.tagSwitch);
        tagArrays = view.findViewById(R.id.tagArray);
        tagDistanceSeek = view.findViewById(R.id.tagDistanceSeekbar);
        minSeekbar = view.findViewById(R.id.minSeekerValue);
        maxSeekbar = view.findViewById(R.id.maxSeekerValue);

        tagScale = 0;
        distanceScale = 100;

        ArrayList<String> items = GraphFragment.vertexname;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, items);

        srcSpinner.setAdapter(adapter);
        srcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                src = srcSpinner.getSelectedItem().toString().toUpperCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        destSpinner.setAdapter(adapter);
        destSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                dest = destSpinner.getSelectedItem().toString().toUpperCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        tagSwitchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    minSeekbar.setVisibility(View.VISIBLE);
                    maxSeekbar.setVisibility(View.VISIBLE);
                    tagDistanceSeek.setVisibility(View.VISIBLE);
                    tagArrays.setVisibility(View.VISIBLE);
                    tagScale = 50;
                    distanceScale = 50;
                }
                else{
                    tagDistanceSeek.setProgress(50);
                    tagScale = 0;
                    distanceScale = 100;
                    tagArrays.setText("");
                    minSeekbar.setVisibility(View.GONE);
                    maxSeekbar.setVisibility(View.GONE);
                    tagDistanceSeek.setVisibility(View.GONE);
                    tagArrays.setVisibility(View.GONE);
                }
            }
        });

        tagDistanceSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("SetTextI18n")
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tagScale = 100-progress;
                distanceScale = progress;
                minSeekbar.setText("Tags Scale : "+(100-progress));
                maxSeekbar.setText("Distance Scale : "+progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        Button buttonSend = view.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(buttonSendOnClickListener);

        return view;
    }

    View.OnClickListener buttonSendOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View arg0) {
//                    String src = srcSpinner.getText().toString().toUpperCase();
//                    String dest = destination.getText().toString().toUpperCase();
                    final String thres = threshold.getText().toString();
                    final String ks = kstp.getText().toString();
                    String tags = "";
                    tags = tagArrays.getText().toString();

                    final String finalTags = tags;

                    if(thres.equals("")){
                        flag[0]=false;
                        threshold.setError("Threshold is Required");
                    }else{
                        flag[0]=true;
                    }

                    if(ks.equals("")){
                        flag[1]=false;
                        kstp.setError("Number of path is Required");
                    }else{
                        flag[1]=true;
                    }

                    if(tagSwitchs.isChecked()){
                        if(tagArrays.getText().length()==0){
                            flag[2]=false;
                            tagArrays.setError("Tags are Required");
                        }else {
                            flag[2]=true;
                        }
                    }else {
                        flag[2]=true;
                    }


                    if(flag[0] && flag[1] && flag[2]){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.out.write(src + ", " + dest+ ", " + thres + ", " + ks + ", " + tagScale + ", " + distanceScale + "; " + finalTags);
                                MainActivity.out.flush();
                            }
                        }).start();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(MainActivity.socket.isConnected()) {
                                    try {
                                        ByteArrayOutputStream byteArrayOutputStream =
                                                new ByteArrayOutputStream(2048);
                                        int bytesRead;
                                        byte[] buffer = new byte[2048];
                                        boolean end=false;
                                        String resp = "";

                                        while (!end) {
                                            bytesRead = MainActivity.in.read(buffer);
                                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                                            resp += byteArrayOutputStream.toString("UTF-8");
                                            if(resp.substring(resp.length()-14).equals("stopReadBuffer")){
                                                end=true;
                                            }
                                        }

                                        Intent intent = new Intent(getActivity(), AnswerActivity.class);
                                        intent.putExtra("listAnswer", resp);
                                        startActivity(intent);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }else{
                        Toast.makeText(getContext(), "Please input the required field", Toast.LENGTH_SHORT).show();
                    }
//                    SocketSendQueryThread socketSendQueryThread = new SocketSendQueryThread(src, dest, thres, ks, tags);
//                    socketSendQueryThread.run();
//                    Client client = new Client();
//                    client.run();
                }};
}