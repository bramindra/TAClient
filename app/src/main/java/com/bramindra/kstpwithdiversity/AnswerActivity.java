package com.bramindra.kstpwithdiversity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class AnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView textView_answerText = findViewById(R.id.answerText);
        ArrayList<String> path = new ArrayList<>();
        ArrayList<String> distPath = new ArrayList<>();
        ArrayList<String> scalePath = new ArrayList<>();
        ArrayList<String> pathPath = new ArrayList<>();
        StringBuilder listAnswer = new StringBuilder();
        textView_answerText.setText("");

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String answer = bundle.getString("listAnswer");

        int i=0, flagBracketClose=0, flagBracketOpen=0;
        while(true){
            assert answer != null;
            if(i< answer.length()){
                if(answer.charAt(i)==')'){
                    flagBracketClose+=1;
                    if(flagBracketClose==2){
                        for(int j = i; j>=0; j--){
                            if(answer.charAt(j)=='('){
                                flagBracketOpen+=1;
                                if(flagBracketOpen==2){
                                    path.add(answer.substring(j+1,i));
                                    flagBracketClose=0;
                                    flagBracketOpen=0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else{
                break;
            }
            i+=1;
        }

        for(int x = 0; x<path.size(); x++){
            i=0;
            String temp = path.get(x);
            while(true){
                if (i < path.get(x).length()){
                    if(temp.charAt(i)=='('){
                        for(int j = i; j>=0; j--){
                            if(temp.charAt(j)==','){
                                scalePath.add(temp.substring(0, j));
                                break;
                            }
                        }
                        for(int j = i; true; j++){
                            if(temp.charAt(j)==','){
                                distPath.add(temp.substring(i+1,j));
                                break;
                            }
                        }
                    }
                    else if(temp.charAt(i)=='['){
                        for (int j=i; true;j++){
                            if (temp.charAt(j)==']'){
                                pathPath.add(temp.substring(i+1,j));
                                break;
                            }
                        }
                    }
                }else{
                    break;
                }
                i+=1;
            }
        }

        for (int x=0; x<pathPath.size();x++){
            String temp = pathPath.get(x);
            temp = temp.replace("\'", "");
            temp = temp.replace(", ", " -> ");
            pathPath.set(x, temp);
        }


        for(int k = 0; k < path.size(); k++){
            listAnswer.append(k+1).append(". ").append(pathPath.get(k)).append(" with distance : ").append(distPath.get(k)).append(" and ").append(scalePath.get(k)).append(" distance to tags scale.").append('\n');
        }

        textView_answerText.setText(listAnswer);
    }

}
