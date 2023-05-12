package com.server;

import com.app.Observer;
import com.app.target;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TargetsAnimation implements Runnable{
    private int speed1, speed2;
    final private Circle big_target, small_target;
    final private AtomicBoolean gameIsRunning;
    final private ArrayList<Observer> allObservers = new ArrayList<>();

    TargetsAnimation(Circle _big_target, Circle _small_target){
        speed1 = 2;
        speed2 = speed1 * 2;
        big_target = _big_target;
        small_target = _small_target;
        gameIsRunning = new AtomicBoolean(false);
        for(var o : allObservers){
            o.TargetMove(target.bigTarget, big_target.getCenterY());
            o.TargetMove(target.smallTarget, big_target.getCenterY());
        }
    }
    public void AddObserver(Observer o){
        allObservers.add(o);
    }
    public void resetAnimation(){
        gameIsRunning.set(false);
        big_target.setCenterY(0);
        small_target.setCenterY(0);
        speed1 = 2;
        speed2 = speed1 * 2;
        bcastTargetMove(target.bigTarget, big_target.getCenterY());
        bcastTargetMove(target.smallTarget, small_target.getCenterY());
    }
    public void continueAnimation(){
        gameIsRunning.set(true);
    }
    public void stopAnimation(){
        gameIsRunning.set(false);
    }
    public Pair<Double, Double> GetTargetCordOnScene(target t){
        var t_ref = t == target.bigTarget ? big_target : small_target;
        Point2D point;
        synchronized (t_ref) {
            point = t_ref.localToScene(t_ref.getCenterX(), t_ref.getCenterY());
        }
        var pair = new Pair<>(point.getX(), point.getY());
        return pair;
    }
    public double GetTargetRadius(target t){
        return t == target.bigTarget ? big_target.getRadius() : small_target.getRadius();
    }
    private void bcastTargetMove(target t, double cord){
        for (var o : allObservers) {
            o.TargetMove(t, cord);
        }
    }

    private void moveBigTarget(int speed) {
        synchronized (big_target) {
            big_target.setCenterY(big_target.getCenterY() + speed);
            bcastTargetMove(target.bigTarget, big_target.getCenterY());
        }
    }
    private void moveSmallTarget(int speed) {
        synchronized (small_target) {
            small_target.setCenterY(small_target.getCenterY() + speed);
            bcastTargetMove(target.smallTarget, small_target.getCenterY());
        }
    }
    @Override
    public void run() {
        while(true) {
            if (!gameIsRunning.get()) {
                continue;
            }

            if(big_target.getCenterY() > 145 || big_target.getCenterY() < -145){
                speed1 = -speed1;
            }
            moveBigTarget(speed1);

            if(small_target.getCenterY() > 152 || small_target.getCenterY() < -152){
                speed2 = -speed2;
            }
            moveSmallTarget(speed2);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
