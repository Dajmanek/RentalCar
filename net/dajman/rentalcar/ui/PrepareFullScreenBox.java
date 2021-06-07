package net.dajman.rentalcar.ui;

import javafx.animation.Animation;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.dajman.rentalcar.ui.transition.swing.FadeTransition;
import net.dajman.rentalcar.ui.transition.swing.SizeAndLocTransition;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.BorderFactory;
import java.awt.*;
import java.util.Queue;

public class PrepareFullScreenBox{


    private static final transient int shadowSize = 25;

    private final Stage stage;
    private final double shadowRadius;
    private final JFrame jFrame;
    private final JPanel jPanel;

    private SizeAndLocTransition showTransition;
    private FadeTransition hideTransition;

    public PrepareFullScreenBox(final Stage stage){
        this(stage, 0);
    }

    public PrepareFullScreenBox(final Stage stage, final double shadowRadius){
        this.stage = stage;
        this.shadowRadius = shadowRadius;
        this.jFrame = new JFrame();
        this.jFrame.setType(Window.Type.UTILITY);
        this.jFrame.setUndecorated(true);
        this.jFrame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.jFrame.setAlwaysOnTop(true);
        final JLayeredPane jLayeredPane;
        this.jFrame.add(jLayeredPane = new JLayeredPane());
        jLayeredPane.add(this.jPanel = new JPanel());
        jLayeredPane.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.jPanel.setBackground(new Color(1f, 1f, 1f, 0.06f));
        this.jPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(1.0f, 1.0f, 1.0f, 0.2f)));
    }


    public void show(final double mouseX, final double mouseY){
        if (this.jFrame == null || this.jFrame.isShowing()){
            return;
        }
        if (this.showTransition != null && this.showTransition.getStatus() == Animation.Status.RUNNING){
            return;
        }
        if (this.hideTransition != null && this.hideTransition.getStatus() == Animation.Status.RUNNING){
            this.hideTransition.stop();
        }
        final Screen screen = this.getScreen(mouseX, mouseY);
        final double screenWidth = screen.getBounds().getWidth();
        final double screenHeight = screen.getBounds().getHeight();

        this.jFrame.setSize((int) screenWidth, (int) screenHeight);
        this.jFrame.setLocation((int) screen.getBounds().getMinX(), (int)screen.getBounds().getMinY());

        int layoutX = (int) (stage.getX() - screen.getBounds().getMinX() + ((stage.getWidth()) - 10 / 2));
        if (layoutX < 0) layoutX = (int) screen.getBounds().getMinX();

        this.jPanel.setSize( 10, 10);
        this.jPanel.setLocation(layoutX, (int) screen.getBounds().getMinY());
        this.jFrame.setOpacity(1.0f);
        this.jFrame.setVisible(true);
        this.stage.toFront();

        this.showTransition = new SizeAndLocTransition(this.jPanel, (int) screenWidth - 20, (int) screenHeight - 60, 10, 10, Duration.millis(180));
        this.showTransition.playFromStart();
    }

    public void hide(){
        if (!this.jFrame.isShowing()){
            return;
        }
        if (this.showTransition != null && this.showTransition.getStatus() == Animation.Status.RUNNING){
            this.showTransition.stop();
        }
        if (this.hideTransition != null && this.hideTransition.getStatus() == Animation.Status.RUNNING){
            return;
        }
        this.hideTransition = new FadeTransition(this.jFrame, 0, Duration.millis(200));
        this.hideTransition.setOnFinished(event -> {
            this.jFrame.setVisible(false);
        });
        this.hideTransition.playFromStart();
    }

    private Screen getScreen(final double x, final double y){
        return Screen.getScreens().stream().filter(screen -> screen.getBounds().contains(x, y)).findFirst().orElse(null);
    }


}
