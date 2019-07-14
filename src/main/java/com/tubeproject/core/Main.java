package com.tubeproject.core;

import com.tubeproject.controller.*;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.view.ViewMainScreen;

import java.sql.SQLException;
import java.util.*;

import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws SQLException {

        List<Node> nodes = new ArrayList<Node>();
        List<String> line = new ArrayList<String>();

        //DatabaseConnection.DatabaseOpen();
        //ViewMainScreen.startWindow();

        GraphCreation graph = new GraphCreation();
        nodes = graph.getData();

        Astar aStar = new Astar();

        int start = 100;
        int end = 60;

        System.out.println("starting from: " + nodes.get(start).getValue() + " ending at : " + nodes.get(end).getValue());

        aStar.AstarSearch(nodes.get(start),nodes.get(end));

        List<Node> path = aStar.printPath(nodes.get(end));

        //System.out.println("lines: " + aStar.printPathWithLine(aStar.printPath(nodes.get(78))));

        System.out.println("Path: " + path);

        for (int i = 0; i<path.size()-1;i++)
        {
            int j = i;
            List <String> tempList = path.get(i).getLines();
            List <String> tempList2 = new ArrayList<String>();

            System.out.println("Station :" + path.get(i).getValue() + " Line: " + path.get(i).getLines());
            //line.add(path.get(i).getLines().get(0));

            if (tempList.size() == 1)
            {
                line.add(tempList.get(0));
            }
            else if (tempList.size() > 1)
            {
                while (tempList.size() > 1 && j<path.size())
                {
                    tempList.retainAll(path.get(j).getLines());
                    j++;
                }
            }
            line.add(tempList.get(0));
        }
        line = line.stream().distinct().collect(Collectors.toList());
        System.out.println("line: " + line);

    }

}
