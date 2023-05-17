package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONObject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Controller  implements Initializable {
	BorderPane pane = new BorderPane();
	
	VBox box = new VBox();
	Map<String,String> data =new HashMap<>();
	boolean result = true;  
	JSONObject touroku = new JSONObject();
	final String basyo = "hozon.json";
	ObservableList<String> nakami = FXCollections.observableArrayList();
	
	  @FXML
	  Button buttonAdd = new Button("登録");
	  @FXML
	  Button buttonStart = new Button("スタート");
	  @FXML
	  Button buttonDel = new Button("セット削除");
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
	  ComboBox<String> puribox = new ComboBox<>();
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
  
	public Controller () {
		readFile();
		start();
		reset();
		puriS ();
		setAdd();
	}
	
	@FXML
	void start() {
		buttonStart.setOnAction((ActionEvent) -> {
			  buttonAdd.setDisable(true);
		      buttonStart.setDisable(true);
		      buttonReset.setDisable(false);
		      String min = txtMin.getText();
		      String sec = txtSec.getText();
		      
		      if(txtMin.getLength() < 2 || txtSec.getLength() < 2) {
		    	  if(txtMin.getLength() < 2) {
		    		  min = ("0" + min);
		    	  }if(txtSec.getLength() < 2) {
		    		  sec = ("0" + sec);
		    	  }
		      }
		      
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
		      
		      if(txtMin.getLength() > 2 || txtSec.getLength() > 2) {
		    	  if(txtMin.getLength() > 2) {
		    		  labelTimerFinish.setText("2桁まで有効です");
			    	  reset();
			    	  return;
		    	  }else if(txtSec.getLength() > 2) {
		    		  labelTimerFinish.setText("2桁まで有効です");
			    	  reset();
			    	  return;
		    	  }
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
     public void readFile() {
		  // ファイルを開く
		  try (FileReader fr = new FileReader(basyo);
		    // ファイルを行ごとに読み取る
		    BufferedReader br = new BufferedReader(fr)) {
		    String line;
		    while ((line = br.readLine()) != null) {
		      // 行をリストに追加する
		    	line = line.replaceFirst("\\{ *", "");
		    	line = line.replaceFirst(" *}", "");
		    	line = line.replaceAll("\"", "");
		    	String[] s = line.split(" *, *");
		    	for (int i = 0; i < s.length; i++) {
		    		String[] ss = s[i].split(":");
		    		data.put(ss[0], ss[1]);
		    		touroku.put(ss[0], ss[1]);
		    		String name = ss[0];
		    		nakami.add(name);
		    	}
		    }
		    
		  } catch (Exception e) {
		    e.printStackTrace();
		  }

	 }
	
	@FXML
	void puriS () {
        puribox.setOnAction((ActionEvent)->{
        	String selectedValue = puribox.getValue();
        	String s = data.get(selectedValue);
        	String[] ss = s.split("\\|");
        	txtMin.setText(ss[0]);
        	txtSec.setText(ss[1]);
        });
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
	@FXML  
	public void delete() {
	  buttonDel.setOnAction((ActionEvent) -> {
	    String selectedValue = puribox.getValue();
		touroku.remove(selectedValue);
		data.remove(selectedValue);
		try (PrintWriter save = new PrintWriter(new FileWriter(basyo))) {
            save.write(touroku.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
	    });
	}
	  

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		puribox.setItems(nakami);
	}
	  
	  

}
