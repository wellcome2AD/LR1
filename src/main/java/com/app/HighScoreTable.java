package com.app;

import com.database.Player;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class HighScoreTable extends TableView {
    public static class HighScore
    {
        private SimpleStringProperty playerName;
        private SimpleIntegerProperty winCount;

        HighScore(String playerName, int winCount){
            this.playerName = new SimpleStringProperty(playerName);
            this.winCount = new SimpleIntegerProperty(winCount);
        }

        public String getPlayerName(){ return playerName.get();}
        public void setPlayerName(String value){ playerName.set(value);}

        public int getWinCount(){ return winCount.get();}
        public void setWinCount(int value){ winCount.set(value);}
    }
    private static ObservableList<HighScore> getObservableList(List<Player> players)
    {
        List<HighScore> highScoresList = new ArrayList<>();
        for (Player player : players)
        {
            highScoresList.add(new HighScore(player.GetPlayerName(), player.GetWinCount()));
        }
        return FXCollections.observableArrayList(highScoresList);
    }
    public HighScoreTable(List<Player> players)
    {
        super(getObservableList(players));
        // столбец для вывода имени
        TableColumn<HighScore, String> nameColumn = new TableColumn<HighScore, String>("Имя игрока");
        // определяем фабрику для столбца с привязкой к свойству name
        nameColumn.setCellValueFactory(new PropertyValueFactory<HighScore, String>("playerName"));
        // добавляем столбец
        getColumns().add(nameColumn);

        // столбец для вывода числа побед
        TableColumn<HighScore, Integer> winCountColumn = new TableColumn<HighScore, Integer>("Число побед");
        winCountColumn.setCellValueFactory(new PropertyValueFactory<HighScore, Integer>("winCount"));
        getColumns().add(winCountColumn);
    }
}