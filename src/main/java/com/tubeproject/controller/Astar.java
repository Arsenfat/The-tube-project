package com.tubeproject.controller;
import java.sql.SQLException;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class Astar{

    public static List<Node> printPath(Node target){
        List<Node> path = new ArrayList<Node>();

        for(Node node = target; node!=null; node = node.getParent()){
            path.add(node);
        }

        Collections.reverse(path);

        return path;
    }

    public List<String> printPathWithLine(List<Node> path) throws SQLException {

        List<String> pathLines = new ArrayList<>();
        GraphCreation graph = new GraphCreation();
        List<Line> lines = graph.getLine();

        for ( Node node : path)
        {
            List<String> pathLinesTemp = new ArrayList<>();
            for (Line l : lines)
            {
                for (Station station : l.getStations())
                {
                    if (node.getValue().equals(station.getName()))
                    {
                        pathLinesTemp.add(l.getName());
                    }
                }
            }
            for (String s : pathLinesTemp)
            {
                System.out.println("s : " + s);
                if (pathLines.isEmpty())
                {
                    pathLines.add(s);
                }
                else if (!s.equals(pathLines.get(pathLines.size()-1)))
                {
                    pathLines.add(s);
                }
            }
            System.out.println("////////////////////////////////////////////");
        }

        return pathLines;
    }

    //search the shortest path by taking the departure node and the end node
    public static void AstarSearch(Node source, Node goal){

        Set<Node> explored = new HashSet<Node>();

        PriorityQueue<Node> queue = new PriorityQueue<Node>(3000,
                new Comparator<Node>(){
                    //override compare method
                    public int compare(Node i, Node j){
                        if(i.getF_scores() > j.getF_scores()){
                            return 1;
                        }

                        else if (i.getF_scores() < j.getF_scores()){
                            return -1;
                        }

                        else{
                            return 0;
                        }
                    }

                }
        );

        //cost from start is 0 since no path and predecessors yet
        source.setG_scores(0);

        queue.add(source);

        boolean found = false;

        //we continue while the open list is not empty and goal has not been found
        while((!queue.isEmpty())&&(!found)){
            //System.out.println("queue:");
            //System.out.println(queue);

            //the node in having the lowest f_score value
            Node current = queue.poll();

            explored.add(current);

            //if we found the goal we stop
            if(current.getValue().equals(goal.getValue())){
                System.out.println("FOUND !!!");
                found = true;
            }

            //check every neighbour of current node
            for(Edge e : current.getAdjacencies()){
                Node child = e.getTarget();
                //System.out.println("child:");
                //System.out.println(child.getValue());
                double cost = e.getCost();
                for (String line : current.getLines())
                {
                    if (child.getLines().indexOf(line) < 0)
                    {
                        System.out.println("inside line change");
                    }
                }
                double temp_g_scores = current.getG_scores() + cost;
                double temp_f_scores = temp_g_scores + child.getH_scores(goal.getLatitude(), goal.getLongitude());
                System.out.println(child.toString() + " wihth h value: " + child.getH_scores(goal.getLatitude(), goal.getLongitude()) + " and f value:" + temp_f_scores );

                //if the new f cost is higher then we skip it

                if((explored.contains(child)) &&
                        (temp_f_scores >= child.getF_scores())){
                    continue;
                }

                //if the child is not in open list or f score is lower

                else if((!queue.contains(child)) ||
                        (temp_f_scores < child.getF_scores())){

                    child.setParent(current);
                    child.setG_scores(temp_g_scores);
                    child.setF_scores(temp_f_scores);

                    if(queue.contains(child)){
                        queue.remove(child);
                    }

                    queue.add(child);

                }

            }

        }

    }

}