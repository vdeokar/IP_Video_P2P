package requester;

import java.io.File;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
//import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VideoPlayer extends Application{
	private static String filePath;
	private javafx.scene.media.MediaPlayer player;
	private StackPane root;
	private Stage stageObj;
	private Media media;
	private double durationInSeconds;
	private VideoHandler videoHandler;
	
//	public VideoPlayer(VideoHandler videoHandler){
//		super();
//		this.videoHandler = videoHandler;
//	}
//	    
//	public VideoPlayer(){
//		super();
//	}
	
	public void pause(){
		player.pause();
	}
	
	public void buffer(){
		player.pause();
		displayText("Bufferring...");
		System.out.println("Buffering Begins...");
	}
		
	public void resume(){
		player.play();
	}
		
	public void togglePlayback(){
		//System.out.println(player.getStatus());
		if(player.getStatus().equals(Status.PLAYING)){
			//System.out.println("Trying to pause...");
			player.pause();
			displayText("Paused");
		}
		else{
			//System.out.println("Trying to pause...");
			player.play();
			displayText("Playing...");
		}
	}
		
	double getCurrentTimeInSeconds(){
		return player.getCurrentTime().toSeconds();
	}
	
	double getDurationInSeconds(){
		return durationInSeconds;
	}
     
	public static void main(String[] args) throws Exception{
		System.out.println("Video Launching");
		if(args!=null && args.length > 0)
			filePath = args[0];
		launch(args);
	}
	
	void displayText(String displayText){
		stageObj.setTitle("IP Video P2P - "+ displayText);
	}
	    
	public void start(Stage stage) throws Exception {
//try{
		File file;
		System.out.println("Plaing video "+filePath);
		try{
			if(filePath!=null)
				file = new File(this.filePath);
			else{
				file = new File("/home/servo/Videos/2101973421-1448825095508.mp4");
				filePath = "/home/servo/Videos/2101973421-1448825095508.mp4";
			}
			System.out.println("Found video file at "+file.getAbsolutePath()+":::"+file.toURI().toURL().toString());
			System.out.println(file.toURL().toString());
			System.out.println(file.toURI().toString());
			System.out.println(file.toURI().toURL().toString());
		}
		catch(Exception e){
			System.err.println("Vicky: "+filePath);
			e.printStackTrace();
			file = null;
		}

		stageObj = stage;

    	//Converts media to string URL
	    media = new Media(file.toURI().toURL().toString());
	    player = new   javafx.scene.media.MediaPlayer(media);
	    MediaView viewer = new MediaView(player);
	
	    //change width and height to fit video
	    DoubleProperty width = viewer.fitWidthProperty();
	    DoubleProperty height = viewer.fitHeightProperty();
	    width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
	    height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
	    viewer.setPreserveRatio(true);
	
	    StackPane root = new StackPane();
	    root.getChildren().add(viewer);
	
	    //set the Scene
	    Scene scenes = new Scene(root, 500, 500, Color.BLACK);
	    stageObj.setScene(scenes);
	    stageObj.setTitle("IP Video P2P");
	    stageObj.setFullScreen(true);
	    stageObj.show();
	    player.play();
	       
	    scenes.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	        	togglePlayback();
	        }
	    });
	    scenes.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent keyEvent) {
	        	//System.out.println(keyEvent.getCode());
	        	if(keyEvent.getCode().equals(KeyCode.ENTER) && keyEvent.getCode().equals(KeyCode.ESCAPE)==false)
	        		if(stageObj.isFullScreen())
	        			stageObj.setFullScreen(false);
	        		else
	        			stageObj.setFullScreen(true);
	        	else
	        		if(keyEvent.getCode().equals(KeyCode.ESCAPE)==false)
	        			togglePlayback();
	        }
	    });
	    
	    player.setOnReady(new Runnable() {
	        public void run() {
	            durationInSeconds = media.getDuration().toSeconds();
	        }
	    });

	    player.setOnEndOfMedia(new Runnable() {
	        public void run() {
	            File f = new File(filePath);
	            f.delete();
	        }
	    });
	    
	    
//		}catch(Throwable t){t.printStackTrace();}
	    player.setOnError(new Runnable() {
	        public void run() {
	            System.out.println("Some Error Vicky");
	            MediaException me = player.getError();
	            me.printStackTrace();
	            Stage dialogStage = new Stage();
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            dialogStage.setScene(new Scene(VBoxBuilder.create().
	                children(new Text(me.getMessage()), new Button("Ok.")).
	                alignment(Pos.CENTER).padding(new Insets(5)).build()));
	            dialogStage.show();
	        }
	    });
	    
     }
	
}
