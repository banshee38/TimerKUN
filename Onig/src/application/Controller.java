package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Controller {
	BorderPane pane = new BorderPane();
	
	VBox box = new VBox();
	Map<String,String> data =new HashMap<>();
	List<String> name = new ArrayList<>();
	List<Integer> time = new ArrayList<>();
	boolean result = true;  
	JSONObject touroku = new JSONObject();
	JSONArray touroku2 = new JSONArray();
	final String basyo = "hozon.json";
	
	
	  @FXML
	  Button buttonAdd = new Button("登録");
	  @FXML
	  Button buttonStart = new Button("スタート");
	  
	  @FXML
	  Button buttonReset = new Button("リセット");
	  @FXML  
	  // 分のセット
	  TextField txtMin = new TextField();
	  @FXML
	  // 登録名のセット
	  TextField setAdd = new TextField();
	  @FXML
	  // 秒のセット
	  TextField txtSec = new TextField();
	  @FXML
	  // プリセット
	  ComboBox puri = new ComboBox();
	  @FXML
	  // タイマー部分 分表示ラベル
	  Label TimerMin = new Label();
	  @FXML
	  // タイマー部分 秒表示ラベル
	  Label TimerSec = new Label();
	  @FXML
	  // タイマー部分 終了ラベル
	  Label labelTimerFinish = new Label("");
	  @FXML
	//アニメーションクラス
      Timeline timer;
  
	public Controller() {
		readFile();
	}
	
	@FXML
	void start() {
		buttonStart.setOnAction((ActionEvent) -> {

			  buttonAdd.setDisable(true);
		      buttonStart.setDisable(true);
		      buttonReset.setDisable(false);
		      String min = txtMin.getText();
		      String sec = txtSec.getText();
		      TimerMin.setText(min);
		      TimerSec.setText(sec);
		      
		      if (min == null || sec == null) {
		    	  labelTimerFinish.setText("数字を入力してください");
		    	  reset();
		    	  return;
		      } else if (min.trim() == "" || sec.trim() == "") {
		    	  labelTimerFinish.setText("数字を入力してください");
		    	  reset();
		    	  return;
		      }
		      
		      for(int i = 0; i < min.length(); i++) {
		    	    //i文字めの文字についてCharacter.isDigitメソッドで判定する
		    	    if(Character.isDigit(min.charAt(i))) {

		    	      //数字の場合は次の文字の判定へ
		    	      continue;

		    	    }else {

		    	      //数字でない場合は検証結果をfalseに上書きする
		    	      result =false;
		    	      labelTimerFinish.setText("数字を入力してください");
		    	      break;
		    	    }
		    	  }
		      
		      for(int i = 0; i < sec.length(); i++) {

		    	    //i文字めの文字についてCharacter.isDigitメソッドで判定する
		    	    if(Character.isDigit(sec.charAt(i))) {

		    	      continue;

		    	    }else {

		    	      result =false;
		    	      labelTimerFinish.setText("数字を入力してください");
		    	      break;
		    	    }
		    	  }

		  timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			  @FXML 
		        public void handle(ActionEvent event) {
				  if (result == false) {
					  labelTimerFinish.setText("数字を入力してください");
					  
		          // 秒数、分数の繰り下げ
				  } else if (TimerSec.getText().equals("00")) {

		            if (TimerMin.getText().equals("00")) {

		              // 全て0になったら終了ラベルを表示
		              labelTimerFinish.setText("時間が来ました！！");

		            } else {
		              TimerSec.setText("59");
		              TimerMin.setText(String.format("%02d", Integer.parseInt(TimerMin.getText()) - 1));
		            }

		          } else {
		            TimerSec.setText(String.format("%02d", Integer.parseInt(TimerSec.getText()) - 1));
		          }
				  
		        }
		      }));
		  timer.setCycleCount(Timeline.INDEFINITE);

	      // アニメーション開始
	      timer.play();
		  });
		  
		    buttonReset.setOnAction((ActionEvent) -> {

		      timer.stop();

		      reset();
		    });
	}
	
	@FXML
	  void reset() {
		    buttonReset.setDisable(true);
		    buttonStart.setDisable(false);
		    buttonAdd.setDisable(false);

		    TimerSec.setText("00");
		    TimerMin.setText("00");

		    labelTimerFinish.setText("");
		    
		    txtMin.setText("00");
		    txtSec.setText("00");
		    setAdd.setText("");

		  }
	@FXML
	void readFile() {
	    try (BufferedReader in = new BufferedReader(new FileReader(basyo))){
//	    	touroku
        } catch (Exception e) {
            e.printStackTrace();
        }
	  }
	
	void pset() {
//		if() {
//			String name = yomi.getString();
//		}
	}
	  
	  @FXML
	  void setAdd() {
		  buttonAdd.setOnAction(( ActionEvent ) -> {
		  String mei = setAdd.getText();
		  String min = txtMin.getText();
	      String sec = txtSec.getText();
		  if(mei.trim() == "") {
			  labelTimerFinish.setText("登録名を入力してください");
			  buttonReset.setDisable(false);
		  }else {
			  if(min.trim() == "" || sec.trim() == "") {
				  labelTimerFinish.setText("数字を入力してください");
			  }else {
				  data.put(mei,(min + "|" + sec));
					touroku.put(mei,(min + "|" + sec));
					 try (PrintWriter save = new PrintWriter(new FileWriter(basyo))) {
				            save.write(touroku.toString());
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
	//			  for(String key : data.keySet()) {
	//				  String val = data.get(key);
	//				  System.out.println(key + val);
	//			  }
			  }
		  }
	   });
	  }
	  
	  

}
