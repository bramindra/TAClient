package com.bramindra.kstpwithdiversity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class AnswerActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView textView_answerText = findViewById(R.id.answerText);
        ArrayList<String> distPath = new ArrayList<>();
        ArrayList<String> scalePath = new ArrayList<>();
        ArrayList<String> pathPath = new ArrayList<>();
        ArrayList<String[]> var1 = new ArrayList<>();
        ArrayList<String[]> var2 = new ArrayList<>();
        ArrayList<String> tagFulfillingPath = new ArrayList<>();
        ArrayList<String> tagPair = new ArrayList<>();
        StringBuilder listAnswer = new StringBuilder();
        int i,flagBracketOpen1,flagBracketOpen2;
        textView_answerText.setText("");

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.globalPreference, MODE_PRIVATE);
        String ansGet = sharedPreferences.getString("listAnswer", "");
        String[] ans;

        assert ansGet != null;
        if (!ansGet.equals("Tidak ada path yang valid")){

            ans = ansGet.split("\\)\\),");
            ans[0] = ans[0].replace("[(","(");
            ans[ans.length-1] = ans[ans.length-1].replace("))]", "");
            for (int x = 0; x<ans.length; x++){
                ans[x]= ans[x].replace(", defaultdict(None, ", ")");
                var1.add(ans[x].split("\\{"));
                var1.get(x)[0] = var1.get(x)[0].replace(" ","");
                var1.get(x)[0] = "[" + var1.get(x)[0] +"]";
                var1.get(x)[1] = var1.get(x)[1].replace("}","");
                var1.get(x)[1] = var1.get(x)[1].replace("), ",");");
                var1.get(x)[1] = var1.get(x)[1].replace("\'","");
                var2.add(var1.get(x)[1].split(";"));
            }

            String temp;
            for(int x = 0; x<ans.length; x++){
                i = 0;
                flagBracketOpen1=0;
                flagBracketOpen2=0;
                temp = var1.get(x)[0];
                while (true){
                    if(i<temp.length()){
                        if(temp.charAt(i)=='('){
                            flagBracketOpen1+=1;
                            if(flagBracketOpen1==1){
                                for(int j=0; j<temp.length();j++){
                                    if(temp.charAt(j)==','){
                                        scalePath.add(temp.substring(i+1,j));
                                        break;
                                    }
                                }
                            }
                            if (flagBracketOpen1==2){
                                for(int j=i; j<temp.length();j++){
                                    if(temp.charAt(j)==','){
                                        distPath.add(temp.substring(i+1,j));
                                        break;
                                    }
                                }
                            }
                        }else if(temp.charAt(i)=='['){
                            flagBracketOpen2+=1;
                            if(flagBracketOpen2==2){
                                for(int j=i; j<temp.length();j++){
                                    if(temp.charAt(j)==']'){
                                        pathPath.add(temp.substring(i+1,j));
                                        for(int k=j+4;k<temp.length();k++){
                                            if(temp.charAt(k)==')'){
                                                tagFulfillingPath.add(temp.substring(j+3,k));
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        break;
                    }
                    i+=1;
                }
            }

            for(int x=0;x<pathPath.size();x++){
                temp = pathPath.get(x);
                temp = temp.replace("\'", "");
                temp = temp.replace(",", " -> ");
                pathPath.set(x,temp);
            }

            for (int x = 0; x<ans.length;x++) {
                StringBuilder string = new StringBuilder();
                for (int y = 0; y<var2.get(x).length;y++) {
                    string.append(var2.get(x)[y]).append("\n");
                }
                tagPair.add(String.valueOf(string));
            }

            for(int x=0;x<ans.length;x++){
                listAnswer.append(x+1).append(". ").append(pathPath.get(x)).append(" with distance : ").append(distPath.get(x)).append(" and ").append(tagFulfillingPath.get(x)).append(" tag(s) fulfilled, with ").append(scalePath.get(x)).append(" distance to tags scale.\n").append(tagPair.get(x));
            }

            textView_answerText.setText(listAnswer);
        }else {
            textView_answerText.setText("Tidak ada path yang valid");
        }
//        textView_answerText.setText(ansGet);
    }
}
