package com.app;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

import java.util.ArrayList;

public class Arrow {
    final private Line arrow1;
    final private Polygon arrow_head;
    final private int move_speed = 20;
    public Arrow(Line _arrow1, Polygon _arrow2)
    {
        arrow1 = _arrow1;
        arrow_head = _arrow2;
    }
    public void moveArrow()
    {
        moveLine();
        movePolygon();
    }
    public void arrowToStart(){
        arrow1.setStartX(-104.0);
        arrow1.setEndX(-69.5);
        var arrow_head_points = arrow_head.getPoints();
        synchronized (arrow_head_points)
        {
            arrow_head_points.setAll(-57.5, -24.5, -44.0, -31.0, -57.5, -39.0);
        }
    }
    public void SetHeadCords(ArrayList<Double> cords){
        var points = arrow_head.getPoints();
        for(int i = 0; i < points.size(); ++i)
        {
            if(i % 2 == 0){
                points.set(i, cords.get(i));
            }
        }
    }
    public void SetLineCoords(Pair<Double, Double> lineCords){
        arrow1.setStartX(lineCords.getKey());
        arrow1.setEndX(lineCords.getValue());
    }
    public Pair<Double, Double> GetHeadPoint(){
        return new Pair<>(arrow_head.getPoints().get(2) + arrow_head.getLayoutX(), arrow_head.getPoints().get(3) + arrow_head.getLayoutY());
    }
    public ArrayList<Double> GetHeadCords(){
        var result = new ArrayList<Double>();
        var points = arrow_head.getPoints();
        for(int i = 0; i < points.size(); ++i)
        {
            if(i % 2 == 0){
                result.add(points.get(i));
            }
        }
        return result;
    }
    public Pair<Double, Double> GetLineCords(){
        return new Pair<>(arrow1.getStartX(), arrow1.getEndX());
    }
    private void moveLine()
    {
        arrow1.setStartX(arrow1.getStartX() + move_speed);
        arrow1.setEndX(arrow1.getEndX() + move_speed);
    }
    private void movePolygon()
    {
        var points = arrow_head.getPoints();
        for(int i = 0; i < points.size(); ++i)
        {
            if(i % 2 == 0){
                points.set(i, points.get(i) + move_speed);
            }
        }
    }
}
