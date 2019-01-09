package com.bramindra.kstpwithdiversity;

import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleNode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Objects;

import com.bramindra.kstpwithdiversity.beans.NetworkGraph;
import com.bramindra.kstpwithdiversity.beans.Vertex;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class GraphFragment extends Fragment {

    public static ArrayList<String> vertexname;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        NetworkGraph graph = new NetworkGraph();

        Bundle bundle = getArguments();
        assert bundle != null;
        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.globalPreference, Context.MODE_PRIVATE);
        String graphString = sharedPreferences.getString("graphString", "");

        ArrayList<String> cost = new ArrayList<>();
        ArrayList<String> vertex = new ArrayList<>();
        vertexname = new ArrayList<>();
        ArrayList<Node> nodesarray = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        int i=0;
        while(true){
            assert graphString != null;
            if(i<graphString.length()){
                if(graphString.charAt(i)=='\''){
                    for(int j = i+1; j<graphString.length(); j++){
                        if(graphString.charAt(j)=='\''){
                            if(!vertex.contains(graphString.substring(i+1,j))){
                                vertexname.add(graphString.substring(i+1,j));
                                nodes.add(new SimpleNode(graphString.substring(i+1,j)));
                            }
                            vertex.add(graphString.substring(i+1,j));
                            i=j;
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

        i=0;
        while(true){
            if(i<graphString.length()){
                if(graphString.charAt(i)==')'){
                    for(int j = i; j>=0; j--){
                        if(graphString.charAt(j)==','){
                            cost.add(graphString.substring(j+2,i));
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

        for(int k = 0; k< nodes.size(); k++) {
            graph.getVertex().add(new Vertex(nodes.get(k), ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.avatar)));
        }

        for(int k = 0; k< vertex.size(); k++){
            for(int l = 0; l< nodes.size(); l++){
                if (vertex.get(k).equals(nodes.get(l).getLabel())){
                    nodesarray.add(nodes.get(l));
                }
            }
        }

        for(int k = 0; k< nodesarray.size(); k+=2) {
            graph.addEdge(new SimpleEdge(nodesarray.get(k), nodesarray.get(k + 1), cost.get(k / 2)));
        }

        GraphSurfaceView surface = view.findViewById(R.id.mysurface);
        surface.init(graph);

        return view;
    }
}