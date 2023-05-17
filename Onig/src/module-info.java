module Onig {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.json;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
}
