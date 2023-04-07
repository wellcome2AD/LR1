package com.app;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

import java.util.ArrayList;

public class Arrow {
    final private Line arrow_line;
    final private Polygon arrow_head;
    final private int move_speed = 20;
    public Arrow(Line _arrow1, Polygon _arrow2)
    {
        arrow_line = _arrow1;
        arrow_head = _arrow2;
    }
    public void moveArrow()
    {
        moveLine();
        movePolygon();
    }
    public void arrowToStart(){
        arrow_line.setStartX(-104.0);
        arrow_line.setEndX(-69.5);
        var arrow_head_points = arrow_head.getPoints();
        synchronized (arrow_head_points)
        {
            arrow_head_points.setAll(-57.5, -24.5, -44.0, -31.0, -57.5, -39.0);
        }
    }
    public void setVisible(boolean value){
        arrow_line.setVisible(value);
        arrow_head.setVisible(value);
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
        arrow_line.setStartX(lineCords.getKey());
        arrow_line.setEndX(lineCords.getValue());
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
        return new Pair<>(arrow_line.getStartX(), arrow_line.getEndX());
    }
    private void moveLine()
    {
        arrow_line.setStartX(arrow_line.getStartX() + move_speed);
        arrow_line.setEndX(arrow_line.getEndX() + move_speed);
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
