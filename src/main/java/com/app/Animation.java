package com.app;

import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Animation implements Runnable{
    final private Circle big_target, small_target;
    private int speed1, speed2;
    private Player player;
    private Arrow arrow = null;
    final private AtomicBoolean gameIsRunning, arrowWasShooting;
    final private ArrayList<Observer> allObservers = new ArrayList<>();

    public Animation(Circle _big_target, Circle _small_target, Player p){
        big_target = _big_target;
        small_target = _small_target;
        speed1 = 2;
        speed2 = speed1 * 2;
        player = p;
        gameIsRunning = new AtomicBoolean(false);
        arrowWasShooting = new AtomicBoolean(false);
    }
    public void AddObserver(Observer o){
        allObservers.add(o);
    }
    public void resetAnimation(){
        gameIsRunning.set(true);
        speed1 = 2;
        speed2 = speed1 * 2;
        arrow.arrowToStart();
        arrowWasShooting.set(false);
        Platform.runLater(() ->{for (var o : allObservers) o.ArrowIsShot(false);});
    }
    public void stopAnimation(){
        gameIsRunning.set(false);
    }
    public void makeShot(){
        arrowWasShooting.set(true);
    }
    private void moveBigTarget(int speed)
    {
        big_target.setCenterY(big_target.getCenterY() + speed);
    }
    private void moveSmallTarget(int speed)
    {
        small_target.setCenterY(small_target.getCenterY() + speed);
    }
    private void moveArrow()
    {
        arrow.moveArrow();
    }
    static private boolean doesArrowIntersectCircle(Pair<Double, Double> arrow_head_cords,
                                          Pair<Double, Double> circle_center_cords,
                                          double circle_radius){
        return Math.pow((arrow_head_cords.getKey() - circle_center_cords.getKey()), 2) +
               Math.pow((arrow_head_cords.getValue() - circle_center_cords.getValue()), 2) <= Math.pow(circle_radius, 2);
    }
    static private boolean doesArrowMissTarget(double arrow_head_x_cord){
        return arrow_head_x_cord >= 470;
    }
    @Override
    public void run() {
        while(true) {
            if (!gameIsRunning.get()) {
                continue;
            }

            if(arrowWasShooting.get()){
                Pair<Double, Double> arrowHeadCords = arrow.GetHeadPoint();

                Pair<Double, Double> target_center_cords = new Pair<>(big_target.getCenterX() + big_target.getLayoutX(), big_target.getCenterY() + big_target.getLayoutY());
                boolean arrInterBigTarget = doesArrowIntersectCircle(arrowHeadCords, target_center_cords, big_target.getRadius());

                target_center_cords = new Pair<>(small_target.getCenterX() + small_target.getLayoutX(), small_target.getCenterY() + small_target.getLayoutY());
                boolean arrInterSmallTarget = doesArrowIntersectCircle(arrowHeadCords, target_center_cords, small_target.getRadius());

                if(arrInterBigTarget || arrInterSmallTarget || doesArrowMissTarget(arrowHeadCords.getKey())){
                    if(arrInterBigTarget)
                    {
                        Platform.runLater(()-> {
                            for (var o : allObservers)
                                o.ScoresChanged(player.GetUserName(), 5);});
                    }
                    else if(arrInterSmallTarget)
                    {
                        Platform.runLater(()-> {for (var o : allObservers) o.ScoresChanged(player.GetUserName(), 10);});
                    }
                    arrow.arrowToStart();
                    arrowWasShooting.set(false);
                    Platform.runLater(()-> {
                        for (var o : allObservers) {
                            o.ShotsChanged(player.GetUserName());
                            o.ArrowIsShot(false);
                        }
                    });
                }
                else {
                    moveArrow();
                }
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
