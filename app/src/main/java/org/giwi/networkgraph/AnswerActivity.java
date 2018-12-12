package org.giwi.networkgraph;

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
        String listAnswer = "";
        textView_answerText.setText("");

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String answer = bundle.getString("listAnswer");

        int i=0;
        while(true){
            if(i< answer.length()){
                if(answer.charAt(i)==')'){
                    for(int j = i; j>=0; j--){
                        if(answer.charAt(j)=='('){
                            path.add(answer.substring(j+1,i));
                            break;
                        }
                    }
                }
            }
            else{
                break;
            }
            i+=1;
        }

        for(int k = 0; k < path.size(); k++){
            listAnswer += path.get(k)+'\n';
        }

        textView_answerText.setText(answer);
        listAnswer = "";
    }

}
