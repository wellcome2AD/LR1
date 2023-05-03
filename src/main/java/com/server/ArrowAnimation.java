package com.server;

import com.app.Arrow;
import com.app.Observer;
import com.app.Player;
import com.app.target;
import javafx.application.Platform;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArrowAnimation implements Runnable {
    final private Player player;
    final private Arrow arrow;
    final private AtomicBoolean gameIsRunning, arrowWasShooting;
    final private ArrayList<Observer> allObservers = new ArrayList<>();
    private TargetsAnimation target_anim;

    public ArrowAnimation(Player p, TargetsAnimation _target_anim){
        player = p;
        target_anim = _target_anim;
        arrow = p.GetArrow();
        gameIsRunning = new AtomicBoolean(false);
        arrowWasShooting = new AtomicBoolean(false);
    }

    public Player GetPlayer() { return player; }
    public void AddObserver(Observer o){
        allObservers.add(o);
    }
    public void resetAnimation(){
        gameIsRunning.set(true);
        arrow.arrowToStart();
        arrowWasShooting.set(false);
        for(var o : allObservers) {
            o.ArrowMove(player.GetPlayerName(), arrow.GetHeadCords(), arrow.GetLineCords());
        }
    }
    public void stopAnimation(){
        gameIsRunning.set(false);
    }
    public void makeShot(){ arrowWasShooting.set(true); }
    private void moveArrow() {
        arrow.moveArrow();
        for(var o : allObservers) {
            o.ArrowMove(player.GetPlayerName(), arrow.GetHeadCords(), arrow.GetLineCords());
        }
    }
    private boolean doesArrowIntersectCircle(target t){
        Pair<Double, Double> arrow_head_cords = arrow.GetHeadPoint();
        var circle_center_cords = target_anim.GetTargetCord(t);
        var circle_radius = target_anim.GetTargetRadius(t);
        return Math.pow((arrow_head_cords.getKey() - circle_center_cords.getKey()), 2) +
                Math.pow((arrow_head_cords.getValue() - circle_center_cords.getValue()), 2) <= Math.pow(circle_radius, 2);
    }
    private boolean doesArrowMissTarget(){
        return arrow.GetHeadPoint().getKey() >= 470;
    }
    @Override
    public void run() {
        while(true) {
            if (!gameIsRunning.get()) {
                continue;
            }

            if(arrowWasShooting.get()){
                boolean arrInterBigTarget = doesArrowIntersectCircle(target.bigTarget);
                boolean arrInterSmallTarget = doesArrowIntersectCircle(target.smallTarget);

                if(arrInterBigTarget || arrInterSmallTarget || doesArrowMissTarget()){
                    if(arrInterBigTarget)
                    {
                        Platform.runLater(()-> {
                            for (var o : allObservers) o.ScoresChanged(player.GetPlayerName(), "5");
                        });
                    }
                    else if(arrInterSmallTarget)
                    {
                        Platform.runLater(()-> {
                            for (var o : allObservers)
                                o.ScoresChanged(player.GetPlayerName(), "10");
                        });
                    }
                    arrow.arrowToStart();
                    for(var o : allObservers){
                        o.ArrowMove(player.GetPlayerName(), arrow.GetHeadCords(), arrow.GetLineCords());
                    }
                    arrowWasShooting.set(false);
                    Platform.runLater(()-> {
                        for (var o : allObservers) {
                            o.ShotsChanged(player.GetPlayerName());
                            o.ArrowIsShot(false);
                        }
                    });
                }
                else {
                    moveArrow();
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

