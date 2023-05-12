package com.app;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class Arrow {
    final private Line arrow_line;
    final private Polygon arrow_head;
    final private int move_speed = 20;
    private ArrayList<Double> startLinePos, startHeadPos;
    public Arrow(Line _arrow1, Polygon _arrow2)
    {
        arrow_line = _arrow1;
        startLinePos = new ArrayList<>(Arrays.asList(_arrow1.getStartX(), _arrow1.getEndX()));
        arrow_head = _arrow2;
        startHeadPos = new ArrayList<>(_arrow2.getPoints());
    }
    public void moveArrow()
    {
        moveLine();
        movePolygon();
    }
    public void arrowToStart(){
        arrow_line.setStartX(startLinePos.get(0));
        arrow_line.setEndX(startLinePos.get(1));
        var arrow_head_points = arrow_head.getPoints();
        synchronized (arrow_head_points)
        {
            arrow_head_points.setAll(startHeadPos);
        }
    }
    public void setVisible(boolean value){
        arrow_line.setVisible(value);
        arrow_head.setVisible(value);
    }
    public void SetHeadCords(ArrayList<Double> cords){
        var points = arrow_head.getPoints();
        for(int pindex = 0, cindex = 0; pindex < points.size(); ++pindex)
        {
            if(pindex % 2 == 0){
                points.set(pindex, cords.get(cindex));
                ++cindex;
            }
        }
    }
    public void SetLineCords(Pair<Double, Double> lineCords){
        arrow_line.setStartX(lineCords.getKey());
        arrow_line.setEndX(lineCords.getValue());
    }
    public Pair<Double, Double> GetHeadPointOnScene(){
        var points = arrow_head.getPoints();
        Point2D point;
        synchronized (points) {
            point = arrow_head.localToScene(points.get(2), points.get(3));
        }
        return new Pair<>(point.getX(), point.getY());
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
    public ArrayList<Double> GetLineCords(){
        return new ArrayList<>(Arrays.asList(arrow_line.getStartX(), arrow_line.getEndX()));
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
