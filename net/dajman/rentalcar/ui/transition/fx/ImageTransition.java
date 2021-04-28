package net.dajman.rentalcar.ui.transition.fx;

import javafx.animation.Transition;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;


public class ImageTransition extends Transition {


    private final ImageView imageView;
    private final Image oldImage;
    private final Image newImage;


    public ImageTransition(final ImageView imageView, final Image oldImage, final Image newImage, final Duration duration){
        this.imageView = imageView;
        this.oldImage = oldImage;
        this.newImage = newImage;
        if (oldImage.getWidth() != newImage.getWidth() || oldImage.getHeight() != newImage.getHeight()){
            imageView.setImage(newImage);
            return;
        }
        this.setCycleDuration(duration);
    }

    @Override
    protected void interpolate(double v) {
        final WritableImage writableImage = new WritableImage((int)newImage.getWidth(), (int)newImage.getHeight());
        for(int x = 0; x < writableImage.getWidth(); x++){
            for(int y = 0; y < writableImage.getHeight(); y++){
                final Color fromColor = this.oldImage.getPixelReader().getColor(x ,y);
                final Color toColor = this.newImage.getPixelReader().getColor(x, y);
                final double opacity = fromColor.getOpacity() + (toColor.getOpacity() - fromColor.getOpacity()) * v;
                if (fromColor.getOpacity() == 0){
                    writableImage.getPixelWriter().setColor(x, y, new Color(toColor.getRed(), toColor.getGreen(), toColor.getBlue(), opacity));
                    continue;
                }else if (toColor.getOpacity() == 0){
                    writableImage.getPixelWriter().setColor(x, y, new Color(fromColor.getRed(), fromColor.getGreen(), fromColor.getBlue(), opacity));
                    continue;
                }
                final double red = fromColor.getRed() + (toColor.getRed() - fromColor.getRed()) * v;
                final double green = fromColor.getGreen() + (toColor.getGreen() - fromColor.getGreen()) * v;
                final double blue = fromColor.getBlue() + (toColor.getBlue() - fromColor.getBlue()) * v;
                writableImage.getPixelWriter().setColor(x ,y, new Color(red, green, blue, opacity));
            }
        }
        imageView.setImage(writableImage);
    }



}
